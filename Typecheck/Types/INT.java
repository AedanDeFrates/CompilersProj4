
package Typecheck.Types;
public class INT extends Type {
   public static final INT INSTANCE = new INT();
   
   public INT() {}

   public String toString() {
      return "INT()";
   }

   public boolean canAccept(Type t) {
      for (Type syn : t.getSynonyms()) {
         if (syn instanceof INT) {
            return true;
         }
      }
      return false;
   }

}
