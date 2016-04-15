package bylt.core

import org.scalacheck.{Arbitrary, Gen}

object ArbitraryExpr {

    // Exprs

    lazy val valueGen : Gen [Value] = {
        val numGen =
            for (value <- Arbitrary.arbitrary [BigDecimal])
                yield NumberValue (value)
        val strGen =
            for (value <- Gen.alphaStr)
                yield StringValue (value)
        val boolGen =
            for (value <- Arbitrary.arbitrary [Boolean])
                yield BooleanValue (value)

        Gen.frequency (
            7 -> numGen,
            5 -> strGen,
            3 -> boolGen
        )
    }

    lazy val varGen : Gen [Var] =
        for (name <- ArbitraryName.nameGen) yield Var (name)

    lazy val refGen : Gen [Ref] =
        for (qname <- ArbitraryName.qnameGen) yield Ref (qname)

    lazy val literalGen : Gen [Literal] =
        for (value <- valueGen) yield Literal (value)

    def applyGen (depth : Int) : Gen [Apply] =
        for {
            fun <- exprGen (depth)
            args <- Gen.listOf (exprGen (depth))
        } yield Apply (fun, args.toVector)

    def lambdaGen (depth : Int) : Gen [Lambda] =
        for {
            args <- Gen.listOf (ArbitraryName.nameGen)
            body <- exprGen (depth)
        } yield Lambda (args.toVector, body)

    def exprGen (depth : Int) : Gen [Expr] =
        if (depth > 1) {
            Gen.frequency (
                7 -> varGen,
                5 -> refGen,
                4 -> literalGen,
                3 -> applyGen (depth - 1),
                1 -> lambdaGen (depth - 1)
            )
        } else {
            Gen.frequency (
                7 -> varGen,
                5 -> refGen,
                4 -> literalGen
            )
        }

    def exprGen  : Gen [Expr] = Gen.sized { size =>
        exprGen (size / 10)
    }

    lazy val arb = org.scalacheck.Arbitrary (exprGen)


}
