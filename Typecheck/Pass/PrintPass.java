package Typecheck.Pass;
import Absyn.*;
import Typecheck.TypeCheckException;
import Typecheck.SymbolTable.Scope;
import java.util.ArrayList;

import Absyn.IfStmt;
import Absyn.WhileStmt;


public class PrintPass extends ScopePass<Void> {
    public PrintPass(Scope s) {
        super(s);
    }

    @Override
    public Void visitFunDecl(FunDecl node)
    {
        System.out.print("Func " + node.name  +" Scope ");
        System.out.println(node.scope);

        Scope prev = currentscope;
        currentscope = node.scope;

        visit(node.type);
        visit(node.params);
        visit(node.body);

        currentscope = prev;

        return null;
    }


    @Override
    public Void visitStructDecl(StructDecl node)
    {

        System.out.print("Struct " + node.name  +" Scope ");
        System.out.println(node.scope);

        Scope prev = currentscope;
        currentscope = node.scope;

        visit(node.body);

        currentscope = prev;

        return null;


    }


    @Override
    public Void visitUnionDecl(UnionDecl node)
    {
        System.out.print("Union " + node.name  +" Scope ");
        System.out.println(node.scope);

        Scope prev = currentscope;
        currentscope = node.scope;

        visit(node.body);

        currentscope = prev;

        return null;
    }


    @Override
    public Void visitIfStmt(IfStmt node)
    {
        System.out.print("If Scope ");
        System.out.println(node.scope);

        Scope prev = currentscope;
        currentscope = node.scope;

        visit(node.expression);
        visit(node.if_statement);
        visit(node.else_statement);

        currentscope = prev;

        return null;

    }


    @Override
    public Void visitWhileStmt(WhileStmt node)
    {
        System.out.print("While Scope ");
        System.out.println(node.scope);

        Scope prev = currentscope;
        currentscope = node.scope;

        visit(node.expression);
        visit(node.statement);

        currentscope = prev;

        return null;
    }

}