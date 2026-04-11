package Absyn;

public class Literal<T> extends Exp {
   public T value;
   public Literal(int p, T v) {
      pos = p;
      value = v;
   }


	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitLiteral(this);
	}
}
