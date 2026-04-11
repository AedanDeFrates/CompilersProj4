package CodeGen;

import Absyn.FunDecl;
import Typecheck.SymbolTable.Scope;

public class CreateFuncPass extends CodeGenPass<Void>{
    public CreateFuncPass(ProgramManager p, Scope s) {
        super(p, s);
    }

    // Creates a new Function in the program
    @Override
    public Void visitFunDecl(FunDecl node) {
        System.out.println("CREATE_FUNC_PASS visitFunDecl\n   " + node.name);

        switchScope(node,()->{
            String name = node.name;
            String returnType = node.type.name;
            Function func =  new Function(name,returnType);
            pm.program.funcs.add(func);

            visit(node.type);
            visit(node.params);
            visit(node.body);
        });
        return null;
    }
}
