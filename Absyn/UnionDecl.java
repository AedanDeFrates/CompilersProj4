package Absyn;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class UnionDecl extends Decl{

    public String name;
    public DeclList body;

    public UnionDecl(int p, String n, DeclList b) {
      pos = p;
      name=n;
      body=b;
  }
   
   public String print(int depth) {
      return "  ".repeat(depth) +"UnionDecl(\n" +
               "  ".repeat(depth) + this.name + ",\n" +
               this.body.print(depth+1) + "\n" +
               "  ".repeat(depth) + ")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitUnionDecl(this);
	}
}
