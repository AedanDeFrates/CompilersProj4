package Absyn;

public class StrLit extends Literal<String> {
   public StrLit(int p, String v) {
      super(p,v);
   }
   
   public String print(int depth) {
      return "  ".repeat(depth) +"StrLit(" +
         this.value + ")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitStrLit(this);
	}
}
