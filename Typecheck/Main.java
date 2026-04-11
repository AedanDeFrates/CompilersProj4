package Typecheck;
import Parse.*;
import Parse.antlr_build.Parse.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import Typecheck.Pass.*;

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
        //System.out.println(asttree.print(0));

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
            PrintPass pp = new PrintPass(scp.globalscope);
            asttree.accept(pp);

            System.out.println("\n==========JUDGEMENT_PASS==========");
            JudgementsPass jp = new JudgementsPass(scp.globalscope);
            asttree.accept(jp);

            System.out.println("Type Check Passed!");
        } catch (TypeCheckException e) {
            System.err.println("TypeCheckError: " + e.getMessage());
        }
    }
}