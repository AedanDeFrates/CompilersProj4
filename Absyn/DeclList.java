
package Absyn;
import java.util.ArrayList;

public class DeclList extends Decl{   
   public ArrayList<Decl> list;
   public DeclList (int p) {
      pos = p;
      this.list = new ArrayList<Decl>();
   }


   public String print(int depth) {
      if (list.size() == 0) {
         return "  ".repeat(depth) + "Decllist()";
      }
      String l = "";

      for (int i = 0; i < list.size(); i++){
         l += list.get(i).print(depth+1) + "\n";
      }

      return "  ".repeat(depth) + "DeclList(\n" +
         l +
         "  ".repeat(depth) + ")";
   }


	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitDeclList(this);
	}
}
