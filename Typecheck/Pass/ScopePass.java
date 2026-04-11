package Typecheck.Pass;
import Absyn.*;
import Typecheck.SymbolTable.*;

public class ScopePass<T> extends Pass<T> {

   protected Scope currentscope;
	protected T defaultReturn = null;

    // Hint: Save scope → switch to node.scope → visit children → restore scope.

   public ScopePass(Scope s) {
      this.currentscope = s;
   }

   // HINT FROM LINE 10
   // 1. In new scope, set the scope to the current node scope
   // 2. visit the children (body, params, members, etc) inside the new scope
   // 3. restore the previous scope

	// wrapper function that switches scope for you
	public void switchScope(Absyn node, Runnable func){
		// 1.
		Scope prevScope = currentscope;
		currentscope = node.scope;

		//2 visit children and custom logic here
		func.run();

		// 3.
		currentscope = prevScope;
	}


   @Override
   public T visitFunDecl(FunDecl node) 
   {
		System.out.println("SCOPE_PASS visitFunDecl\n   " + node.name);
		switchScope(node, () -> {
			visit(node.type);
			visit(node.params);
			visit(node.body);
		});
		return null;
   }

   //PATTERN REPEATS FOR REMAINING FUNCTIONS
   @Override
	public T visitStructDecl(StructDecl node) 
	{
		System.out.println("SCOPE_PASS visitStructDecl\n   " + node.name);
		switchScope(node,()->{
			visit(node.body);
		});
		return null;
	}

	@Override
	public T visitUnionDecl(UnionDecl node) 
	{
		System.out.println("SCOPE_PASS visitUnionDecl\n   " + node.name);
		switchScope(node,()->{
			visit(node.body);
		});
		return null;
	}

	@Override
	public T visitIfStmt(IfStmt node) 
	{
		System.out.println("SCOPE_PASS visitIfStmt\n   IF");
		switchScope(node,()->{
			visit(node.if_statement);
			visit(node.else_statement);
			visit(node.expression);
		});
		return null;
	}

   @Override
	public T visitWhileStmt(WhileStmt node) 
	{
		System.out.println("SCOPE_PASS visitWhileStmt\n   WHILE");
		switchScope(node,()->{
			visit(node.expression);
			visit(node.statement);
		});
	   return null;
	}

}
