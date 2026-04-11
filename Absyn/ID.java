package Absyn;

public class ID extends Literal<String> {
   public ID(int p, String v) {
      super(p,v);
   }
   public String print(int depth) {
      return "  ".repeat(depth) + "ID(" +
         this.value +")";
   }


	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitID(this);
	}
}
