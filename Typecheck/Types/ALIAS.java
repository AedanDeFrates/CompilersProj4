
package Typecheck.Types;
import java.util.Set;
import java.util.HashSet;
import Typecheck.TypeCheckException;

public class ALIAS extends Type {
   public String name;
   private Type type;

   // We don't know enough information to assign 
   // the type yet. Maybe we can do it later?
   public ALIAS(String n) {
      this.name = n;
      this.type = null;
   }

   public void setType(Type t) {
      this.type = t;
   }

   public Type getType() {
      if (this.type == null) {
         throw new TypeCheckException("ALIAS has not been given a type yet");
      } else return this.type;
   }

   public boolean canAccept(Type t) {return this.type.canAccept(t);}

   public Set<Type> getSynonyms() {
      Set<Type> set = new HashSet<>();
      set.add(this);
      set.add(this.type);
      return set;
   }

   public String toString() {
      return ("ALIAS("+this.type+")");
   }

}
