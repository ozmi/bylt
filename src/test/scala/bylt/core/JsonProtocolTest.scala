package bylt.core

import bylt.core.JsonProtocol._
import org.scalacheck.Prop._
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import spray.json._

class JsonProtocolTest extends FunSuite  with Checkers {

    implicit override val generatorDrivenConfig =
        PropertyCheckConfig (minSize = 0, maxSize = 40, workers = 4)

    implicit val arbExpr = ArbitraryExpr.arb

    test ("Expr to and from json are equivalent") {
        check { (expr: Expr) =>
            val jsonAst = expr.toJson
            val expr2 = jsonAst.convertTo [Expr]
            expr == expr2
        }
    }

    implicit val arbType = ArbitraryType.arb

    test ("Type to and from json are equivalent") {
        check { (tpe: TypeExpr) =>
            val jsonAst = tpe.toJson
            val tpe2 = jsonAst.convertTo [TypeExpr]
            tpe == tpe2
        }
    }

    implicit val arbModule = ArbitraryModule.arb

    test ("Module to and from json are equivalent") {
        check { (module : Module) =>
            val jsonAst = module.toJson
            val module2 = jsonAst.convertTo [Module]
            module == module2
        }
    }

}
