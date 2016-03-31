package bylt.core

import spray.json._

object JsonProtocol extends DefaultJsonProtocol {

    implicit object NameFormat extends RootJsonFormat[Name] {
        def write (name : Name) : JsValue =
            JsString (name.toString)
        def read (value : JsValue) : Name =
            value match {
                case JsString (name) =>
                    Name.fromString (name)
                case _ =>
                    deserializationError ("Name expected")
            }
    }

    implicit object QNameFormat extends RootJsonFormat[QName] {
        def write (qname : QName) : JsValue =
            JsString (qname.toString)
        def read (value : JsValue) : QName =
            value match {
                case JsString (qname) =>
                    QName.fromString (qname)
                case _ =>
                    deserializationError ("QName expected")
            }
    }

    implicit object ExprFormat extends RootJsonFormat[Expr] {
        def write (expr : Expr) : JsValue =
            expr match {
                case Var (name) =>
                    JsObject ("Var" -> JsArray (Vector (name.toJson)))
                case Const (value) =>
                    JsObject ("Const" -> JsArray (Vector (value.toJson)))
                case Apply (fun, args) =>
                    JsObject ("Apply" -> JsArray (Vector (fun.toJson, args.toJson)))
                case Lambda (args, body) =>
                    JsObject ("Lambda" -> JsArray (Vector (args.toJson, body.toJson)))
            }
        def read (value : JsValue) : Expr =
            value match {
                case JsObject (fields) if fields contains "Var" =>
                    val JsArray (Vector (name)) = fields ("Var")
                    Var (name.convertTo [Name])
                case JsObject (fields) if fields contains "Const" =>
                    val JsArray (Vector (qname)) = fields ("Const")
                    Const (qname.convertTo [QName])
                case JsObject (fields) if fields contains "Apply" =>
                    val JsArray (Vector (fun, JsArray (args))) = fields ("Apply")
                    Apply (fun.convertTo [Expr], args map {_.convertTo [Expr]})
                case JsObject (fields) if fields contains "Lambda" =>
                    val JsArray (Vector (JsArray (args), body)) = fields ("Lambda")
                    Lambda (args map {_.convertTo [Name]}, body.convertTo [Expr])
            }
    }

    implicit object TypeFormat extends RootJsonFormat[Type] {
        def write (tpe : Type) : JsValue =
            tpe match {
                case TypeRef (qname) =>
                    JsObject ("TypeRef" -> JsArray (Vector (qname.toJson)))
                case TopType () =>
                    JsObject ("TopType" -> JsArray ())
                case BottomType () =>
                    JsObject ("BottomType" -> JsArray ())
                case UnitType () =>
                    JsObject ("UnitType" -> JsArray ())
                case LambdaType (arg, ret) =>
                    JsObject ("LambdaType" -> JsArray (Vector (arg.toJson, ret.toJson)))
                case TupleType (elems) =>
                    JsObject ("TupleType" -> JsArray (Vector (elems.toJson)))
                case RecordType (fields) =>
                    val jsFields = JsArray (fields map {case (name, value) => JsArray (Vector (name.toJson, value.toJson))})
                    JsObject ("RecordType" -> JsArray (Vector (jsFields)))
                case SumType (members) =>
                    JsObject ("SumType" -> JsArray (Vector (members.toJson)))
                case OptionType (elem) =>
                    JsObject ("OptionType" -> JsArray (Vector (elem.toJson)))
                case ManyType (elem, sequential, unique) =>
                    JsObject ("ManyType" -> JsArray (Vector (elem.toJson, sequential.toJson, unique.toJson)))
                case RestrictedType (base, predicate) =>
                    JsObject ("RestrictedType" -> JsArray (Vector (base.toJson, predicate.toJson)))
                case StructuralType (fields) =>
                    val jsFields = JsArray (fields map {case (name, value) => JsArray (Vector (name.toJson, value.toJson))})
                    JsObject ("StructuralType" -> JsArray (Vector (jsFields)))
                case TemporalType (elem) =>
                    JsObject ("TemporalType" -> JsArray (Vector (elem.toJson)))
            }
        def read (value : JsValue) : Type =
            value match {
                case JsObject (fields) if fields contains "TypeRef" =>
                    val JsArray (Vector (qname)) = fields ("TypeRef")
                    TypeRef (qname.convertTo [QName])
                case JsObject (fields) if fields contains "TopType" =>
                    val JsArray (Vector ()) = fields ("TopType")
                    TopType ()
                case JsObject (fields) if fields contains "BottomType" =>
                    val JsArray (Vector ()) = fields ("BottomType")
                    BottomType ()
                case JsObject (fields) if fields contains "UnitType" =>
                    val JsArray (Vector ()) = fields ("UnitType")
                    UnitType ()
                case JsObject (fields) if fields contains "LambdaType" =>
                    val JsArray (Vector (jsArg, jsRet)) = fields ("LambdaType")
                    LambdaType (jsArg.convertTo [Type], jsRet.convertTo [Type])
                case JsObject (fields) if fields contains "TupleType" =>
                    val JsArray (Vector (JsArray (tupleFields))) = fields ("TupleType")
                    TupleType (tupleFields map {_.convertTo [Type]})
                case JsObject (fields) if fields contains "RecordType" =>
                    val JsArray (Vector (JsArray (jsFields))) = fields ("RecordType")
                    RecordType (jsFields map {case JsArray (Vector (name, tpe)) => (name.convertTo [Name], tpe.convertTo [Type])})
                case JsObject (fields) if fields contains "SumType" =>
                    val JsArray (Vector (JsArray (unionMembers))) = fields ("SumType")
                    SumType (unionMembers map {_.convertTo [QName]})
                case JsObject (fields) if fields contains "OptionType" =>
                    val JsArray (Vector (elem)) = fields ("OptionType")
                    OptionType (elem.convertTo [Type])
                case JsObject (fields) if fields contains "ManyType" =>
                    val JsArray (Vector (elem, JsBoolean (sequential), JsBoolean (unique))) = fields ("ManyType")
                    ManyType (elem.convertTo [Type], sequential, unique)
                case JsObject (fields) if fields contains "RestrictedType" =>
                    val JsArray (Vector (base, predicate)) = fields ("RestrictedType")
                    RestrictedType (base.convertTo [Type], predicate.convertTo [Expr])
                case JsObject (fields) if fields contains "StructuralType" =>
                    val JsArray (Vector (JsArray (jsFields))) = fields ("StructuralType")
                    StructuralType (jsFields map {case JsArray (Vector (name, tpe)) => (name.convertTo [Name], tpe.convertTo [Type])})
                case JsObject (fields) if fields contains "TemporalType" =>
                    val JsArray (Vector (elem)) = fields ("TemporalType")
                    TemporalType (elem.convertTo [Type])
            }
    }

    implicit object DeclarationFormat extends RootJsonFormat[Declaration] {
        def write (decl : Declaration) : JsValue =
            JsObject (
                "type"  -> decl.tpe.map {_.toJson}.getOrElse (JsNull),
                "value" -> decl.value.map {_.toJson}.getOrElse (JsNull)
            )
        def read (value : JsValue) : Declaration = {
            val Seq (jsType, jsValue) =
                value.asJsObject.getFields("type", "value")
            Declaration (
                tpe   = if (jsType == JsNull) None else Some (jsType.convertTo [Type]),
                value = if (jsValue == JsNull) None else Some (jsValue.convertTo [Expr])
            )
        }
    }

    implicit object ModuleFormat extends RootJsonFormat[Module] {
        def write (module : Module) : JsValue =
            JsObject (
                "name"    -> module.name.toJson,
                "modules" -> JsObject (module.modules map {case (name, mod) => (name.toString, mod.toJson)}),
                "decls"   -> JsObject (module.decls map {case (name, decl) => (name.toString, decl.toJson)})
            )
        def read (value : JsValue) : Module = {
            val Seq (jsName, JsObject (jsModules), JsObject (jsDecls)) =
                value.asJsObject.getFields("name", "modules", "decls")
            Module (
                name    = jsName.convertTo [Name],
                modules = jsModules map {case (name, jsModule) => (Name.fromString(name), jsModule.convertTo [Module])},
                decls   = jsDecls map {case (name, jsDecl) => (Name.fromString(name), jsDecl.convertTo [Declaration])}
            )
        }
    }

}