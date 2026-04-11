package Absyn;
public class WhileStmt extends Stmt{
  public Exp expression;
  public Stmt statement;
  public WhileStmt(int p, Exp e, Stmt s) {
    pos = p;
    expression = e;
    statement = s;
  }

   public String print(int depth) {
      return "  ".repeat(depth) +"WhileStmt(\n" +
              "  ".repeat(depth+1) + "Check:\n" +
               this.expression.print(depth+2) + ",\n" +
              "  ".repeat(depth+1) + "Body:\n" +
               this.statement.print(depth+2) + "\n" +
               "  ".repeat(depth) +")";
   }




	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitWhileStmt(this);
	}
}
