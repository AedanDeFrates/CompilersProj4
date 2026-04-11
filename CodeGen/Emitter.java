package CodeGen;
import java.util.ArrayList;
import java.util.List;

public class Emitter {

    public static class ProgramEmitter {

        private ArrayList<Var> globals;
        private ArrayList<Function> funcs;

        public ProgramEmitter(ArrayList<Var> globals, ArrayList<Function> funcs) {
            this.globals = globals;
            this.funcs = funcs;
        }

        private InstructionEmitter instrEmitter = new InstructionEmitter();

        public String emitProgram() {
            StringBuilder sb = new StringBuilder();

            sb.append("#include <stdlib.h>\n");
            sb.append("#include <stdio.h>\n");

            // 1. Emit global variable declarations
                for (Var v : globals) {
                    sb.append(v.type.toString()).append(" ").append(v.name).append(";\n");
                }
            sb.append("\n");

            // 2. Emit functions
            for (Function f : funcs) {
                sb.append(f.returntype + " ").append(f.name).append("() {\n");
                for (GOTO instr : f.instr) {
                    sb.append(instr.accept(instrEmitter)).append("\n");
                }
                sb.append("}\n\n");
            }
            return sb.toString();
        }
    }

    public static class InstructionEmitter implements Visitor<String> {

        @Override
        public String visitGOTO(GOTO instr) {
            throw new RuntimeException("Something bad happened\nEmail: blara4@lsu.edu");
        }

        @Override
        public String visitVar(Var instr) {
            return instr.name;
        }

        @Override
        public String visitLiteral(Literal instr) {
            switch (instr.type) {
                case INT -> { return instr.value.toString(); }
                case STRING -> { return "\"" + instr.value.toString() + "\""; }
                default -> throw new RuntimeException("Unsupported literal type: " + instr.type);
            }
        }

        @Override
        public String visitBinOp(BinOp instr) {
            return String.format("(%s %s %s)",
                                 visit(instr.left),
                                 instr.op,
                                 visit(instr.right)
                                 );
        }

        @Override
        public String visitUnaryOp(UnaryOp instr) {
            return String.format("(%s(%s))",
                                 instr.op,
                                 visit(instr.expr));
        }

        @Override
        public String visitCall(Call instr) {
            String ret = instr.func + "()";
            return ret;
        }

        @Override
        public String visitArrayLoad(ArrayLoad instr) {
            return String.format("*(%s+%s)",
                                 visit(instr.array),
                                 visit(instr.index));
        }
        
        @Override
        public String visitAssign(Assign instr) {
                    return String.format("%s = %s;",
                                         visit(instr.target),
                                         visit(instr.value)
                                         );
        }

        @Override
        public String visitArrayStore(ArrayStore instr) {
            String ret = "";
            String idx = visit(instr.index);
            String val = visit(instr.value);
            ret += "*(" + visit(instr.array) + " + " + idx + ") = " + val + ";\n";
            return ret;
        }

        @Override
        public String visitArrayAlloc(ArrayAlloc instr) {
            String arrayName = visit(instr.array);
            String size = visit(instr.size);
            String type = instr.array.type.toString();
                return String.format("%s = realloc(%s, sizeof(%s) * %s);\n",
                                     arrayName,
                                     arrayName,
                                     type,
                                     size);
        }

        @Override
        public String visitIfStmt(IfStmt instr) {
            return String.format("if (%s) goto %s;\ngoto %s;",
                                 visit(instr.cond),
                                 instr.trueLabel,
                                 instr.falseLabel);
        }

        @Override
        public String visitGoto(Goto instr) {
            return String.format("goto %s;",
                                 instr.label);
        }

        @Override
        public String visitLabel(Label instr) {
            return String.format("%s:",
                                 instr.name);
        }

        @Override
        public String visitReturnStmt(ReturnStmt instr) {
            return String.format("return %s;",
                                 visit(instr.value));
        }

        public String visitPrintf(Printf instr) {
            StringBuilder sb = new StringBuilder();
            sb.append("printf(\"").append(instr.format).append("\"");
            for (IRExpr arg : instr.args) {
                sb.append(", ").append(visit(arg));
            }
            sb.append(");");
            return sb.toString();
        }

    }


}
