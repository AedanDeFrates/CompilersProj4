package Absyn;
import java.util.ArrayList;

public class UnionMember extends Parameter {
    public UnionMember(int p, Type t, String n) {
      super(p,t,n);
    }

   @Override
   public String print(int depth) {
      return "  ".repeat(depth) +"UnionMember(\n" +
               this.type.print(depth+1) + ",\n" +
               "  ".repeat(depth+1) +this.name + "\n" +
               "  ".repeat(depth) +")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitUnionMember(this);
	}
}
