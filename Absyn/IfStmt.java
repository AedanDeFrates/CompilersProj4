package Absyn;
public class IfStmt extends Stmt{
  public Exp expression;
  public Stmt if_statement;
  public Stmt else_statement;
  public IfStmt(int p, Exp e, Stmt if_s, Stmt else_s) {
    pos=p; 
    expression = e; 
    if_statement = if_s; 
    else_statement = else_s;
  }

   public String print(int depth) {
      return "  ".repeat(depth) +"IfStmt(\n" +
               "  ".repeat(depth+1) + "Check: \n" +
               this.expression.print(depth+2) + ",\n" +
               "  ".repeat(depth+1) + "Then: \n" +
               this.if_statement.print(depth+2) + ",\n" +
               "  ".repeat(depth+1) + "Else: \n"+
               this.else_statement.print(depth+2) + "\n" +
               "  ".repeat(depth) +")";
   }





	public <T> T accept(Typecheck.Pass.Visitor<T> v) {
		return v.visitIfStmt(this);
	}
}
