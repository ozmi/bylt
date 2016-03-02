package bylt.core

case class Module (
    modules : Map [Name, Module],
    types : Map [Name, Type],
    exprs : Map [Name, Expr])

abstract class ModuleDecl (val qname : QName) {

    private var _typeDecls : Map [Name, () => Type] = Map.empty

    def typeRef (name : Name) : TypeRef =
        TypeRef (qname / name)

    def typeDecl (name : Name) (tpeInit : => Type) : TypeRef = {
        val fullName = qname / name
        _typeDecls = _typeDecls + (name, () => tpeInit)
        TypeRef (fullName)
    }

    def unitType (name : Name) : TypeRef =
        typeDecl (name) {
            UnitType (qname / name)
        }

    def asModule : Module =
        Module (
            modules = Map.empty,
            types = _typeDecls mapValues {t => t ()},
            exprs = Map.empty)

}
