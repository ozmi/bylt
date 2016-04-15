package bylt.core

sealed abstract class TypeExpr {
    def -> (returnType : TypeExpr) : LambdaType =
        LambdaType (this, returnType)
}
case class TypeRef (qname : QName) extends TypeExpr
case class TypeVar (name : Name) extends TypeExpr
case class TypeApply (constructor : QName, args : Vector [TypeExpr]) extends TypeExpr
case class TypeLambda (args : Vector [Name], body : TypeExpr) extends TypeExpr

sealed abstract class TypeDecl extends TypeExpr

    /** This is the type that includes all the possible values in the universe. Its member function is constant true.
      */
    case class TopType () extends TypeDecl

    /** This is the type that doesn't have any members. Its member function is constant false.
      */
    case class BottomType () extends TypeDecl

    /** This is a type that has exactly one member. There are potentially as many instances of this type as many values
      * there are in the universe but practically only certain values are represented as a unit type.
      */
    case class UnitType () extends TypeDecl

    case class LabeledType () extends TypeDecl

    sealed trait ProductType extends TypeDecl
        case class TupleType (elems : Vector [TypeExpr]) extends ProductType
        case class RecordType (fields : Vector [(Name, TypeExpr)]) extends ProductType

    case class SumType (members : Vector [(Name, TypeExpr)]) extends TypeDecl

    case class LambdaType (from : TypeExpr, to : TypeExpr) extends TypeDecl

    case class TypeClass (constraints : Vector [TypeApply], args : Vector [Name], members : Vector [(Name, TypeExpr)])

object Examples {

    val Eq = TypeClass (
        constraints = Vector.empty,
        args = Vector ('a),
        members = Vector (
            'eq -> (TypeVar ('a) -> TypeVar ('a) -> TypeRef ('lib / 'boolean)),
            'ne -> (TypeVar ('a) -> TypeVar ('a) -> TypeRef ('lib / 'boolean))
        )
    )

    val Ord = TypeClass (
        constraints = Vector (TypeApply ('example / 'eq, Vector (TypeVar ('a)))),
        args = Vector ('a),
        members = Vector (
            'lt -> (TypeVar ('a) -> TypeVar ('a) -> TypeRef ('lib / 'boolean)),
            'le -> (TypeVar ('a) -> TypeVar ('a) -> TypeRef ('lib / 'boolean)),
            'gt -> (TypeVar ('a) -> TypeVar ('a) -> TypeRef ('lib / 'boolean)),
            'ge -> (TypeVar ('a) -> TypeVar ('a) -> TypeRef ('lib / 'boolean))
        )
    )

    val Option = TypeLambda (
        args = Vector ('elem),
        body = SumType (Vector (
            'some -> TypeVar ('elem),
            'none -> BottomType ()
        ))
    )

    val Int = LabeledType ()

    val BagOfInt = TypeApply ('lib / 'bag, Vector (Int))

}