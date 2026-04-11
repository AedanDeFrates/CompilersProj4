package Absyn;
import java.util.ArrayList;

public class Parameter extends Decl {
    public Type type;
    public String name;
    public Parameter(int p, Type t, String n) {
        pos=p;
        name = n;
        type = t;
    }


   public String print(int depth) {
      return "  ".repeat(depth) +"Parameter(\n" + 
          this.type.print(depth+1) +",\n"+ 
          "  ".repeat(depth+1) +this.name + "\n" +
          "  ".repeat(depth) + ")";
   }


	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitParameter(this);
	}
}
