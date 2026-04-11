package Absyn;
public class FunExp extends Exp {
    public Exp name;
    public ExpList params;

    public FunExp(int p, Exp n, ExpList i) {
        pos=p;
        name = n;
        params = i;
    }

   public String print(int depth) {
      return "  ".repeat(depth) + "FunExp(\n" +
               this.name.print(depth+1) + ",\n" +
               this.params.print(depth+1) + "\n"+
               "  ".repeat(depth)+")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitFunExp(this);
	}
}
