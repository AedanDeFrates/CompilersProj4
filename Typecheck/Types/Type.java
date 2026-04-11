
package Typecheck.Types;
import java.util.Set;
import java.util.HashSet;

public abstract class Type {
   public abstract boolean canAccept(Type t);
   public Set<Type> getSynonyms() {return Set.of(this);}
}

