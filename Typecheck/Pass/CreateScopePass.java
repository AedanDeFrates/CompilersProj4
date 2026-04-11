package Typecheck.Pass;
import Typecheck.SymbolTable.*;
import Typecheck.TypeCheckException;
import Absyn.*;

public class CreateScopePass extends Pass<Void> {

   protected Scope currentscope;
   public Scope globalscope;

   public CreateScopePass() 
   {
      this.globalscope = new Scope();
      this.currentscope = globalscope;
   }

   // Notes
   // This pass is creates child local scopes for all the funcs,structs,unions,ifs,and whiles
   // These scopes are defined recursively, all empty, will have bindings added in next two passes



   // Hint: Functions introduce a new nested scope.
   // 1. Create a new Scope whose parent is the current scope.
   // 2. Temporarily switch currentscope to this new scope.
   // 3. Visit the function type, parameters, and body.
   // 4. Store the resulting scope in node.scope.
   // 5. Restore the previous scope.
   @Override
   public Void visitFunDecl(FunDecl node)
   {
      System.out.println("visitFunDecl\n" + "   " + node.name);

      Scope funcScope = new Scope(currentscope);
      Scope prev = currentscope;
      currentscope = funcScope;

      visit(node.type);
      for(Decl parameter: node.params.list)
      {
         visit(parameter);
      }
      visit(node.body);

      node.scope = funcScope;
      currentscope = prev;

      return null;
   }


   // Hint: Struct bodies are evaluated inside their own scope.
   // 1. Create a new Scope whose parent is the current scope.
   // 2. Switch currentscope to this new scope.
   // 3. Visit the struct body.
   // 4. Store this scope in node.scope.
   // 5. Restore the previous scope.
   @Override
	public Void visitStructDecl(StructDecl node) 
   {
      System.out.println("visitStructDecl\n" + "   " + node.name);

      Scope structScope = new Scope(currentscope);
      Scope prev = currentscope;
      currentscope = structScope;

      for(Decl parameter: node.body.list)
         {
         visit(parameter);
      }

      node.scope = structScope;
      currentscope = prev;
      return null;

	}

   // Hint: Union bodies behave like structs for scoping.
   // 1. Create a new Scope whose parent is the current scope.
   // 2. Switch currentscope to the new scope.
   // 3. Visit the union body.
   // 4. Store this scope in node.scope.
   // 5. Restore the previous scope.
	@Override
	public Void visitUnionDecl(UnionDecl node) 
   {
      System.out.println("visitUnionDecl\n" + "   " + node.name);

      Scope unionScope = new Scope(currentscope);
      Scope prev = currentscope;
      currentscope = unionScope;

      for(Decl parameter: node.body.list)
      {
         visit(parameter);
      }

      node.scope = unionScope;
      currentscope = prev;
      return null;
	}


   // Hint: If statements execute inside a fresh scope.
   // 1. Create a new Scope whose parent is the current scope.
   // 2. Switch currentscope to this new scope.
   // 3. Visit the condition and both branches.
   // 4. Save this scope in node.scope.
   // 5. Restore the previous scope.
	@Override
	public Void visitIfStmt(IfStmt node) 
   {
      System.out.println("visitIfStmt\n" + "   IF");

      Scope ifScope = new Scope(currentscope);
      Scope prev = currentscope;
      currentscope = ifScope;

      visit(node.expression);
      visit(node.if_statement);
      visit(node.else_statement);

      node.scope = ifScope;
      currentscope = prev;
      return null;
	}


   // Hint: Loops also introduce a nested scope.
   // 1. Create a new Scope whose parent is the current scope.
   // 2. Switch currentscope to the new scope.
   // 3. Visit the condition and loop body.
   // 4. Store the scope in node.scope.
   // 5. Restore the previous scope.
   @Override
	public Void visitWhileStmt(WhileStmt node) 
   {
      System.out.println("visitWhileStmt\n" + "   WHILE");
      
      Scope whileScope = new Scope(currentscope);
      Scope prev = currentscope;
      currentscope = whileScope;

      visit(node.expression);
      visit(node.statement);

      node.scope = whileScope;
      currentscope = prev;
      return null;
	}
}
