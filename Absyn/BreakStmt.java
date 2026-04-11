package Absyn;
public class BreakStmt extends Stmt{
  public BreakStmt(int p) {
    pos = p;
  }

   public String print(int depth) {
      return "  ".repeat(depth) + "BreakStmt()";
   }


	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitBreakStmt(this);
	}
}
