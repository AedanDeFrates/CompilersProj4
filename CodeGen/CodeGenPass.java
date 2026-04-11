package CodeGen;

import Typecheck.Pass.ScopePass;
import Typecheck.SymbolTable.Scope;

// Abstract parent class for all Code Generation passes
// Has access to the program manager and AST scope
public abstract class CodeGenPass<T> extends ScopePass<T> {

    ProgramManager pm;
    public CodeGenPass(ProgramManager p, Scope s) {
        super(s);
        this.pm = p;
    }
}
