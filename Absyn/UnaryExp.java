package Absyn;
public class UnaryExp extends Exp {
    public Exp exp;
    public String prefix;
   public UnaryExp(int p, String pre, Exp e) {
        pos=p;
        exp=e;
        prefix=pre;
   }

   public String print(int depth) {
      return "  ".repeat(depth) +"UnaryExp(\n" +
               this.exp.print(depth+1) + ",\n" +
               "  ".repeat(depth+1) +this.prefix + "\n"+
               "  ".repeat(depth) +")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitUnaryExp(this);
	}
}
