package Absyn;

public class Absyn {
  public int pos;
  public Typecheck.SymbolTable.Scope scope;
  public Typecheck.Types.Type typeAnnotation;

	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitAbsyn(this);
	}
}
