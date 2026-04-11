
package Absyn;

public class EmptyStmt extends Stmt{

    public EmptyStmt(int p) {
        pos=p;
    }

   public String print(int depth) {
      return "  ".repeat(depth) + "EmptyStmt()";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitEmptyStmt(this);
	}
}
