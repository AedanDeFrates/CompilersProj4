package Absyn;

public class EmptyExp extends Exp{
    public EmptyExp(int p) {
        pos=p;
    }


   public String print(int depth) {
      return "  ".repeat(depth) + "EmptyExp()";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitEmptyExp(this);
	}
}
