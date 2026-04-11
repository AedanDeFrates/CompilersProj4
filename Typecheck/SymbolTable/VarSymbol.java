
package Typecheck.SymbolTable;
import Typecheck.Types.*;

public class VarSymbol extends Symbol {

   public String name;
   public Type type;

   public VarSymbol(String n, Type t) {
      this.name = n;
      this.type = t;
   }

   @Override
   public String toString(){
      return type.toString();
   }


}
