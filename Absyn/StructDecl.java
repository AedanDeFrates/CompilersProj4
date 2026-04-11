package Absyn;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class StructDecl extends Decl{

    public String name;
    public DeclList body;

    public StructDecl(int p, String n, DeclList b) {
      pos = p;
      name=n;
      body=b;
  }
   
   public String print(int depth) {
      return "  ".repeat(depth) +"StructDecl(\n" +
               "  ".repeat(depth+1) +this.name + ",\n" +
               this.body.print(depth+1) + "\n" +
               "  ".repeat(depth) +")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitStructDecl(this);
	}
}
