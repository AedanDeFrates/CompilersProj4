package Typecheck.Pass;
import Typecheck.Types.*;
import Typecheck.SymbolTable.*;
import Typecheck.TypeCheckException;
import java.util.ArrayList;

import Absyn.IfStmt;
import Absyn.WhileStmt;

// This pass implements the type rules.
// Some of the logic has been implemented for you in the Types.
// Check out the "canAccept" functions.
public class JudgementsPass extends ScopePass<Void> {
   public JudgementsPass(Scope s) {
      super(s);
   }


   //check if the type is a number
   private boolean isNumber(Type t){
      if (t == null) {
         throw new TypeCheckException("Type is null");
      }

      return (t instanceof INT || t instanceof POINTER);
   }


   // Rule 7: checks that an initializer for an array with empty brackets
   // has a consistent nested shape and can match some concrete dimensions.
   private boolean matchesListSize(Type declared, Type actual) {
      //check to make sure both types exist
      if (declared == null || actual == null) return false;

      //check if both sides are lists, if so go through each element and compare them
      if (declared instanceof LIST && actual instanceof LIST) {
         //cast them to list (need this to use typelist)
         LIST d = (LIST) declared;
         LIST a = (LIST) actual;
         //then check the size of each list (make sure they have the same number of elements)
         if (d.typelist.size() != a.typelist.size()) {
            return false;
         }

         //if list size matches, use for loop to recursively call function and check for each element
         for (int i = 0; i < d.typelist.size(); i++) {
            if (!matchesListSize(d.typelist.get(i), a.typelist.get(i))) {
               return false;
            }
         }
         return true;
      }

      // if the declared is an array and the actual is a list
      // check that the initializer can match some concrete array dimensions.
      if (declared instanceof ARRAY && actual instanceof LIST) {
         //cast them to array and list
         ARRAY arr = (ARRAY) declared;
         LIST list = (LIST) actual;

         //go through every element to see if it matches the array's inner type
         for (Type elem : list.typelist) {
            if (!matchesListSize(arr.type, elem)) {
               return false;
            }
         }

         //checks if there is more than one element
         if (list.typelist.size() > 1) {
            //if so, gets the first element to use as a reference to make sure the other elements match that shape
            Type first = list.typelist.get(0);
            for (int i = 1; i < list.typelist.size(); i++) {
               if (!matchesListSize(first, list.typelist.get(i))) {
                  return false;
               }
            }
         }

         return true;
      }

      return declared.canAccept(actual);
   }


   // Compute the Typecheck.Types.Type of an expression.
   private Type typeOf(Absyn.Exp e) {
      if (e instanceof Absyn.EmptyExp) return null;
      if (e instanceof Absyn.DecLit)   return new INT();
      if (e instanceof Absyn.StrLit)   return new STRING();
      if (e instanceof Absyn.ExpList) {
         ArrayList<Type> types = new ArrayList<>();
         for (Absyn.Exp sub : ((Absyn.ExpList) e).list) {
            Type subType = typeOf(sub);
            if (subType == null) {
               throw new TypeCheckException("Could not determine list element type");
            }
            types.add(subType);
         }
         return new LIST(types);
      }
      // Rule 12: Any type T used in the program must be a valid type in the Scope
      if (e instanceof Absyn.ID) {
         String name = ((Absyn.ID) e).value;
         if (currentscope.hasVar(name)) {
            return currentscope.getVar(name).type;
         }
         throw new TypeCheckException("Variable '" + name + "' not found in scope");
      }

      // Rule 6: unions can be assigned any value that type checks with one of its members
      if (e instanceof Absyn.AssignExp){
         Absyn.AssignExp a = (Absyn.AssignExp) e;
         Type leftT = typeOf(a.left);
         Type rightT = typeOf(a.right);

         if (leftT == null || rightT == null){
            throw new TypeCheckException("Couldn't determine assignment types.");
         }
         if (!leftT.canAccept(rightT)) {
            throw new TypeCheckException("Invalid assignment");
         }
         return leftT;
      }

      // Rule 8: math operation can only accept numbers
      // checks both right and left factors to see what type they are
      // returns new number if both sides are numeric values
      // returns TypeCheckException if left or right is not a number
      if(e instanceof Absyn.BinOp){
         Absyn.BinOp b = (Absyn.BinOp) e;
         Type leftT = typeOf(b.left);
         Type rightT = typeOf(b.right);

         if (!isNumber(leftT) || !isNumber(rightT)) {
            throw new TypeCheckException("Binary operator '" + b.oper + "' requires both factors to be a numeric value.");
         }
         return new INT();
      }

      //Rule 14 Unary operations all take numbers and return numbers except:
      //    * takes a pointer, *p evaluates to a underlying type of the pointer.
      //    & takes any term and evaluates to a pointer of that type
      if(e instanceof Absyn.UnaryExp){
         Absyn.UnaryExp u = (Absyn.UnaryExp) e;
         Type expType = typeOf(u.exp);

         if(u.prefix.equals("*")) {
            if (!(expType instanceof POINTER)) {
               throw new TypeCheckException("Unary operator '" + u.prefix + "' requires a pointer value.");
            }
            return ((POINTER)(expType)).type;
         }

         if (u.prefix.equals("&")){
            return new POINTER(expType);
         }

         if(!isNumber(expType)){
            throw new TypeCheckException("Unary operator '" + u.prefix + "' requires a numeric value.");
         }

         return expType;
      }

      //Rule 9: function application must match parameter type and evaluate to expression of the return type
      if (e instanceof Absyn.FunExp){
         Absyn.FunExp f = (Absyn.FunExp) e;
         //check if the function name is an identifier
         if (!(f.name instanceof Absyn.ID)){
            throw new TypeCheckException("Function name must be an identifier.");
         }
         //isolate the function name and check if a function with that name exists
         String fname = ((Absyn.ID) f.name).value;
         if(!currentscope.hasFun(fname)){
            throw new TypeCheckException("Function '" + fname + "' not found");
         }
         //get the function symbol (stores the parameters types and return type)
         FunSymbol fs = currentscope.getFun(fname);
         //loop through to figure out all arguments
         ArrayList<Type> arTypes = new ArrayList<>();
         for (Absyn.Exp ar : f.params.list) {
            Type t = typeOf(ar);
            if (t == null) {
               throw new TypeCheckException(
                       "Could not determine type of argument for '" + fname + "'"
               );
            }
            arTypes.add(t);
         }

         //check the list of actual arguments vs the list of expected arguments to make sure they match
         LIST actuals = new LIST(arTypes);
         LIST formals = fs.params;
         Type retType = fs.returnType;

         if (!formals.canAccept(actuals)) {
            throw new TypeCheckException(
                    "Function call arguments don't match the parameters for '" + fname + "'"
            );
         }

         return retType;
      }

      // Other expression types will be handled when implementing later rules
      return null;
   }


   // Rule 1: Numbers and strings are different values
   // Rule 2: Any number can be assigned to a pointer of any type (pointers count as numbers)
   // Rule 3: Arrays must be initialized with a list of the correct length
   // Rule 4: Structs must be initialized with a list matching their members
   @Override
   public Void visitVarDecl(Absyn.VarDecl node) {

      Type declaredType = node.type.typeAnnotation;
      if (declaredType == null) {
         throw new TypeCheckException("Var " + node.name + " has null type");
      }

      Absyn.Exp init = node.init;

      // No initializer — nothing to check for Rules 1-4
      // (parser may represent "no init" as EmptyExp or a 0-element ExpList)
      if (init instanceof Absyn.EmptyExp) {
         return null;
      }
      if (init instanceof Absyn.ExpList && ((Absyn.ExpList) init).list.isEmpty()) {
         return null;
      }

      Type initType = typeOf(init);
      if (initType == null) {
         throw new TypeCheckException(
                 "Could not determine initializer type for '" + node.name + "'"
         );
      }

      // Rule 5: unions cannot be initialized with a list
      // unions are initialized with a single value that type-checks as one of its members
      if (declaredType instanceof OR && init instanceof Absyn.ExpList){
         throw new TypeCheckException(
                 "Union '" + node.name +"' cannot be initialized with a list. Must be initialized with a single value."
         );
      }

      if (initType instanceof LIST) {
         if (!matchesListSize(declaredType, initType)) {
            throw new TypeCheckException(
                    "List initializer does not match declared type for '" + node.name + "'"
            );
         }
         return null;
      }


      // The canAccept methods encode Rules 1-4:
      //   Rule 1: INT.canAccept(STRING) == false, STRING.canAccept(INT) == false
      //   Rule 2: POINTER.canAccept(INT) == true, INT.canAccept(POINTER) == true
      //   Rule 3: LIST.canAccept(LIST) checks size + element types
      //   Rule 4: struct LIST.canAccept(init LIST) checks member types
      if (!declaredType.canAccept(initType)) {
         throw new TypeCheckException(
                 "Type mismatch in declaration of '" + node.name + "': " +
                         "cannot assign " + initType + " to " + declaredType
         );
      }

      return null;
   }

   @Override
   public Void visitAssignExp(Absyn.AssignExp node) {
      typeOf(node);
      return null;
   }

   @Override
   public Void visitFunDecl(Absyn.FunDecl node)
   {
      System.out.println("JUDGEMENT_PASS visitFunDecl\n   " + node.name);
      switchScope(node,()->{
         // Rule 11:
         if (this.currentscope.hasVar(node.name))
         {
            throw new TypeCheckException("Tried to define fun ("+node.name+") but var with same name already exists");
         }

         visit(node.params);
         visit(node.body);

      });
      return null;
   }

   @Override
   public Void visitWhileStmt(WhileStmt node)
   {
      System.out.println("JUDGEMENT_PASS visitIfStmt\n IF");

      switchScope(node,()->{
         // RULE 13:
         if(!isNumber(typeOf(node.expression))) {
            throw new TypeCheckException("Invalid Expression Type when WHILE expects INT");
         }

         visit(node.expression);
         visit(node.statement);
      });
      return null;
   }


   @Override
   public Void visitIfStmt(IfStmt node)
   {
      System.out.println("JUDGEMENT_PASS visitIfStmt\n IF");

      switchScope(node, ()->{
         // RULE 13:
         if(!isNumber(typeOf(node.expression))) {
            throw new TypeCheckException("Invalid Expression Type when IF expects INT");
         }

         visit(node.if_statement);
         visit(node.else_statement);
         visit(node.expression);
      });
      return null;
   }

}
