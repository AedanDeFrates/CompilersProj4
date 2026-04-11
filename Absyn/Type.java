package Absyn;
import java.util.ArrayList;

public class Type extends Decl{
    public String name;
    public int pointerCount;
    public DeclList brackets;
    public boolean constant;

    public Type(int p, boolean c, String n, int pc, DeclList b) {
        pos=p;
        constant=c;
        name = n;
        pointerCount = pc;
        brackets = b;
    }

   public String print(int depth) {
      return "  ".repeat(depth) +"Type(\n" +
               "  ".repeat(depth+1) + "Constant: " + this.constant + ",\n" +
               "  ".repeat(depth+1) + "Base Type: " + this.name + ",\n" +
               "  ".repeat(depth+1) + "Pointer Stars: " + this.pointerCount + ",\n" +
               this.brackets.print(depth+1) + "\n" +
               "  ".repeat(depth) +")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitType(this);
	}
}
