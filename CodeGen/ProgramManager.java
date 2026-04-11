package CodeGen;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.stream.Collectors;


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

    public String outputGOTOProgram(){
        StringBuilder sb = new StringBuilder();

        sb.append("Globals ");
        String globals = program.globals.stream()
                .map(var -> "var " + var.type + " " + var.name)
                .collect(Collectors.joining(",\n   ", "{\n   ", "\n}\n"));
        sb.append(globals);

        sb.append("Funcs ");
        String funcs = program.funcs.stream()
                .map(func -> "fun " + func.returntype + " " + func.name)
                .collect(Collectors.joining(",\n   ", "{\n   ", "\n}\n"));
        sb.append(funcs);

        return sb.toString();
    }

    public String outputCProgram(){
        return emitter.emitProgram();

    }


    // Create C file FileName.C with generated C code
    // Takes in FileName.g
    public void writeCProgram(String geauxFileName){
        String fileName = geauxFileName.replaceFirst("[.][^.]+$", "") + ".c";

        try(FileWriter fw = new FileWriter(fileName,false)){
            fw.write(outputCProgram());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}