package Absyn;
public class Exp extends Absyn {
   public String print(int depth) {return "";}

	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitExp(this);
	}
}
