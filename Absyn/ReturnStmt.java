package Absyn;
public class ReturnStmt extends Stmt{
  public Exp expression;
  public ReturnStmt(int p, Exp e) {
    pos = p;
    expression = e;
  }
  public String print(int depth) {
    return "  ".repeat(depth) + "Return(\n" + 
      expression.print(depth+1) + "\n" +
      "  ".repeat(depth) + ")";
  }

	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitReturnStmt(this);
	}
}
