package bylt.core

sealed abstract class Expr
    case class Var (name : Name) extends Expr
    case class Const (value : QName) extends Expr
    case class Apply (fun : Expr, args : Vector [Expr]) extends Expr
    case class Lambda (args : Vector [Name], body : Expr) extends Expr

