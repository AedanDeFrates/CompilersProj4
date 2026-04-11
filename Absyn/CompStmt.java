package Absyn;
import java.util.ArrayList;
public class CompStmt extends Stmt{
  public DeclList decl_list;
  public StmtList stmt_list;
  public CompStmt(int p, DeclList dl, StmtList sl) {
    pos = p;
    decl_list = dl;
    stmt_list = sl;
  }

   public String print(int depth) {
      return "  ".repeat(depth) + "CompStmt(\n" +
               "  ".repeat(depth+1) + "Declarations:\n " +
                  this.decl_list.print(depth+2) + "\n" +
               "  ".repeat(depth+1) + "Statements:\n " +
                  this.stmt_list.print(depth+2) +"\n" +
                  "  ".repeat(depth) + ")";

   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitCompStmt(this);
	}
}
