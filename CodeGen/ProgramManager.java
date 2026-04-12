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
                .collect(Collectors.joining(",\n   ", "{\n   ", "\n}"));
        sb.append(funcs);

        return sb.toString();
    }

    public String outputCProgram(){
        return emitter.emitProgram();

    }

    // Create C file fileName.C with generated C code
    // Takes in fileName with no extension
    public void writeCProgram(String fileName){
        try(FileWriter fw = new FileWriter(fileName+".c",false)){
            fw.write(outputCProgram());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Compiles C file to executable, calls gcc on command line
    public void compileCProgram(String fileName){
        try {

            ProcessBuilder pb = new ProcessBuilder("gcc", fileName + ".c", "-o", fileName);
            Process process = pb.start();
            process.waitFor();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}