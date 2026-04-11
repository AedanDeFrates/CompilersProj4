package Typecheck;
import Parse.*;
import Parse.antlr_build.Parse.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import Typecheck.Pass.*;
import CodeGen.*;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromFileName(args[0]);

        gLexer lexer = new gLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        System.out.println("Starting Parser");
        gParser parser = new gParser(tokens);

        ParseTree tree = parser.program();

        ASTBuilder astBuilder = new ASTBuilder();

        Absyn.DeclList asttree = (Absyn.DeclList)astBuilder.visit(tree);
        System.out.println("\n==========AST==========");
        System.out.println(asttree.print(0));

        try {
            // Passes
            System.out.println("\n==========TYPE_PASS==========");
            TypeAnnotationPass tap = new TypeAnnotationPass();
            asttree.accept(tap);

            System.out.println("\n==========SCOPE_PASS==========");
            CreateScopePass scp = new CreateScopePass();
            asttree.accept(scp);

            System.out.println("\n==========TYPE_SCOPE_PASS==========");
            TypeScopePass tcp = new TypeScopePass(scp.globalscope);
            asttree.accept(tcp);

            System.out.println("\n==========FUN_AND_VAR_SCOPE_PASS==========");
            FunAndVarScopePass fvcp = new FunAndVarScopePass(scp.globalscope);
            asttree.accept(fvcp);

            System.out.println("\n==========PRINT_PASS==========");
            System.out.print("Global Scope ");
            System.out.println(scp.globalscope);
            PrintPass print = new PrintPass(scp.globalscope);
            asttree.accept(print);

            System.out.println("\n==========JUDGEMENT_PASS==========");
            JudgementsPass jp = new JudgementsPass(scp.globalscope);
            asttree.accept(jp);
            System.out.println("Type Check Passed!");

            //=================CODEGEN=================
            ProgramManager pm = new ProgramManager();

            System.out.println("\n==========GLOBAL_VARIABLE_PASS==========");
            GlobalVariablePass gvp = new GlobalVariablePass(pm,scp.globalscope);
            asttree.accept(gvp);

            System.out.println("\n==========CREATE_FUNC_PASS==========");
            CreateFuncPass cfp = new CreateFuncPass(pm,scp.globalscope);
            asttree.accept(cfp);

            System.out.println("\n==========GOTO_PROGRAM==========");
            System.out.println(pm.outputGOTOProgram());

            System.out.println("\n==========INSTRUCTIONS_PASS==========");
            InstructionsPass ip = new InstructionsPass(pm,scp.globalscope);
            asttree.accept(ip);

            System.out.println("\n==========C_PROGRAM==========");
            String cText = pm.outputCProgram();
            System.out.println(cText);

            //Create a C file
            pm.writeCProgram(args[0]);

            // run.sh will call gcc compiler after this
            // to compile the C code into an executable

        } catch (TypeCheckException e) {
            System.err.println("TypeCheckError: " + e.getMessage());
        }

    }
}