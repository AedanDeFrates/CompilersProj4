package Absyn;
public class ArrayExp extends Exp {
    public Exp name;
    public ExpList index_list;
    public ArrayExp(int p, Exp n, ExpList i) {
        pos=p;
        name = n;
        index_list = i;
    }

    

   public String print(int depth) {
      return "  ".repeat(depth) + "ArrayExp(\n" +
              this.name.print(depth+1) + ",\n" +
                this.index_list.print(depth+1) + "\n" +
                "  ".repeat(depth) + ")";
   }

	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitArrayExp(this);
	}
}
