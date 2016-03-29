package bylt.core

import org.scalacheck.Gen

object Arbitrary {

    lazy val lowerAlphaStr : Gen [String] =
        Gen.nonEmptyListOf (Gen.alphaLowerChar).map(_.mkString)

    // Names

    lazy val nameGen : Gen [Name] =
        for (parts <- Gen.nonEmptyListOf (lowerAlphaStr)) yield Name (parts.toVector)

    lazy val qnameGen : Gen [QName] =
        for {
            path <- Gen.listOf (nameGen)
            name <- nameGen
        } yield QName (path.toVector, name)

    // Exprs

    lazy val varGen : Gen [Var] =
        for (name <- nameGen) yield Var (name)

    lazy val constGen : Gen [Const] =
        for (qname <- qnameGen) yield Const (qname)

    def applyGen (depth : Int) : Gen [Apply] =
        for {
            fun <- exprGen (depth)
            args <- Gen.listOf (exprGen (depth))
        } yield Apply (fun, args.toVector)

    def lambdaGen (depth : Int) : Gen [Lambda] =
        for {
            args <- Gen.listOf (nameGen)
            body <- exprGen (depth)
        } yield Lambda (args.toVector, body)

    def exprGen (depth : Int) : Gen [Expr] =
        if (depth > 1) {
            Gen.frequency (
                7 -> varGen,
                5 -> constGen,
                3 -> applyGen (depth - 1),
                1 -> lambdaGen (depth - 1)
            )
        } else {
            Gen.frequency (
                7 -> varGen,
                5 -> constGen
            )
        }

    def exprGen  : Gen [Expr] = Gen.sized { size =>
        exprGen (size / 10)
    }

    lazy val expr = org.scalacheck.Arbitrary (exprGen)

    // Types

    lazy val typeRefGen : Gen [TypeRef] =
        for (qname <- qnameGen) yield TypeRef (qname)

    lazy val unitTypeGen : Gen [UnitType] =
        for (qname <- qnameGen) yield UnitType (qname)

    def lambdaTypeGen (depth : Int) : Gen [LambdaType] =
        for {
            arg <- typeGen (depth)
            ret <- typeGen (depth)
        } yield LambdaType (arg, ret)

    def tupleTypeGen (depth : Int) : Gen [TupleType] =
        for (elems <- Gen.nonEmptyListOf (typeGen (depth))) yield TupleType (elems.toVector)

    def recordTypeGen (depth : Int) : Gen [RecordType] = {
        val fieldGen =
            for {
                name <- nameGen
                tpe <- typeGen (depth)
            } yield (name, tpe)

        for (fields <- Gen.nonEmptyListOf (fieldGen))
            yield RecordType (fields.toVector)
    }

    def unionTypeGen (depth : Int) : Gen [UnionType] = {
        for (members <- Gen.nonEmptyListOf (typeGen (depth)))
            yield UnionType (members.toSet)
    }

    def taggedUnionTypeGen (depth : Int) : Gen [TaggedUnionType] = {
        val fieldGen =
            for {
                name <- nameGen
                tpe <- typeGen (depth)
            } yield (name, tpe)

        for (fields <- Gen.nonEmptyListOf (fieldGen))
            yield TaggedUnionType (fields.toMap)
    }

    def optionTypeGen (depth : Int) : Gen [OptionType] =
        for (elem <- typeGen (depth)) yield OptionType (elem)

    def manyTypeGen (depth : Int) : Gen [ManyType] =
        for {
            elem <- typeGen (depth)
            sequential <- Gen.oneOf (true, false)
            unique <- Gen.oneOf (true, false)
        } yield ManyType (elem, sequential, unique)

    def restrictedTypeGen (depth : Int) : Gen [RestrictedType] =
        for {
            base <- typeGen (depth)
            predicate <- exprGen (depth)
        } yield RestrictedType (base, predicate)

    def structuralTypeGen (depth : Int) : Gen [StructuralType] = {
        val fieldGen =
            for {
                name <- nameGen
                tpe <- typeGen (depth)
            } yield (name, tpe)

        for (fields <- Gen.nonEmptyListOf (fieldGen))
            yield StructuralType (fields.toVector)
    }

    def temporalTypeGen (depth : Int) : Gen [TemporalType] =
        for (elem <- typeGen (depth)) yield TemporalType (elem)

    def typeGen (depth : Int) : Gen [Type] =
        if (depth > 1) {
            Gen.frequency (
                3 -> typeRefGen,
                1 -> Gen.const (TopType ()),
                1 -> Gen.const (BottomType ()),
                3 -> unitTypeGen,
                5 -> lambdaTypeGen (depth - 1),
                2 -> tupleTypeGen (depth - 1),
                9 -> recordTypeGen (depth - 1),
                7 -> unionTypeGen (depth - 1),
                7 -> taggedUnionTypeGen (depth - 1),
                10 -> optionTypeGen (depth - 1),
                10 -> manyTypeGen (depth - 1),
                4 -> restrictedTypeGen (depth - 1),
                2 -> temporalTypeGen (depth - 1)
            )
        } else {
            Gen.frequency (
                3 -> typeRefGen,
                1 -> Gen.const (TopType ()),
                1 -> Gen.const (BottomType ()),
                3 -> unitTypeGen
            )
        }

    def typeGen  : Gen [Type] = Gen.sized { size =>
        typeGen (size / 10)
    }

    lazy val tpe = org.scalacheck.Arbitrary (typeGen)

    def moduleGen (depth : Int) : Gen [Module] = {
        val moduleFieldGen =
            for {
                name <- nameGen
                module <- moduleGen (depth - 1)
            } yield (name, module)

        val typeFieldGen =
            for {
                name <- nameGen
                tpe <- typeGen (depth - 1)
            } yield (name, tpe)

        val exprFieldGen =
            for {
                name <- nameGen
                expr <- exprGen (depth - 1)
            } yield (name, expr)

        if (depth > 1) {
            for {
                name <- nameGen
                modules <- Gen.listOf (moduleFieldGen)
                types <- Gen.listOf (typeFieldGen)
                exprs <- Gen.listOf (exprFieldGen)
            } yield Module (name, modules.toMap, types.toMap, exprs.toMap)
        } else {
            for {
                name <- nameGen
                types <- Gen.listOf (typeFieldGen)
                exprs <- Gen.listOf (exprFieldGen)
            } yield Module (name, Map.empty, types.toMap, exprs.toMap)
        }
    }

    def moduleGen  : Gen [Module] = Gen.sized { size =>
        moduleGen (size / 20)
    }

    lazy val module = org.scalacheck.Arbitrary (moduleGen)

}
