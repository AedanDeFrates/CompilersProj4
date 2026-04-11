
package Absyn;
import java.util.ArrayList;

public class ExpList extends Exp{   
   public ArrayList<Exp> list;
   public ExpList (int p) {
      pos = p;
      this.list = new ArrayList<Exp>();
   }
  
   public String print(int depth) {
       if (list.size() == 0) {
         return "  ".repeat(depth) + "Explist()";
      }
      
      String l = "";

      for (int i = 0; i < list.size(); i++){
         l += list.get(i).print(depth+1) + "\n";
      }

      return "  ".repeat(depth) + "ExpList(\n" +
         l + 
         "  ".repeat(depth) + ")";
   }



	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitExpList(this);
	}
}
