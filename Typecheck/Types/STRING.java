
package Typecheck.Types;
public class STRING extends Type {
   public static final STRING INSTANCE = new STRING();

   public STRING() {}
   
   public boolean canAccept(Type t) { return t instanceof STRING; }

   public String toString() {
      return ("STRING()");
   }

}
