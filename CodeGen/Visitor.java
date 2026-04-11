package CodeGen;

public interface Visitor<T> {
    T visitVar(Var instr);
    T visitLiteral(Literal instr);
    T visitBinOp(BinOp instr);
    T visitUnaryOp(UnaryOp instr);
    T visitCall(Call instr);
    T visitArrayLoad(ArrayLoad instr);
    T visitAssign(Assign instr);
    T visitArrayStore(ArrayStore instr);
    T visitArrayAlloc(ArrayAlloc instr);
    T visitIfStmt(IfStmt instr);
    T visitGoto(Goto instr);
    T visitLabel(Label instr);
    T visitReturnStmt(ReturnStmt instr);
    T visitPrintf(Printf instr);
    T visitGOTO(GOTO instr);


	default T visit(GOTO node) {
		return node.accept(this);
	}

}
