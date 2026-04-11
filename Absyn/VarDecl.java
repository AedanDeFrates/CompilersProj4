package Absyn;
public class VarDecl extends Decl{
    public Type type;
    public String name;
    public Exp init;

    public VarDecl(int p, Type t, String n, Exp i) {
        pos=p;
        type = t;
        name = n;
        init = i;
    }

   public String print(int depth) {
      return "  ".repeat(depth) +"VarDecl(\n" +
               this.type.print(depth+1) + ",\n" +
               "  ".repeat(depth+1) +this.name + " = \n" +
               this.init.print(depth+2) + ")";
   }




	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitVarDecl(this);
	}
}
