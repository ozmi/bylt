package bylt.core

import spray.json.{JsArray, _}

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

    implicit object ValueFormat extends RootJsonFormat[Value] {
        def write (value : Value) : JsValue =
            value match {
                case NumberValue (v) =>
                    JsNumber (v)
                case StringValue (v) =>
                    JsString (v)
                case BooleanValue (v) =>
                    JsBoolean (v)
            }
        def read (value : JsValue) : Value =
            value match {
                case JsNumber (v) =>
                    NumberValue (v)
                case JsString (v) =>
                    StringValue (v)
                case JsBoolean (v) =>
                    BooleanValue (v)
            }
    }

    implicit object ExprFormat extends RootJsonFormat[Expr] {
        def write (expr : Expr) : JsValue =
            expr match {
                case Var (name) =>
                    JsObject ("var" -> JsArray (Vector (name.toJson)))
                case Ref (value) =>
                    JsObject ("ref" -> JsArray (Vector (value.toJson)))
                case Literal (value) =>
                    JsObject ("literal" -> JsArray (Vector (value.toJson)))
                case Apply (fun, args) =>
                    JsObject ("apply" -> JsArray (Vector (fun.toJson, args.toJson)))
                case Lambda (args, body) =>
                    JsObject ("lambda" -> JsArray (Vector (args.toJson, body.toJson)))
            }
        def read (value : JsValue) : Expr =
            value match {
                case JsObject (fields) if fields contains "var" =>
                    val JsArray (Vector (name)) = fields ("var")
                    Var (name.convertTo [Name])
                case JsObject (fields) if fields contains "ref" =>
                    val JsArray (Vector (qname)) = fields ("ref")
                    Ref (qname.convertTo [QName])
                case JsObject (fields) if fields contains "literal" =>
                    val JsArray (Vector (value)) = fields ("literal")
                    Literal (value.convertTo [Value])
                case JsObject (fields) if fields contains "apply" =>
                    val JsArray (Vector (fun, JsArray (args))) = fields ("apply")
                    Apply (fun.convertTo [Expr], args map {_.convertTo [Expr]})
                case JsObject (fields) if fields contains "lambda" =>
                    val JsArray (Vector (JsArray (args), body)) = fields ("lambda")
                    Lambda (args map {_.convertTo [Name]}, body.convertTo [Expr])
            }
    }

    implicit object TypeExprFormat extends RootJsonFormat[TypeExpr] {
        def write (expr : TypeExpr) : JsValue =
            expr match {
                case TypeVar (name) =>
                    JsObject ("type_var" -> JsArray (Vector (name.toJson)))
                case TypeRef (value) =>
                    JsObject ("type_ref" -> JsArray (Vector (value.toJson)))
                case TypeApply (const, args) =>
                    JsObject ("type_apply" -> JsArray (Vector (const.toJson, args.toJson)))
                case TypeLambda (args, body) =>
                    JsObject ("type_lambda" -> JsArray (Vector (args.toJson, body.toJson)))
                case TopType () =>
                    JsObject ("top_type" -> JsArray ())
                case BottomType () =>
                    JsObject ("bottom_type" -> JsArray ())
                case UnitType () =>
                    JsObject ("unit_type" -> JsArray ())
                case LabeledType () =>
                    JsObject ("labeled_type" -> JsArray ())
                case LambdaType (arg, ret) =>
                    JsObject ("lambda_type" -> JsArray (Vector (arg.toJson, ret.toJson)))
                case TupleType (elems) =>
                    JsObject ("tuple_type" -> JsArray (Vector (elems.toJson)))
                case RecordType (fields) =>
                    val jsFields = JsArray (fields map {case (name, value) => JsArray (Vector (name.toJson, value.toJson))})
                    JsObject ("record_type" -> JsArray (Vector (jsFields)))
                case SumType (members) =>
                    val jsMembers = JsArray (members map {case (name, value) => JsArray (Vector (name.toJson, value.toJson))})
                    JsObject ("sum_type" -> JsArray (Vector (jsMembers)))
            }
        def read (value : JsValue) : TypeExpr =
            value match {
                case JsObject (fields) if fields contains "type_var" =>
                    val JsArray (Vector (name)) = fields ("type_var")
                    TypeVar (name.convertTo [Name])
                case JsObject (fields) if fields contains "type_ref" =>
                    val JsArray (Vector (qname)) = fields ("type_ref")
                    TypeRef (qname.convertTo [QName])
                case JsObject (fields) if fields contains "type_apply" =>
                    val JsArray (Vector (jsConst, JsArray (jsArgs))) = fields ("type_apply")
                    TypeApply (jsConst.convertTo [QName], jsArgs map {_.convertTo [TypeExpr]})
                case JsObject (fields) if fields contains "type_lambda" =>
                    val JsArray (Vector (JsArray (jsArgs), jsBody)) = fields ("type_lambda")
                    TypeLambda (jsArgs map {_.convertTo [Name]}, jsBody.convertTo [TypeExpr])
                case JsObject (fields) if fields contains "top_type" =>
                    val JsArray (Vector ()) = fields ("top_type")
                    TopType ()
                case JsObject (fields) if fields contains "bottom_type" =>
                    val JsArray (Vector ()) = fields ("bottom_type")
                    BottomType ()
                case JsObject (fields) if fields contains "unit_type" =>
                    val JsArray (Vector ()) = fields ("unit_type")
                    UnitType ()
                case JsObject (fields) if fields contains "labeled_type" =>
                    val JsArray (Vector ()) = fields ("labeled_type")
                    UnitType ()
                case JsObject (fields) if fields contains "lambda_type" =>
                    val JsArray (Vector (jsArg, jsRet)) = fields ("lambda_type")
                    LambdaType (jsArg.convertTo [TypeExpr], jsRet.convertTo [TypeExpr])
                case JsObject (fields) if fields contains "tuple_type" =>
                    val JsArray (Vector (JsArray (tupleFields))) = fields ("tuple_type")
                    TupleType (tupleFields map {_.convertTo [TypeExpr]})
                case JsObject (fields) if fields contains "record_type" =>
                    val JsArray (Vector (JsArray (jsFields))) = fields ("record_type")
                    RecordType (jsFields map {case JsArray (Vector (name, tpe)) => (name.convertTo [Name], tpe.convertTo [TypeExpr])})
                case JsObject (fields) if fields contains "sum_type" =>
                    val JsArray (Vector (JsArray (unionMembers))) = fields ("sum_type")
                    SumType (unionMembers map {case JsArray (Vector (name, tpe)) => (name.convertTo [Name], tpe.convertTo [TypeExpr])})
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
                tpe   = if (jsType == JsNull) None else Some (jsType.convertTo [TypeExpr]),
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