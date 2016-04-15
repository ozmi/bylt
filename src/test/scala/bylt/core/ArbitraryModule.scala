package bylt.core

import org.scalacheck.Gen

/**
  * Created by attila on 4/14/2016.
  */
object ArbitraryModule {

    def declGen (depth : Int) : Gen [Declaration] = {
        for {
            tpe <- Gen.option (ArbitraryType.typeExprGen (depth))
            value <- Gen.option (ArbitraryExpr.exprGen (depth))
        } yield Declaration (tpe, value)
    }

    def moduleGen (depth : Int) : Gen [Module] = {
        val moduleFieldGen =
            for {
                name <- ArbitraryName.nameGen
                module <- moduleGen (depth - 1)
            } yield (name, module)

        val declFieldGen =
            for {
                name <- ArbitraryName.nameGen
                decl <- declGen (depth - 1)
            } yield (name, decl)

        if (depth > 1) {
            for {
                name <- ArbitraryName.nameGen
                modules <- Gen.listOf (moduleFieldGen)
                decls <- Gen.listOf (declFieldGen)
            } yield Module (name, modules.toMap, decls.toMap)
        } else {
            for {
                name <- ArbitraryName.nameGen
                decls <- Gen.listOf (declFieldGen)
            } yield Module (name, Map.empty, decls.toMap)
        }
    }

    def moduleGen  : Gen [Module] = Gen.sized { size =>
        moduleGen (size / 20)
    }

    lazy val arb = org.scalacheck.Arbitrary (moduleGen)

}
