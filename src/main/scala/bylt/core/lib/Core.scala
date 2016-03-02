package bylt.core
package lib

object Core extends ModuleDecl ('lib / 'core) {

    // names
    val Name = typeDecl ('name) {
        RecordType (Vector ('parts -> ManyType (Text.CharSeq, sequential = true)))
    }

    val QName = typeDecl ('qname) {
        RecordType (Vector ('ns -> ManyType (Name, sequential = true), 'name -> Name))
    }

    // exprs
    val Const = typeDecl ('const) {
        RecordType (Vector ('value -> QName))
    }
    val Var = typeDecl ('var) {
        RecordType (Vector ('name -> Name))
    }
    val Apply = typeDecl ('apply) {
        RecordType (Vector ('fun -> Expr, 'args -> ManyType (Expr)))
    }
    val Lambda = typeDecl ('lambda) {
        RecordType (Vector ('args -> ManyType (Expr), 'body -> Expr))
    }
    lazy val Expr : TypeRef = typeDecl ('expr) {
        TaggedUnionType.fromRefs (Const, Var, Apply, Lambda)
    }

    // types
    val _TypeRef = typeDecl ('type_ref) {
        RecordType (Vector ('qname -> QName))
    }
    val _TopType = unitType ('top_type)
    val _BottomType = unitType ('bottom_type)
    val _UnitType = typeDecl ('unit_type) {
        RecordType (Vector ('value -> QName))
    }

    val _LambdaType = typeDecl ('lambda_type) {
        RecordType (Vector ('arg -> typeRef ('type), 'ret -> _Type))
    }

    val _TupleType = typeDecl ('tuple_type) {
        RecordType (Vector ('elems -> ManyType (_Type, sequential = true)))
    }
    val _RecordType = typeDecl ('record_type) {
        RecordType (Vector ('fields -> ManyType (TupleType (Vector (Name, _Type)), sequential = true)))
    }
    val _ProductType = typeDecl ('product_type) {
        TaggedUnionType.fromRefs (_TupleType, _RecordType)
    }

    // TODO: use map for cases
    val _TaggedUnionType = typeDecl ('tagged_union_type) {
        RecordType (Vector ('cases -> ManyType (TupleType (Vector (Name, _Type)))))
    }

    val _OptionType = typeDecl ('option_type) {
        RecordType (Vector ('elem -> typeRef ('type)))
    }
    val _ManyType = typeDecl ('many_type) {
        RecordType (Vector ('elem -> _Type, 'sequential -> Boolean.Boolean, 'unique -> Boolean.Boolean))
    }

    val _RestrictedType = typeDecl ('restricted_type) {
        RecordType (Vector ('base -> _Type, 'predicate -> Expr))
    }

    val _StructuralType = typeDecl ('structural_type) {
        RecordType (Vector ('fields -> ManyType (TupleType (Vector (Name, _Type)), sequential = true)))
    }

    val _TemporalType = typeDecl ('temporal_type) {
        RecordType (Vector ('elem -> _Type))
    }

    lazy val _Type : TypeRef = typeDecl ('type) {
        TaggedUnionType.fromRefs (
            _TypeRef, _TopType, _BottomType, _UnitType, _LambdaType,
            _ProductType, _TaggedUnionType, _OptionType, _ManyType,
            _RestrictedType, _StructuralType, _TemporalType)
    }

}
