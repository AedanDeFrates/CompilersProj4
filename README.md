

# ***CompilersProj4 - Code Generation***
4/30/2026

Written by Joshua Walther

[GitHub Repo Link](https://github.com/AedanDeFrates/CompilersProj4)

### Group Members

- Aedan DeFrates
- Alex Hawk
- Lorenzo Jackson
- Joshua Walther

## Theory
For this project we are implementing the code generation of our compiler, taking the annotated, type-checked AST from the semantic analysis and lowering it to an intermediate representation closer to C, before emitting the corresponding C code, and compiling it with GCC. At this point the compiler works fully, taking a `fileName.g` Geaux file, performing lexical, syntactic, and semantic analysis, transpiling it C, and compiling it into an executable.

### What is Code Generation?
Code Generation is the process of translating the intermediate representation of our program into instructions for the machine to run. To make this easier, we can avoid writing our own compiler backend and optimization by instead generating C code from our program and compiling that with an existing compiler like GCC. Our code generation then will happen in three steps, lowering, emitting, and compiling.

1. Lowering - lowers our current intermediate representation, an AST, to one closer to actual C code.

2. Emitting -  takes our lowered IR and uses it to emit a corresponding C program file.

3. Compiling - leverages an existing C compiler like GCC to compile our code to an executable.


## How to run our project
To run our the code, use the provided shell script and any program file.          
`./run.sh <optional_file_name.g>`

### PREQUISITE: GCC
Our shell script will compile the emitted C code file automatically using GCC. This will not function if GCC is not installed

## Code Implementation
We will again make use of the visitor pattern, and implement passes on the AST to create our lower level IR. Then we will use a pass on the IR to emit C code.

### Lowering
This is the step that we are implementing. Our lower level IR is GOTO.  A GOTO Program has a list of global variables and functions. It consists only of fourteen prewritten instructions classes.

Instructions:
1. Var
2. Literal
3. BinOp
4. UnaryOp
5. Assign
6. Return
7. Call
8. IfStmt
9. Goto
10. Label
11. ArrayLoad
12. ArrayAllocation
13. ArrayStore
14. Builtin

In this IR, all variable declarations will be global, Control flow is implemented with goto statements and labels, and the only types are int, string, and int array.

We created a ProgramManager class to store our GOTO program object and methods using the program emitter.

We decided to use three passes to generate our IR program. Each of these passes extends a common abstract CodeGenPass which extends ScopePass from last project and holds a reference to the ProgramManager so that the passes can access the program.


#### GlobalVariablePass
Visits all variables, parameters, struct/union members and adds them as global Vars. Since variables from different scopes can share the same name, we rename all of the Vars to unique names using a counter when assigned globally. We also assign the Var one of the three types based on what GEAUX type it has.

#### CreateFuncPass
This pass visits all function declarations and creates the corresponding GOTO Functions and adds them to the GOTO program. Each of these functions holds a list of GOTO instructions that will be added in the next pass

#### InstructionsPass
This pass visits the rest of our AST nodes to add instructions to their corresponding functions. It stores a reference to the current Function. When visiting a function it switches the current function, visits its children, and restores the previous function.

Each visit method returns its resulting GOTO object, if one is created, so that they can be retrieved by their parent node. Only statements, those ending in semicolons, need to be added as instructions to the functions, but these statements may be made up of multiple GOTO instructions. An assign statement may be made of multiple binary operations for example.



## Group Strategy and Issues Encountered

### Group Strategy
Since this project utilized the same skills as the last one and given that this project is more open ended than the last one, this project was not nearly as difficult.

We decided to split our implementation among three passes. The var and fun passes could probably be combined, and the instruction pass could probably be split, but we found that it organized the code nicely in to separate logical steps.

We also made more of an attempt to stay organized instead of using one large method for most of the implementation.

A big improvement over the last project was leveraging the return type of the visit methods to pass objects up to parent visit functions. In the last project the JudgementPass was of type Void, where it would have been beneficial if it was of type Type so that the type of child nodes could be accessed by parents. Instead we create a typeOf method that recursively determined the type of expressions based on their children's type. This made using the visit methods kind of pointless since these helper functions


### Issues Encountered
One of the big issues with this projects was the ir.java and the GOTO classes. Since ir.java is not a class file, but a file full of classes, they all have default visibility meaning they can only be accessed from the CodeGen package. It also made it hard to import them, especially when working with AST nodes since there are a lot of duplicate class names across the AST, TypeCheck, and GOTO classes.

We created a ProgramManager class as a way to access the program object from main and to hold any methods to print out, write, or compile our code using the emitter.