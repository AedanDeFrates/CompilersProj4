package Absyn;
public class AssignExp extends Exp {
    public Exp left;
    public Exp right;
    public AssignExp(int p, Exp l, Exp r) {
        pos=p;
        left=l;
        right=r;
    }

   public String print(int depth) {
      return "  ".repeat(depth) + "AssignExp(\n" +
              this.left.print(depth+1) + ",\n" +
              this.right.print(depth+1) + "\n" +
              "  ".repeat(depth) + ")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitAssignExp(this);
	}
}
