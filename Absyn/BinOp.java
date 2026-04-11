package Absyn;
public class BinOp extends Exp {
   public Exp left, right;
   public String oper;
   public BinOp(int p, Exp l, String o, Exp r) {pos=p; left=l; oper=o; right=r;}


   @Override
   public String print(int depth) {
      return "  ".repeat(depth) +"BinOp(\n" +
               this.left.print(depth+1) + ",\n" +
              "  ".repeat(depth+1) + this.oper + ",\n" +
               this.right.print(depth+1) + "\n" +
               "  ".repeat(depth) + ")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitBinOp(this);
	}
}
