package bylt.core

import spray.json._

object JsonProtocol extends DefaultJsonProtocol {

    implicit object NameFormat extends RootJsonFormat[Name] {
        def write (name : Name) =
            JsString (name.toString)
        def read (value : JsValue) =
            value match {
                case JsString (name) =>
                    Name.fromString (name)
                case _ =>
                    deserializationError ("Name expected")
            }
    }

    implicit object QNameFormat extends RootJsonFormat[QName] {
        def write (qname : QName) =
            JsString (qname.toString)
        def read (value : JsValue) =
            value match {
                case JsString (qname) =>
                    QName.fromString (qname)
                case _ =>
                    deserializationError ("QName expected")
            }
    }

    implicit object ExprFormat extends RootJsonFormat[Expr] {
        def write (expr : Expr) =
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
        def read (value : JsValue) =
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
        def write (tpe : Type) =
            tpe match {
                case TypeRef (qname) =>
                    JsObject ("TypeRef" -> JsArray (Vector (qname.toJson)))
                case TopType () =>
                    JsObject ("TopType" -> JsArray ())
                case BottomType () =>
                    JsObject ("BottomType" -> JsArray ())
                case UnitType (value) =>
                    JsObject ("UnitType" -> JsArray (Vector (value.toJson)))
                case TupleType (elems) =>
                    JsObject ("TupleType" -> JsArray (Vector (elems.toJson)))
                case RecordType (fields) =>
                    val jsFields = JsArray (fields map {case (name, value) => JsArray (Vector (name.toJson, value.toJson))})
                    JsObject ("RecordType" -> JsArray (Vector (jsFields)))
                case TaggedUnionType (cases) =>
                    val map = JsObject (cases.toSeq map {case (key, value) => key.toString -> value.toJson} : _*)
                    JsObject ("TaggedUnionType" -> JsArray (Vector (map)))
                case OptionType (elem) =>
                    JsObject ("OptionType" -> JsArray (Vector (elem.toJson)))
                case ManyType (elem, sequential, unique) =>
                    JsObject ("ManyType" -> JsArray (Vector (elem.toJson, sequential.toJson, unique.toJson)))
                case RestrictedType (base, predicate) =>
                    JsObject ("RestrictedType" -> JsArray (Vector (base.toJson, predicate.toJson)))
                case StructuralType (fields) =>
                    val jsFields = JsArray (fields map {case (name, value) => JsArray (Vector (name.toJson, value.toJson))})
                    JsObject ("StructuralType" -> JsArray (Vector (jsFields)))
            }
        def read (value : JsValue) =
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
                    val JsArray (Vector (qname)) = fields ("UnitType")
                    UnitType (qname.convertTo [QName])
                case JsObject (fields) if fields contains "TupleType" =>
                    val JsArray (Vector (JsArray (tupleFields))) = fields ("TupleType")
                    TupleType (tupleFields map {_.convertTo [Type]})
                case JsObject (fields) if fields contains "RecordType" =>
                    val JsArray (Vector (JsArray (jsFields))) = fields ("RecordType")
                    RecordType (jsFields map {case JsArray (Vector (name, tpe)) => (name.convertTo [Name], tpe.convertTo [Type])})
                case JsObject (fields) if fields contains "TaggedUnionType" =>
                    val JsArray (Vector (JsObject (jsFields))) = fields ("TaggedUnionType")
                    TaggedUnionType (jsFields map {case (name, tpe) => (Name.fromString (name), tpe.convertTo [Type])})
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
            }
    }

}