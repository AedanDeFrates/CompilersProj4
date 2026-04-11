package Typecheck.Pass;
import Typecheck.Types.*;
import Typecheck.SymbolTable.*;
import java.util.ArrayList;
import Typecheck.TypeCheckException;

public class TypeScopePass extends ScopePass<Void> {

   public TypeScopePass(Scope s) {
      super(s);
   }
   // Hint: Structs define a new type from their member types.
   // 1. Visit the body so member types are fully resolved.
   // 2. Collect each member's typeAnnotation.
   // 3. Build a LIST type from them.
   // 4. Register the struct name in the current scope.
   @Override
	public Void visitStructDecl(Absyn.StructDecl node) 
   {
      System.out.println("visitStructDecl\n" + "   " + node.name);
      // 1.
      for(Absyn.Decl d: node.body.list)
      {
         visit(d);
         System.out.println("      MEMBER VISITED");
      }
      
      // 2.
      ArrayList<Typecheck.Types.Type> typeAnnotationList = new ArrayList<>();
      for (Absyn.Decl d : node.body.list)
      {
         if (d instanceof Absyn.StructMember sm)
         {
            typeAnnotationList.add(sm.typeAnnotation);
         }
      }

      // 3.
      LIST structDecl = new LIST(typeAnnotationList);
      
      // 4.
      TypeSymbol sym = new TypeSymbol(node.name, structDecl);
      this.currentscope.addType(node.name, sym);

		return null;
   }
   // Hint: Unions define a type that can be any of their member types.
   // 1. Visit the body so member types are resolved.
   // 2. Collect each member's typeAnnotation.
   // 3. Build an OR type from them.
   // 4. Register the union name in the current scope.
   @Override
	public Void visitUnionDecl(Absyn.UnionDecl node) 
   {
      System.out.println("visitUnionDecl\n" + "   " + node.name);
      // 1.
      for(Absyn.Decl d : node.body.list)
      {
         visit(d);
         System.out.println("      MEMBER VISITED");
      }

      // 2.
      ArrayList<Typecheck.Types.Type> typeAnnotationList = new ArrayList<>();
      for (Absyn.Decl d : node.body.list)
      {
         if(d instanceof Absyn.UnionMember um)
         {
            typeAnnotationList.add(um.typeAnnotation);
         }
      }

      // 3.
      OR orDecl = new OR(typeAnnotationList);

      // 4.
      TypeSymbol sym = new TypeSymbol(node.name, orDecl);
      this.currentscope.addType(node.name, sym);

      return null;
   }

   // Hint: Typedef introduces a new name for an existing type.
   // Visit the type first, then register the alias in the current scope.
   @Override
	public Void visitTypedef(Absyn.Typedef node)
   {
      visit(node.type);
      TypeSymbol sym = new TypeSymbol(node.name, node.type.typeAnnotation);
      this.currentscope.addType(node.name, sym);
		return null;
	}

   // Hint: Replace ALIAS types with their real definition.
   // Remember that Types can be nested (IE ARRAY(ARRAY(ARRAY(...))) )
   // Traverse the whole type to search for Aliases. Once an alias is found,
   // look up the type of the alias in the symbol table.
      // This is a function I found helpful to implement. If you have a solution
      // in mind that does not include a helper function, then feel free to ignore
   private void resolveAliases(Type t)
   {
      if (t instanceof ALIAS) {
         ALIAS alias = (ALIAS) t;
         if (alias.name != null && currentscope.hasType(alias.name)) {
            Type resolved = currentscope.getType(alias.name).type;
            alias.setType(resolved);
            // Recursively resolve the inner type too
            resolveAliases(resolved);
         } else {
            throw new TypeCheckException("Unknown type: " + (t instanceof ALIAS ? ((ALIAS)t).name : t));
         }
      } else if (t instanceof ARRAY) {
         resolveAliases(((ARRAY) t).type);
      } else if (t instanceof POINTER) {
         resolveAliases(((POINTER) t).type);
      } else if (t instanceof LIST) {
         for (Type elem : ((LIST) t).typelist) {
            resolveAliases(elem);
         }
      } else if (t instanceof OR) {
         for (Type opt : ((OR) t).options) {
            resolveAliases(opt);
         }
      }
      // INT, STRING, VOID need no resolution
   }

   // Hint: Visit the brackets and resolve the alias to a type (if the typeAnnotation contains ALIAS)
   @Override
   public Void visitType(Absyn.Type node) 
   {
      visit(node.brackets);
      resolveAliases(node.typeAnnotation);
      return null;
   }

}
