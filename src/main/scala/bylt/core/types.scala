package bylt.core

sealed abstract class Type

    case class TopType () extends Type
    case class BottomType () extends Type
    case class UnitType (value : QName) extends Type

    sealed trait ProductType extends Type
        case class TupleType (elems : Vector [Type]) extends ProductType
        case class RecordType (fields : Vector [(Name, Type)]) extends ProductType

    case class TaggedUnionType (cases : Map [Name, Type]) extends Type

    case class OptionType (elem : Type) extends Type
    case class ManyType (elem : Type) extends Type

    case class RestrictedType (base : Type, predicate : Expr) extends Type
