package Typecheck.Pass;
import Typecheck.TypeCheckException;
import Absyn.*;

public class Pass<T> implements Visitor<T> {
   public T result;
	private T defaultReturn;

	@Override
	public T visitAbsyn(Absyn node) {
		return defaultReturn;
	}
	@Override
	public T visitArrayExp(ArrayExp node) {
      visit(node.name);
      visit(node.index_list);
		return defaultReturn;
	}
	@Override
	public T visitArrayType(ArrayType node) {
      visit(node.size);
		return defaultReturn;
	}
	@Override
	public T visitAssignExp(AssignExp node) {
      visit(node.left);
      visit(node.right);
		return defaultReturn;
	}
	@Override
	public T visitBinOp(BinOp node) {
      visit(node.left);
      visit(node.right);
		return defaultReturn;
	}
	@Override
	public T visitBreakStmt(BreakStmt node) {
		return defaultReturn;
	}
	@Override
	public T visitCompStmt(CompStmt node) {
      visit(node.decl_list);
      visit(node.stmt_list);
		return defaultReturn;
	}
	@Override
	public T visitDecLit(DecLit node) {
		return defaultReturn;
	}
	@Override
	public T visitDecl(Decl node) {
		return defaultReturn;
	}
	@Override
	public T visitDeclList(DeclList node) {
      for (Decl d : node.list) {
         visit(d);
		}
      	return defaultReturn;
	}
	@Override
	public T visitEmptyExp(EmptyExp node) {
		return defaultReturn;
	}
	@Override
	public T visitEmptyStmt(EmptyStmt node) {
		return defaultReturn;
	}
	@Override
	public T visitExp(Exp node) {
		return defaultReturn;
	}
	@Override
	public T visitExpList(ExpList node) {
      for (Exp e : node.list) {
         visit(e);
		}
      	return defaultReturn;
	}
	@Override
	public T visitExprStmt(ExprStmt node) {
      visit(node.expression);
		return defaultReturn;
	}
	@Override
	public T visitFunDecl(FunDecl node) {
      visit(node.type);
      visit(node.params);
      visit(node.body);
		return defaultReturn;
	}
	@Override
	public T visitFunExp(FunExp node) {
      visit(node.name);
      visit(node.params);
		return defaultReturn;
	}
	@Override
	public T visitID(ID node) {
		return defaultReturn;
	}
	@Override
	public T visitIfStmt(IfStmt node) {
      visit(node.expression);
      visit(node.if_statement);
      visit(node.else_statement);
		return defaultReturn;
	}
	@Override
	public T visitLiteral(Literal node) {
		return defaultReturn;
	}
	@Override
	public T visitParameter(Parameter node) {
      visit(node.type);
		return defaultReturn;
	}
	@Override
	public T visitReturnStmt(ReturnStmt node) {
      visit(node.expression);
		return defaultReturn;
	}
	@Override
	public T visitStmt(Stmt node) {
		return defaultReturn;
	}
	@Override
	public T visitStmtList(StmtList node) {
      for (Stmt d : node.list) {
         visit(d);
		}
      	return defaultReturn;
	}
	@Override
	public T visitStrLit(StrLit node) {
      return defaultReturn;
	}
	@Override
	public T visitStructDecl(StructDecl node) {
      visit(node.body);
		return defaultReturn;
	}
	@Override
	public T visitStructMember(StructMember node) {
      visit(node.type);
		return defaultReturn;
	}
	@Override
	public T visitTypedef(Typedef node) {
      visit(node.type);
		return defaultReturn;
	}
	@Override
	public T visitType(Type node) {
      visit(node.brackets);
		return defaultReturn;
	}
	@Override
	public T visitUnaryExp(UnaryExp node) {
      visit(node.exp);
		return defaultReturn;
	}
	@Override
	public T visitUnionDecl(UnionDecl node) {
      visit(node.body);
		return defaultReturn;
	}
	@Override
	public T visitUnionMember(UnionMember node) {
      visit(node.type);
		return defaultReturn;
	}
	@Override
	public T visitVarDecl(VarDecl node) {
      visit(node.type);
      visit(node.init);
		return defaultReturn;
	}
	@Override
	public T visitWhileStmt(WhileStmt node) {
      visit(node.expression);
      visit(node.statement);
		return defaultReturn;
	}
}

