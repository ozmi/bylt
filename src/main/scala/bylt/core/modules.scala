package bylt.core

case class Module (
    name : Name,
    modules : Map [Name, Module],
    decls : Map [Name, Declaration])

case class Declaration (
    tpe : Option[TypeExpr],
    value : Option[Expr])

object Module {

    def allFromDirectory (rootDir : java.io.File) : Seq [Module] = {
        for (subDir <- rootDir.listFiles if subDir.isDirectory) yield {
            fromDirectory (subDir)
        }
    }

    def fromDirectory (rootDir : java.io.File) : Module = {
        import bylt.core.JsonProtocol._
        import spray.json._

        val decls =
            for (jsonFile <- rootDir.listFiles if jsonFile.isFile && (jsonFile.getName endsWith ".json")) yield {
                val declName = Name.fromString (jsonFile.getName.split ("\\.").dropRight (1).mkString)
                val decl = io.Source.fromFile (jsonFile).mkString.parseJson.convertTo [Declaration]
                declName -> decl
            }

        val modules =
            for (subDir <- rootDir.listFiles if subDir.isDirectory) yield {
                val module = fromDirectory (subDir)
                module.name -> module
            }

        Module (
            name = Name.fromString (rootDir.getName),
            modules = modules.toMap,
            decls = decls.toMap
        )
    }

    def root =
        Module (
            name = Name.fromString (""),
            modules = Map.empty,
            decls = Map.empty
        )

    def mergeModules (parent : Module, aModule : Module, bModule : Module) : Module = {
        if (aModule.name == bModule.name) {
            Module (
                name = aModule.name,
                modules = mergeModuleMaps (aModule.modules, bModule.modules),
                decls = mergeDeclarationMaps (aModule.decls, bModule.decls)
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
                            decls = mergeDeclarationMaps (aModule.decls, bModule.decls)
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

    def mergeDeclarationMaps (a : Map [Name, Declaration], b : Map [Name, Declaration]) : Map [Name, Declaration] = {
        val declMapping =
            for (declName <- a.keys ++ b.keys)
                yield (a get declName, b get declName) match {
                    case (Some (aDecl), Some (bDecl)) =>
                        declName -> Declaration (
                            tpe = aDecl.tpe orElse bDecl.tpe,
                            value = aDecl.value orElse bDecl.value
                        )
                    case (Some (aModule), None) =>
                        declName -> aModule
                    case (None, Some (bModule)) =>
                        declName -> bModule
                    case (None, None) =>
                        sys.error ("This should never happen")
                }
        declMapping.toMap
    }
}

abstract class ModuleDecl (val qname : QName) {

    private var _typeDecls : Map [Name, () => TypeExpr] = Map.empty

    def typeRef (name : Name) : TypeRef =
        typeDecl (name) {
            TypeRef (qname / name)
        }

    def typeDecl (name : Name) (tpeInit : => TypeExpr) : TypeRef = {
        val fullName = qname / name
        _typeDecls = _typeDecls + (name, () => tpeInit)
        TypeRef (fullName)
    }

    def unitType (name : Name) : TypeRef =
        typeDecl (name) {
            UnitType ()
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
                        decls = Map.empty
                    )
                wrapIfNeeded (path dropRight 1, wrapper)
            }

        val leafModule =
            Module (
                name = this.qname.name,
                modules = Map.empty,
                decls = this._typeDecls mapValues {t => Declaration (Some (t ()), None)}
            )

        wrapIfNeeded (this.qname.ns, leafModule)
    }

}
