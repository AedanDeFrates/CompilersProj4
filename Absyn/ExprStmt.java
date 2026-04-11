package Absyn;
public class ExprStmt extends Stmt{
  public Exp expression;
  public ExprStmt(int p, Exp e) {
    pos = p;
    expression = e;
  }

   public String print(int depth) {
      return "  ".repeat(depth) + "ExprStmt(\n" +
               this.expression.print(depth+1) + "\n" +
               "  ".repeat(depth) + ")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitExprStmt(this);
	}
}
