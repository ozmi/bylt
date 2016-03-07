package bylt.core

case class Module (
    name : Name,
    modules : Map [Name, Module],
    types : Map [Name, Type],
    exprs : Map [Name, Expr])

object Module {

    def root =
        Module (
            name = Name.fromString (""),
            modules = Map.empty,
            types = Map.empty,
            exprs = Map.empty
        )

    def mergeModules (parent : Module, aModule : Module, bModule : Module) : Module = {
        if (aModule.name == bModule.name) {
            Module (
                name = aModule.name,
                modules = mergeModuleMaps (aModule.modules, bModule.modules),
                types = aModule.types ++ bModule.types,
                exprs = aModule.exprs ++ bModule.exprs
            )
        } else {
            val childModules =
                Map (
                    aModule.name -> aModule,
                    bModule.name -> bModule
                )
            parent.copy (
                modules = mergeModuleMaps (parent.modules, childModules)
            )
        }
    }

    def mergeModuleMaps (a : Map [Name, Module], b : Map [Name, Module]) : Map [Name, Module] = {
        val moduleMapping =
            for (moduleName <- a.keys ++ b.keys)
                yield (a get moduleName, b get moduleName) match {
                    case (Some (aModule), Some (bModule)) =>
                        moduleName -> Module (
                            name = moduleName,
                            modules = mergeModuleMaps (aModule.modules, bModule.modules),
                            types = aModule.types ++ bModule.types,
                            exprs = aModule.exprs ++ bModule.exprs
                        )
                    case (Some (aModule), None) =>
                        moduleName -> aModule
                    case (None, Some (bModule)) =>
                        moduleName -> bModule
                    case (None, None) =>
                        sys.error ("This should never happen")
                }
        moduleMapping.toMap
    }
}

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

    def asModule : Module = {
        def wrapIfNeeded (path : Seq [Name], module : Module) : Module =
            if (path.isEmpty) {
                module
            } else {
                val wrapper =
                    Module (
                        name = path.last,
                        modules = Map (module.name -> module),
                        types = Map.empty,
                        exprs = Map.empty
                    )
                wrapIfNeeded (path dropRight 1, wrapper)
            }

        val leafModule =
            Module (
                name = this.qname.name,
                modules = Map.empty,
                types = this._typeDecls mapValues {t => t ()},
                exprs = Map.empty)

        wrapIfNeeded (this.qname.ns, leafModule)
    }

}
