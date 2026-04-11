
package Absyn;
import java.util.ArrayList;

public class StmtList extends Stmt{   
   public ArrayList<Stmt> list;
   public StmtList (int p) {
      pos = p;
      this.list = new ArrayList<Stmt>();
   }


   public String print(int depth) {
      if (list.size() == 0) {
         return "  ".repeat(depth) + "StmtList()";
      }
 
      String l = "";

      for (int i = 0; i < list.size(); i++){
         l += list.get(i).print(depth+1) + "\n";
      }

      return "  ".repeat(depth) + "StmtList(\n" +
         l + 
         "  ".repeat(depth) + ")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitStmtList(this);
	}
}
