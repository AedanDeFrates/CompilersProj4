package CodeGen;

import java.util.ArrayList;
import java.util.List;

/**
 * Type system for the IR.
 *
 * Each IRExpr carries a type. This tells the backend what types 
 * to use in the generated C code.
 */
enum Type {
    INT, STRING, INTARRAY;

    @Override
    public String toString() {
        return switch (this) {
            case INT -> "int";
            case STRING -> "char*";
            case INTARRAY -> "int*";
        };
    }
}

/**
 * Base class for all IR nodes.
 */
abstract class GOTO {
    public <T> T accept(Visitor<T> v) {
        return v.visitGOTO(this);
    }
}

/**
 * Expressions in IR.
 */
abstract class IRExpr extends GOTO {
    public Type type;
}

/**
 * Statements in IR.
 *
 * Anything that performs an action but does not compute a value directly.
 */
abstract class IRStmt extends GOTO {}

/**
 * Builtin operations.
 *
 * This class represents "Builtin" functions. IE functions that come
 * prepackaged with Geaux. 
 *
 * The idea is that when encournting:
 *            var int x = 9;
 *            printf(x);
 *
 * The compiler can insert a builtin to handle printf(). See printf()
 * for a better idea on how this works.
 * In order to make this work, you will need to make special cases
 * for the "builtin" functions in the type checker. Otherwise the typechecker
 * will complain about undefined functions and mismatching types.
 *
 */
class Builtin extends IRStmt {}

/**
 * The top level class that makes up a GOTO program.
 * This will be populated by functions. Each function
 * represents a C function in the generated code.
 * Not necessarily a Geaux function. For example
 * Geaux has nested functions, but C does not.
 * This is something that must be dealt with.

 * Program also provides two unique name generators.
 * This is how students can generate unique names for their
 * variables and labels (which will be important).

 * IMPORTANT: GOTO Programs declare ALL variables as globals
 * Every single variable you plan on using in the entire program,
 * whether it is function local or not, must be in the globals
 * member below.

 * ALSO IMPORTANT: A reasonable question would be:
 * "Wouldn't all variables be in the same scope?
 * Wouldn't we get name clashing if multiple variables have the same name?"
 * The answer is: Yes! and that is a problem that must be solved. This is
 * why one of the suggested passes is renaming variables that shadow each other.
 * It prevents name clashing here.

 * More explanation of this problem can be found in the Var comment.
 */
class Program {
    private int unique_name_counter;
    private int unique_label_counter;

    public ArrayList<Var> globals;
    public ArrayList<Function> funcs;

    public Program() {
        this.unique_name_counter = 0;
        this.unique_label_counter = 0;
        this.globals = new ArrayList<>();
        this.funcs = new ArrayList<>();
    }

    public String getUniqueVarName() {
        return "_x"+(++this.unique_name_counter);
    }

    public String getUniqueLabelName() {
        return "LABEL"+(++this.unique_label_counter);
    }
}

/**
 * Represents a function in IR.
 *
 * Holds a list of IR instructions.
 *
 * Example emitted C:
 * int main() {
 *     int x;
 *     x = 5;
 *     return x;
 * }
 */
class Function {
    public ArrayList<GOTO> instr;
    public String name;
    public String returntype;

    public Function(String name, String ret) {
        this.name = name;
        this.instr = new ArrayList<>();
        this.returntype = ret;
    }
}

/**
 * Printf statement.
 *
 * Emitted C:
 * printf("xi = %d\n", xi);
 *
 * This is mainly for debugging, but it's also cool to write programs
 * that actually, ya know, do something! Putting in these Builtins
 * turns Geuax into an actual usable language.
 */
class Printf extends Builtin {
    public final String format;       
    public final List<IRExpr> args;  

    public Printf(String format, List<IRExpr> args) {
        this.format = format;
        this.args = args;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitPrintf(this);
    }
}

/**
 * Some Additional Builtin's to implement. Read and Write to/from file
 * are pretty self explanatory, but Input is a builtin for getting user
 * input. These are not implemented here or in Emitter. You will need
 * implement the code in Emitter too.
 *
 * Remember, the idea is that Geaux should have a simple "readfromfile("file")"
 * function, and the Emitter turns that into C that actually reads from the file.
 */
class ReadFromFile extends Builtin {}
class WriteToFile extends Builtin {}
class Input extends Builtin {}

/**
 * Variable reference.
 *
 * Var looks simple, but its probably the hardest instruction to work with.
 * When lowering to GOTO, you will run into the problem that two variables
 * in the Geaux program have the same name. Example:
                     var int x = 0;
                     fun int func(int x) {
                     ...
                     }
 * If the final C file declares all variables as global, then this is going
 * to cause an error when we try to run gcc. If we do not intervene and rename
 * one of these variables, the following code will be emitted:

                    int x;
                    int x;
                    int func() {
                    ...
                    }

 * The solution is to rename ALL variables with unique names. Inside the Scope object,
 * assign each var entry a unique name. Change every reference to that variable to the new
 * name. This ensure no name clashing.
 */
class Var extends IRExpr {
    public final String name;

    public Var(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitVar(this);
    }
}

/**
 * Literal constant.
 *
 * Represents either a hardcoded int or string.
 */
class Literal extends IRExpr {
    public final Object value;  

    public Literal(Object value, Type type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitLiteral(this);
    }
}

/**
 * Binary operation.
 *
 * Represents operations like +, *, <, etc.
 *
 * Example emitted C:
 * y + z
 * x < 5
 */
class BinOp extends IRExpr {
    public final String op;
    public final IRExpr left, right;

    public BinOp(String op, IRExpr left, IRExpr right, Type type) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.type = type;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitBinOp(this);
    }
}

/**
 * Unary operation.
 *
 * Example emitted C:
 * +y
 * *p
 */
class UnaryOp extends IRExpr {
    public final String op;
    public final IRExpr expr;

    public UnaryOp(String op, IRExpr expr, Type type) {
        this.op = op;
        this.expr = expr;
        this.type = type;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitUnaryOp(this);
    }
}

/**
 * Function call.
 *
 * Example emitted C:
 * f()

 * Functions do not need arguments, because all
 * variables are global in the final C file.
 */
class Call extends IRExpr {
    public final String func;

    public Call(String func, Type rettype) {
        this.func = func;
        this.type = rettype;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitCall(this);
    }
}

/**
 * Array load: reading from an array.
 *
 * Example emitted C:
 * (*arr + i)
 */
class ArrayLoad extends IRExpr {
    public final Var array;
    public final IRExpr index;

    public ArrayLoad(Var array, IRExpr index, Type type) {
        this.array = array;
        this.index = index;
        this.type = type; 
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitArrayLoad(this);
    }
}

/**
 * Assignment.
 *
 * Example emitted C:
 * x = 5;
 */
class Assign extends IRStmt {
    public final Var target;
    public final IRExpr value;

    public Assign(Var target, IRExpr value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitAssign(this);
    }
}

/**
 * Array store: writing to an array.
 *
 * Example emitted C:
 * (*arr + i) = x;
 */
class ArrayStore extends IRStmt {
    public final Var array;
    public final IRExpr index;
    public final IRExpr value;

    public ArrayStore(Var array, IRExpr index, IRExpr value) {
        this.array = array;
        this.index = index;
        this.value = value; 
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitArrayStore(this);
    }
}

/**
 * Array allocation.
 *
 * Example emitted C:
 * arr = realloc(&arr, size * sizeof({this.array.type}));
 *
 * Note: The type in the sizeof call is the type of the Var array member.
 *
 * Be mindful that Geaux Arrays are actually dynamic lists. If a user tries to store
 * an out of bounds index, YOU need to resize the array by calling ArrayAlloc to make that work
 * If they try to read from an out of bounds index, then the program should throw an
 * error.
 */
class ArrayAlloc extends IRStmt {
    public final Var array;
    public final IRExpr size;

    public ArrayAlloc(Var array, IRExpr size) {
        this.array = array;
        this.size = size;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitArrayAlloc(this);
    }
}

/**
 * If statement implemented with goto statements
 *
 * Example emitted C:
 * if (x < 5) goto LABEL_TRUE;
 * goto LABEL_FALSE;

 * Your program is responsible for placing the true and false labels in the proper
 * locations. That does not happen automatically. 
 */
class IfStmt extends IRStmt {
    public final IRExpr cond;
    public final String trueLabel;
    public final String falseLabel;

    public IfStmt(IRExpr cond, String trueLabel, String falseLabel) {
        this.cond = cond;
        this.trueLabel = trueLabel;
        this.falseLabel = falseLabel;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitIfStmt(this);
    }
}

/**
 * Goto statement.
 *
 * Example emitted C:
 * goto LABEL;
 */
class Goto extends IRStmt {
    public final String label;

    public Goto(String label) { 
        this.label = label; 
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitGoto(this);
    }
}

/**
 * Label definition.
 *
 * Example emitted C:
 * LABEL:
 */
class Label extends IRStmt {
    public final String name;

    public Label(String name) { 
        this.name = name; 
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitLabel(this);
    }
}

/**
 * Return statement.
 *
 * Example emitted C:
 * return x;
 */
class ReturnStmt extends IRStmt {
    public final IRExpr value;

    public ReturnStmt(IRExpr value) { 
        this.value = value; 
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visitReturnStmt(this);
    }
}
