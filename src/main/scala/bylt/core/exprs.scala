package bylt.core

sealed abstract class Expr
    case class Var (name : Name) extends Expr
    sealed trait Const extends Expr
        case class Ref (qname : QName) extends Const
        case class Literal (value : Value) extends Const
    case class Apply (fun : Expr, args : Vector [Expr]) extends Expr
    case class Lambda (args : Vector [Name], body : Expr) extends Expr

sealed trait Value
case class NumberValue (value : BigDecimal) extends Value
case class StringValue (value : String) extends Value
case class BooleanValue (value : Boolean) extends Value
