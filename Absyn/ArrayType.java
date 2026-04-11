package Absyn;
import java.util.ArrayList;

public class ArrayType extends Decl{
    public Exp size;
    public ArrayType(int p, Exp s) {
        pos=p;
        size=s;
    }

   public String print(int depth) {
      return "  ".repeat(depth) + "[" + "\n" +
            this.size.print(depth+1) +"\n" +
            "  ".repeat(depth) + "]";
   }

	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitArrayType(this);
	}
}
