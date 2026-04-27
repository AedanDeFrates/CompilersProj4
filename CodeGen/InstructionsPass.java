package CodeGen;

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
        // for now assume visit(node.left) will always return a Var,
        // in theory compiler should be enforcing this beforehand
        // but this has not been tested and checks may be necessary
        Var var =  (Var)visit(node.left);
        IRExpr exp = (IRExpr)visit(node.right);


        Assign assign = new Assign(var,exp);
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
        GOTO exp = (GOTO)visit(node.expression);
        return super.visitExprStmt(node);
    }

    @Override
    public Object visitFunExp(FunExp node) {
        return super.visitFunExp(node);
    }
}
