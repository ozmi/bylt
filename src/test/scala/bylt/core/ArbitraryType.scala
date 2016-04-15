package bylt.core

import org.scalacheck.Gen

/**
  * Created by attila on 4/14/2016.
  */
object ArbitraryType {

    // Types

    def lambdaTypeGen (depth : Int) : Gen [LambdaType] =
        for {
            arg <- typeExprGen (depth)
            ret <- typeExprGen (depth)
        } yield LambdaType (arg, ret)

    def tupleTypeGen (depth : Int) : Gen [TupleType] =
        for (elems <- Gen.nonEmptyListOf (typeExprGen (depth)))
            yield TupleType (elems.toVector)

    def recordTypeGen (depth : Int) : Gen [RecordType] = {
        val fieldGen =
            for {
                name <- ArbitraryName.nameGen
                tpe <- typeExprGen (depth)
            } yield (name, tpe)

        for (fields <- Gen.nonEmptyListOf (fieldGen))
            yield RecordType (fields.toVector)
    }

    def sumTypeGen (depth : Int) : Gen [SumType] = {
        val memberGen =
            for {
                name <- ArbitraryName.nameGen
                tpe <- typeExprGen (depth)
            } yield (name, tpe)

        for (members <- Gen.nonEmptyListOf (memberGen))
            yield SumType (members.toVector)
    }

    def typeDeclGen (depth : Int) : Gen [TypeDecl] =
        if (depth > 1) {
            Gen.frequency (
                1 -> Gen.const (TopType ()),
                1 -> Gen.const (BottomType ()),
                3 -> Gen.const (UnitType ()),
                5 -> lambdaTypeGen (depth - 1),
                2 -> tupleTypeGen (depth - 1),
                9 -> recordTypeGen (depth - 1),
                7 -> sumTypeGen (depth - 1)
            )
        } else {
            Gen.frequency (
                1 -> Gen.const (TopType ()),
                1 -> Gen.const (BottomType ()),
                3 -> Gen.const (UnitType ())
            )
        }

    lazy val typeRefGen : Gen [TypeRef] =
        for (qname <- ArbitraryName.qnameGen) yield TypeRef (qname)

    lazy val typeVarGen : Gen [TypeVar] =
        for (name <- ArbitraryName.nameGen) yield TypeVar (name)

    def typeApplyGen (depth : Int) : Gen [TypeApply] =
        for {
            qname <- ArbitraryName.qnameGen
            args <- Gen.nonEmptyListOf (typeExprGen (depth))
        } yield TypeApply (qname, args.toVector)

    def typeLambdaGen (depth : Int) : Gen [TypeLambda] =
        for {
            args <- Gen.nonEmptyListOf (ArbitraryName.nameGen)
            body <- typeExprGen (depth)
        } yield TypeLambda (args.toVector, body)

    def typeExprGen (depth : Int) : Gen [TypeExpr] =
        if (depth > 1) {
            Gen.frequency (
                1 -> typeRefGen,
                2 -> typeVarGen,
                3 -> typeApplyGen (depth - 1),
                1 -> typeLambdaGen (depth - 1),
                7 -> typeDeclGen (depth)
            )
        } else {
            Gen.frequency (
                1 -> typeRefGen,
                2 -> typeVarGen,
                7 -> typeDeclGen (depth)
            )
        }

    def typeExprGen  : Gen [TypeExpr] = Gen.sized { size =>
        typeExprGen (size / 10)
    }

    lazy val arb = org.scalacheck.Arbitrary (typeExprGen)

}
