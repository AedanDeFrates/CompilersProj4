
package Typecheck.Types;

public class ARRAY extends Type {
   public Type type;

   public ARRAY(Type t) {
      this.type = t;
   }

   public boolean canAccept(Type t) {
      for (Type syn : t.getSynonyms()) {
         if ((syn instanceof ARRAY)) {
            ARRAY arr = (ARRAY) syn;
            boolean maybe = this.type.canAccept(arr.type);
            if (maybe) {return true;}
         } else if (t instanceof LIST) {
            LIST list = (LIST) syn;
            boolean maybe =  list.typelist.stream()
               .allMatch(e -> this.type.canAccept(e));
            if (maybe) {return true;}
         } 
      }
      return false;
   }

   public String toString() {
      return ("ARRAY(" + this.type.toString() + ")");
   }

}
