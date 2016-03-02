package bylt.core

sealed abstract class Type {
    def -> (returnType : Type) : LambdaType =
        LambdaType (this, returnType)
}

    case class TypeRef (qname : QName) extends Type

    case class TopType () extends Type
    case class BottomType () extends Type
    case class UnitType (value : QName) extends Type

    case class LambdaType (arg : Type, ret : Type) extends Type

    sealed trait ProductType extends Type
        case class TupleType (elems : Vector [Type]) extends ProductType
        case class RecordType (fields : Vector [(Name, Type)]) extends ProductType

    case class TaggedUnionType (cases : Map [Name, Type]) extends Type
    object TaggedUnionType {
        def fromRefs (values : TypeRef*) : TaggedUnionType =
            TaggedUnionType ((values map {tpe => tpe.qname.name -> tpe}).toMap)
    }

    case class OptionType (elem : Type) extends Type
    case class ManyType (elem : Type, sequential : Boolean = false, unique : Boolean = false) extends Type

    case class RestrictedType (base : Type, predicate : Expr) extends Type

    case class StructuralType (fields : Vector [(Name, Type)]) extends Type

    case class TemporalType (elem : Type) extends Type