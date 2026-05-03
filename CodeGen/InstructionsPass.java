package CodeGen;

import java.util.ArrayList;
import java.util.List;

import Absyn.*;
import Absyn.BinOp;
import Absyn.ReturnStmt;
import Absyn.IfStmt;
import Typecheck.SymbolTable.Scope;

//This pass creates all the functions and their instructions in the GOTO ir
public class InstructionsPass extends CodeGenPass<Object>{

    Function currentFunc;

    public InstructionsPass(ProgramManager p, Scope s) {
        super(p, s);
    }

    /*
    ALL GOTO nodes:
        1. Var - created in GlobalScopePass, need for assignments, use ID
        2. Literal - Int or String Literal, use DeclLit and StrLit
        3. BinOp - use BinaryOp
        4. UnaryOp - use UnaryOp
        5. Assign - use VarDecl and AssignExp
        6. ReturnStmt - use ReturnStmt
        7. Call - use ExprStmt and FunExp
        8. IfStmt - need goto for control, use IfStmt
        9. GotoStmt - implement ifs and loops, use IfStmt and WhileStmt
        10. Label - create labels for gotos, use IfStmt and WhileStmt
        11. ArrayLoad - read array value, use ?
        12. ArrayStore - use ?
        13. ArrayAlloc - change size of array, use
        14. Builtin - typecheck add builtins to globalscope, ignore in CreateFuncPass, use FunDecl

     All GOTO Instructions: need to be added to their respective function using addInst()
        - Builtin
        - Call (if ExprStmt)
        - Assign
        - ArrayStore
        - ArrayAllocation
        - If
        - Goto
        - Return
     */


    /*
    While loop in GOTO
    label1:
    if(condition){
        statement...
        GOTO label1;
    }
     */

    //adds instruction to the current function
    void addInst(GOTO inst){
        if(inst==null){
            throw new RuntimeException("Cannot add null instruction to function");
        }
        if(currentFunc==null){
            throw new RuntimeException("Cannot add instruction in global scope. Function switching may not be working correctly.");
        }
        currentFunc.instr.add(inst);
    }

    // returns a Var from the program given its original name
    Var findVar(String name){
        String uniqueName = pm.varNameTranslator.get(name);

        Var var = null;
        for(Var v : pm.program.globals){
            if(v.name.equals(uniqueName)){
                var = v;
            }
        }

        if(var==null){
            throw new RuntimeException(
                    String.format("Can't find var %s with unique name %s in program",name,uniqueName)
            );
        }

        return var;
    }

    // returns a Var from the program given its unique (mangled) name
    Var findVarByUniqueName(String uniqueName){
        for(Var v : pm.program.globals){
            if(v.name.equals(uniqueName)){
                return v;
            }
        }
        throw new RuntimeException("Can't find var with unique name " + uniqueName);
    }

    // returns a Function from the program given its name
    Function findFunc(String name){
        Function func = null;
        for(Function f : pm.program.funcs){
            if(f.name.equals(name)){
                func = f;
            }
        }

        if(func==null){
            throw new RuntimeException(
                    String.format("Can't find func %s in program",name)
            );
        }

        return func;
    }

    // IDs can be for variables, functions, or types
    // Returns Var if for a variable, nothing if for a function
    @Override
    public Object visitID(ID node) {
        System.out.println("INSTRUCTION_PASS visitID\n   " + node.value);
        try {
            return findVar(node.value);
        }
        catch (RuntimeException e) {

               if(currentscope.hasFun(node.value))
                   return null; //findFunc(node.value);
               else throw e;
        }
    }

    // Sets function as current function, visits children,
    // then restores current function
    @Override
    public Object visitFunDecl(FunDecl node) {

        System.out.println("INSTRUCTION_PASS visitFunDecl\n   " + node.name);
        switchScope(node,()->{
            Function func =  findFunc(node.name);

            Function prevFunc = currentFunc;
            currentFunc = func;

            visit(node.type);
            visit(node.params);
            visit(node.body);

            currentFunc = prevFunc;
        });
        return null;
    }

    @Override
    public Object visitDecLit(DecLit node) {
        System.out.println("INSTRUCTION_PASS visitDecLit\n   " + node.value);
        Literal lit = new Literal(node.value,Type.INT);
        return lit;
    }

    @Override
    public Object visitStrLit(StrLit node) {
        System.out.println("INSTRUCTION_PASS visitStrLit\n   " + node.value);
        Literal lit = new Literal(node.value,Type.STRING);
        return lit;
    }

    @Override
    public Object visitBinOp(BinOp node) {
        System.out.println("INSTRUCTION_PASS visitBinaryOp\n   " + node.oper);
        IRExpr left = (IRExpr)visit(node.left);
        String op = node.oper;
        IRExpr right = (IRExpr)visit(node.right);
        CodeGen.BinOp bin = new CodeGen.BinOp(op,left,right,Type.INT);
        return bin;
    }

    @Override
    public Object visitUnaryExp(UnaryExp node) {
        System.out.println("INSTRUCTION_PASS visitUnaryOp\n   " + node.prefix);
        String pre = node.prefix;
        IRExpr exp = (IRExpr)visit(node.exp);
        CodeGen.UnaryOp un = new CodeGen.UnaryOp(pre,exp,Type.INT);
        return un;
    }

    @Override
    public Object visitVarDecl(VarDecl node) {
        System.out.println("INSTRUCTION_PASS visitVarDecl\n   " + node.name);
        Var var = findVar(node.name);
        IRExpr init = (IRExpr)visit(node.init);

        //no initialization = no assignment instr needed
        if(init==null){
            return null;
        }

        Assign assign = new Assign(var,init);

        if(currentFunc!=null){
            addInst(assign);
            return assign;
        }

        // currentFunc==null, means in global scope,
        try{
            // global variables can be declared in global scope,
            // but in C they may not be able to be initialized
            // to be safe initial assignments will be put at top of main()
            currentFunc = findFunc("main");
            addInst(assign);
            currentFunc = null;

            return assign;

        } catch (RuntimeException e) {
            throw new RuntimeException("Program missing main() function");
        }
    }

    @Override
    public Object visitAssignExp(AssignExp node) {
        System.out.println("INSTRUCTION_PASS visitAssignExp\n   " + "assign");

        if (node.left instanceof ArrayExp) {
            ArrayExp ae = (ArrayExp) node.left;
            Var arrayVar = (Var) visit(ae.name);
            IRExpr index = (IRExpr) visit(ae.index_list.list.get(0));
            IRExpr value = (IRExpr) visit(node.right);

            // Create a temp global to hold the index (avoids double-evaluation)
            String tempName = pm.program.getUniqueVarName();
            Var tempIndex = new Var(tempName, Type.INT);
            pm.program.globals.add(tempIndex);
            addInst(new Assign(tempIndex, index));

            // Bounds check: if (tempIndex >= sizeVar) realloc and update size
            String sizeUniqueName = pm.arraySizeVarNames.get(arrayVar.name);
            if (sizeUniqueName != null) {
                Var sizeVar = findVarByUniqueName(sizeUniqueName);
                String resizeLabel = "RESIZE_" + pm.program.getUniqueLabelName();
                String noResizeLabel = "NORESIZE_" + pm.program.getUniqueLabelName();

                IRExpr outOfBounds = new CodeGen.BinOp(">=", tempIndex, sizeVar, Type.INT);
                addInst(new CodeGen.IfStmt(outOfBounds, resizeLabel, noResizeLabel));

                addInst(new Label(resizeLabel));
                IRExpr newSize = new CodeGen.BinOp("+", tempIndex, new Literal(1, Type.INT), Type.INT);
                addInst(new ArrayAlloc(arrayVar, newSize));
                addInst(new Assign(sizeVar, new CodeGen.BinOp("+", tempIndex, new Literal(1, Type.INT), Type.INT)));

                addInst(new Label(noResizeLabel));
            }

            addInst(new ArrayStore(arrayVar, tempIndex, value));
            return null;
        }

        Var var = (Var) visit(node.left);
        IRExpr exp = (IRExpr) visit(node.right);
        Assign assign = new Assign(var, exp);
        addInst(assign);
        return assign;
    }

    @Override
    public Object visitReturnStmt(ReturnStmt node) {
        System.out.println("INSTRUCTION_PASS visitFunDecl\n   " + node.expression);
        IRExpr value = (IRExpr)visit(node.expression);
        CodeGen.ReturnStmt re = new CodeGen.ReturnStmt(value);
        addInst(re);
        return re;
    }

    @Override
    public Object visitIfStmt(IfStmt node) {
        System.out.println("INSTRUCTION_PASS visitIfStmt\n   " + "if statement");
        
        // Lower the condition expression to an IRExpr
        IRExpr cond = (IRExpr) visit(node.expression);
        
        String trueLabel = "TRUE_" + pm.program.getUniqueLabelName();
        String falseLabel = "FALSE_" + pm.program.getUniqueLabelName();
        String endLabel = "END_" + pm.program.getUniqueLabelName();
        
        CodeGen.IfStmt is = new CodeGen.IfStmt(cond, trueLabel, falseLabel);
        addInst(is);
        
        // add True branch
        addInst(new Label(trueLabel));
        visit(node.if_statement);
        addInst(new GotoStmt(endLabel));
        
        // add False branch (else)
        addInst(new Label(falseLabel));
        if (node.else_statement != null) {
            visit(node.else_statement);
        }
        addInst(new Label(endLabel));
        
        return null;
    }

    @Override
    public Object visitWhileStmt(WhileStmt node) {
        System.out.println("INSTRUCTION_PASS visitWhileStmt\n   " + "while statement");

        IRExpr cond = (IRExpr) visit(node.expression);

        String startLabel = "START_" + pm.program.getUniqueLabelName();
        String bodyLabel = "BODY_" + pm.program.getUniqueLabelName();
        String endLabel = "END_" + pm.program.getUniqueLabelName();

        addInst(new Label(startLabel));

        CodeGen.IfStmt is = new CodeGen.IfStmt(cond, bodyLabel, endLabel);
        addInst(is);
        
        addInst(new Label(bodyLabel));
        visit(node.statement);
        
        addInst(new GotoStmt(startLabel));
        
        addInst(new Label(endLabel));
        
        return null;
    }

    @Override
    public Object visitExprStmt(ExprStmt node) {
        System.out.println("INSTRUCTION_PASS visitExprStmt\n   " + "expression statement");

        Object result = visit(node.expression);
        
        if (result instanceof Call) {
            FunStmt fs = new FunStmt(((Call) result).func);
            addInst(fs);
        }
        else if (result instanceof IRStmt) {
            addInst((IRStmt) result);
        }
        return result;
    }


    @Override
    public Object visitArrayExp(ArrayExp node) {
        System.out.println("INSTRUCTION_PASS visitArrayExp");
        Var arrayVar = (Var) visit(node.name);
        IRExpr index = (IRExpr) visit(node.index_list.list.get(0));
        return new ArrayLoad(arrayVar, index, Type.INT);
    }

    @Override
    public Object visitFunExp(FunExp node) {
        System.out.println("INSTRUCTION_PASS visitFunExp\n   " + "function call");

        String name = ((ID) node.name).value;

        if ("input".equals(name)) {
            String tmpName = pm.program.getUniqueVarName();
            Var tmpVar = new Var(tmpName, Type.INT);
            pm.program.globals.add(tmpVar);
            addInst(new Input(tmpVar));
            return tmpVar;
        }

        if ("readFromFile".equals(name)) {
            IRExpr filename = (IRExpr) visit(node.params.list.get(0));
            String tmpName = pm.program.getUniqueVarName();
            Var tmpVar = new Var(tmpName, Type.STRING);
            pm.program.globals.add(tmpVar);
            addInst(new ReadFromFile(tmpVar, filename));
            return tmpVar;
        }

        if ("writeToFile".equals(name)) {
            IRExpr filename = (IRExpr) visit(node.params.list.get(0));
            IRExpr content  = (IRExpr) visit(node.params.list.get(1));
            return new WriteToFile(filename, content);
        }

        if ("printf".equals(name))
        {
            IRExpr formatExpr = (IRExpr) visit(node.params.list.get(0));
            String format = (String) ((Literal) formatExpr).value;

            List<IRExpr> args = new ArrayList<>();
            for (int i = 1; i < node.params.list.size(); i++)
            {
                args.add((IRExpr) visit(node.params.list.get(i)));
            }

            return new Printf(format, args);
        }
        else
        {
            // Pre-assign arguments to the function's parameter global variables
            ArrayList<String> paramNames = pm.funcParamVarNames.get(name);
            if (paramNames != null && node.params != null) {
                for (int i = 0; i < node.params.list.size() && i < paramNames.size(); i++) {
                    IRExpr argExpr = (IRExpr) visit(node.params.list.get(i));
                    Var paramVar = findVarByUniqueName(paramNames.get(i));
                    addInst(new Assign(paramVar, argExpr));
                }
            }

            Type returnType = Type.INT;
            Call c = new Call(name, returnType);
            return c;
        }
    }
}
