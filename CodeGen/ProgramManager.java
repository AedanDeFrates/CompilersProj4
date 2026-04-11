package CodeGen;
import java.util.ArrayList;
import java.util.HashMap;


// This class creates and manages the Program and Emitter objects
// Since the Program class is only accessible from the CodeGen package
public class ProgramManager {

    public Program program;
    public Emitter.ProgramEmitter emitter;
    public HashMap<String,String> varNameTranslator;

    public ProgramManager(){
        this.program = new Program();
        this.varNameTranslator  = new HashMap<>();
        this.emitter = new Emitter.ProgramEmitter(program.globals,program.funcs);
    }

    public void printProgram(){
        String programText = emitter.emitProgram();
        System.out.println(programText);
    }

}