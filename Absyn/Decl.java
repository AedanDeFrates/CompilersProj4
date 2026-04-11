package Absyn;
public class Decl extends Absyn {
   public String print(int depth) {return "";}

	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitDecl(this);
	}
}
