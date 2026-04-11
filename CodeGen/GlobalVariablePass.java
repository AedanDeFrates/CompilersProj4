package CodeGen;
import Absyn.*;
import Typecheck.SymbolTable.Scope;
import Typecheck.Types.*;
import java.util.ArrayList;
import java.util.HashMap;



// This pass creates all variables in the GOTO IR as globals
// and renames all variables unique names
// from variables, parameters, struct/union members
// using Program.getUniqueVarName()
public class GlobalVariablePass extends CodeGenPass<Void>{

    public GlobalVariablePass(ProgramManager p, Scope s) {
        super(p, s);
    }

    // Maps each TypeCheck.Type to a CodeGen.Type
    private CodeGen.Type getGOTOType(Typecheck.Types.Type type){

        //I'M NOT SURE WHAT THE TYPES SHOULD BE FOR SOME OF THESE
        return switch(type){
            case Typecheck.Types.VOID t -> CodeGen.Type.INT;
            case Typecheck.Types.POINTER t -> Type.INT;
            case Typecheck.Types.INT t -> Type.INT;
            case Typecheck.Types.STRING t -> Type.STRING;
            case Typecheck.Types.LIST t-> Type.INTARRAY;
            case Typecheck.Types.ARRAY t -> Type.INTARRAY;
            case Typecheck.Types.OR t -> Type.INTARRAY;
            case Typecheck.Types.ALIAS t -> Type.INTARRAY;
            default -> null;
        };
    }

    //Create the global varialbes in the program, all with unique names
    //Adds a mappings of old var names to their new unique one
    private void createGOTOVar(String name,Typecheck.Types.Type type){

        String uniqueName = pm.program.getUniqueVarName();
        pm.varNameTranslator.put(name,uniqueName);

        CodeGen.Type GOTOtype =  getGOTOType(type);

        CodeGen.Var v = new Var(uniqueName,GOTOtype);
        pm.program.globals.add(v);
    }


    @Override
    public Void visitVarDecl(VarDecl node){

        System.out.println("GLOBAL_VARIABLE_PASS visitVarDecl\n   " + node.name);
        createGOTOVar(node.name,node.type.typeAnnotation);
        return null;
    }

    @Override
    public Void visitParameter(Parameter node){

        System.out.println("GLOBAL_VARIABLE_PASS visitParameter\n   " + node.name);
        createGOTOVar(node.name,node.type.typeAnnotation);
        return null;
    }

    @Override
    public Void visitStructMember(StructMember node){

        System.out.println("GLOBAL_VARIABLE_PASS visitStructMember\n   " + node.name);
        createGOTOVar(node.name,node.type.typeAnnotation);
        return null;
    }

    @Override
    public Void visitUnionMember(UnionMember node){

        System.out.println("GLOBAL_VARIABLE_PASS visitUnionMember\n   " + node.name);
        createGOTOVar(node.name,node.type.typeAnnotation);
        return null;
    }
}

