package Absyn;
import java.util.ArrayList;

public class StructMember extends Parameter {
    public StructMember(int p, Type t, String n) {
      super(p,t,n);
    }

   
   public String print(int depth) {
      return "  ".repeat(depth) +"StructMember(\n" +
               this.type.print(depth+1) + ",\n" +
               "  ".repeat(depth+1) +this.name + "\n" +
               "  ".repeat(depth) +")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitStructMember(this);
	}
}
