
package Typecheck.Types;
import java.util.Set;
import java.util.HashSet;


public class POINTER extends INT {
   public Type type;
   public static final POINTER INSTANCE = new POINTER(new INT());


   public POINTER(Type t) {
      this.type = t;
   }

   @Override
   public boolean canAccept(Type t) {
      boolean ret = false;
      for (Type syn : t.getSynonyms()) {
         ret |= (syn instanceof INT || syn instanceof POINTER);
      }
      return ret;
   }

   @Override
   public Set<Type> getSynonyms() {
      Set<Type> set = new HashSet<>();
      set.add(this);
      set.add(new INT());
      return set;
   }

   public String toString() {
      return ("POINTER(" + this.type.toString() + ")");
   }

}
