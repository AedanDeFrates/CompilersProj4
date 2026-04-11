
package Typecheck.Types;
public class VOID extends Type {
   public static final VOID INSTANCE = new VOID();

   public VOID() {}

   public boolean canAccept(Type t) { return false; }

   public String toString() {
      return ("VOID()");
   }

}

