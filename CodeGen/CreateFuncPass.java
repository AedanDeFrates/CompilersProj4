package CodeGen;

import Absyn.FunDecl;
import Typecheck.SymbolTable.Scope;

public class CreateFuncPass extends CodeGenPass<Void>{
    public CreateFuncPass(ProgramManager p, Scope s) {
        super(p, s);
    }

    /*
    This creates an empty GOTO Function object for each function declaration it passes in the AST.
    Instructions are added later in the Instruction Pass
     */
    // Creates a new Function in the program
    @Override
    public Void visitFunDecl(FunDecl node) {
        System.out.println("CREATE_FUNC_PASS visitFunDecl\n   " + node.name);

        //moves into function's scope
        switchScope(node,()->{
            String name = node.name;
            String returnType = node.type.name;
            //creates a GOTO function shell with empty instructions.
            Function func =  new Function(name,returnType);
            //adds the function to the program
            pm.program.funcs.add(func);

            visit(node.type);
            visit(node.params);
            visit(node.body);
        });
        return null;
    }
}
