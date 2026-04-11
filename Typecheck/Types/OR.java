
package Typecheck.Types;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

// This is only for unions

public class OR extends Type {
   public ArrayList<Type> options;

   public OR(ArrayList<Type> tl) {
      this.options = tl;
   }

   @Override
   public boolean canAccept(Type tt) { 
      for (Type t: tt.getSynonyms()) {
         for (Type option : this.options) {
            if (option.canAccept(t)) {
               return true;
            }
         }
      }
      return false;
   }

   public Set<Type> getSynonyms() {
      Set<Type> set = new HashSet<>();
      for (Type o : this.options) {
         set.add(o);
      }
      return set;
   }


   public String toString() {
      String ret = "";
      ret += ("OR(");
      for (Type t: this.options) {
         ret += (", " + t.toString());
      }
      ret += (")");
      return ret;
   }

}
