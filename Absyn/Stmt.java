package Absyn;
public class Stmt extends Absyn {
   public String print(int depth) {return "";}

	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitStmt(this);
	}
}
