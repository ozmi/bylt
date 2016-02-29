package bylt.core

case class Module (
    modules : Map [Name, Module],
    types : Map [Name, Type],
    exprs : Map [Name, Expr])