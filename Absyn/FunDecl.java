package Absyn;
import java.util.ArrayList;

public class FunDecl extends Decl{

  public Type type;
  public String name;
  public DeclList params;
  public Stmt body;

  public FunDecl(int p, Type t, String n, DeclList pl, Stmt cs) {
    pos = p;
    type=t;
    name=n;
    params=pl;
    body=cs;
  }


   public String print(int depth) {
      return "  ".repeat(depth) + "FunDecl(\n" +
               this.type.print(depth+1) + ",\n" +
               "  ".repeat(depth+1) +this.name + ",\n" +
               this.params.print(depth+1) + ",\n" +
               this.body.print(depth+1) + "\n" +
               "  ".repeat(depth) + ")";

   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitFunDecl(this);
	}
}
