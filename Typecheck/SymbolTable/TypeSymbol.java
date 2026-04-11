
package Typecheck.SymbolTable;
import Typecheck.Types.*;

public class TypeSymbol extends Symbol {
   
   public String name;
   public Type type;

   public TypeSymbol(String n, Type t) {
      this.name = n;
      this.type = t;
   }

   @Override
   public String toString(){
      return type.toString();
   }

}
