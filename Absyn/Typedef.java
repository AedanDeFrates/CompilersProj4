package Absyn;
import java.util.ArrayList;

public class Typedef extends Decl{
  public Type type;
  public String name;
  public Typedef(int p, Type t, String n) {
      pos = p;
      type = t;
      name = n;
  }


   public String print(int depth) {
      return "  ".repeat(depth) +"Typedef(\n" + 
               this.type.print(depth+1) +",\n" + 
               "  ".repeat(depth+1) + this.name.toString() + "\n" +
               "  ".repeat(depth) +")";
   }


	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitTypedef(this);
	}
}
