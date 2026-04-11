package Typecheck.Pass;
import Parse.*;
import Absyn.*;

public interface Visitor<T> {
	T visitAbsyn(Absyn node) ;

	T visitArrayExp(ArrayExp node) ;

	T visitArrayType(ArrayType node) ;

	T visitAssignExp(AssignExp node) ;

	T visitBinOp(BinOp node) ;

	T visitBreakStmt(BreakStmt node) ;

	T visitCompStmt(CompStmt node) ;

	T visitDecLit(DecLit node) ;

	T visitDecl(Decl node) ;

	T visitDeclList(DeclList node) ;

	T visitEmptyExp(EmptyExp node) ;

	T visitEmptyStmt(EmptyStmt node) ;

	T visitExp(Exp node) ;

	T visitExpList(ExpList node) ;

	T visitExprStmt(ExprStmt node) ;

	T visitFunDecl(FunDecl node) ;

	T visitFunExp(FunExp node) ;

	T visitID(ID node) ;

	T visitIfStmt(IfStmt node) ;

	T visitLiteral(Literal node) ;

	T visitParameter(Parameter node) ;

	T visitReturnStmt(ReturnStmt node) ;

	T visitStmt(Stmt node) ;

	T visitStmtList(StmtList node) ;

	T visitStrLit(StrLit node) ;

	T visitStructDecl(StructDecl node) ;

	T visitStructMember(StructMember node) ;

	T visitTypedef(Typedef node) ;

	T visitType(Type node) ;

	T visitUnaryExp(UnaryExp node) ;

	T visitUnionDecl(UnionDecl node) ;

	T visitUnionMember(UnionMember node) ;

	T visitVarDecl(VarDecl node) ;

	T visitWhileStmt(WhileStmt node) ;

	default T visit(Absyn node) {
		return node.accept(this);
	}

}
