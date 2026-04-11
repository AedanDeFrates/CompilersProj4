package Typecheck.Pass;
import Typecheck.Types.*;
import java.util.ArrayList;
import java.util.stream.*;
import Typecheck.TypeCheckException;

public class TypeAnnotationPass extends Pass<Void> {


   // NOTES:
   // This pass assigns Typecheck.Type classes to the typeAnnotation field of the parent Absyn class
   // foreach node in the AST that is a variable declaration.
   // POINTER,LIST,ARRAY are compound types, POINTERs are of a specific type,
   // and ARRAYs and LISTs are mutli-dimensional.

    // Hint: Build the base type from the name, then wrap it for pointers and any [] modifiers.
    // 1. Construct the base type ("string" -> STRING)
    // 2. Wrap the base type in a POINTER Type if stars count > 0
    // 3. If the Array has concrete values ([10][3][9]), Then construct a LIST
    //         a. Loop over the List of brackets ([x][i][j]...)
    //            For the first bracket ([x]) construct a List of "basetype"
    //                  LIST(basetype, basetype, basetype,... x times)
    //            For the next bracket ([i]) construct a List of the PREVIOUS List
    //                  LIST(
    //                     LIST(basetype, basetype, basetype,... x times),
    //                     LIST(basetype, basetype, basetype,... x times),
    //                     LIST(basetype, basetype, basetype,... x times),
    //                     LIST(basetype, basetype, basetype,... x times),
    //                     ... i times
    //                  )
    //            Keep repeating until no more brackets
    // 4. If the Array does not have expressions ([][][]...), then construct an ARRAY
    //         a. Pull the first bracket ([]) and construct an ARRAY(basetype)
    //            Pull the next bracket and construct an ARRAY(ARRAY(basetype))
    //            Keep repeating.


   @Override
   public Void visitType(Absyn.Type node) {

      System.out.println("visitType");
      
       // Here is how I checked if the type needed ARRAY or a LIST:
       // Feel free to use it or change it. 
      boolean isARRAY = node.brackets.list.stream()
         .allMatch(e -> ((Absyn.ArrayType)e).size instanceof Absyn.EmptyExp);
      boolean isLIST = node.brackets.list.stream()
         .allMatch(e -> ((Absyn.ArrayType)e).size instanceof Absyn.DecLit);
      if (!isARRAY && !isLIST && node.brackets.list.size() != 0) 
         throw new TypeCheckException("Array has invalid parameters in []");

      //Construct base type
      Type basetype = node.name.equals("int") ? new INT() :
                  node.name.equals("string") ? new STRING() :
                  node.name.equals("void") ? new VOID() :
                  new ALIAS(node.name);

      //Wrap in POINTER if necessary
       //loop to account for multiple pointers
      if(node.pointerCount>0){
          for (int i = 0; i < node.pointerCount; i++) {
              basetype = new POINTER(basetype);
          }
      }

     if(isARRAY){
        for(Absyn.Decl bracket : node.brackets.list){
           basetype = (Type) new ARRAY(basetype);
        }
     }

     else if(isLIST){
        ArrayList<Absyn.Decl> brackets = node.brackets.list;
        // Process brackets in reverse so int[3][2] produces LIST(3 x LIST(2 x INT))
        for(int bi = brackets.size()-1; bi >= 0; bi--){
            Absyn.DecLit lit = (Absyn.DecLit)((Absyn.ArrayType)brackets.get(bi)).size;
            int num = lit.value;
            ArrayList<Type> typelist = new ArrayList<>();
            for(int i = 0;i<num;i++){
                typelist.add(basetype);
            }
            basetype = new LIST(typelist);
        }
     }
     
     //Annoate node with corresponding Type
      node.typeAnnotation = basetype;

      return null;
   }

   // Propagate the type annotation from the Absyn.Type child up to the declaration node.
   // This is what FunAndVarScopePass and TypeScopePass read via node.typeAnnotation.

   @Override
   public Void visitVarDecl(Absyn.VarDecl node) {
      visit(node.type);
      node.typeAnnotation = node.type.typeAnnotation;
      visit(node.init);
      return null;
   }

   @Override
   public Void visitParameter(Absyn.Parameter node) {
      visit(node.type);
      node.typeAnnotation = node.type.typeAnnotation;
      return null;
   }

   @Override
   public Void visitStructMember(Absyn.StructMember node) {
      visit(node.type);
      node.typeAnnotation = node.type.typeAnnotation;
      return null;
   }

   @Override
   public Void visitUnionMember(Absyn.UnionMember node) {
      visit(node.type);
      node.typeAnnotation = node.type.typeAnnotation;
      return null;
   }
}
