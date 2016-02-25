package bylt.core

import org.scalatest.FunSuite
import org.scalatest.prop.Checkers
import org.scalacheck.Prop._
import spray.json._
import JsonProtocol._

class JsonProtocolTest extends FunSuite  with Checkers {

    implicit override val generatorDrivenConfig =
        PropertyCheckConfig (minSize = 0, maxSize = 40, workers = 4)

    implicit val arbExpr = Arbitrary.expr

    test ("Expr to and from json are equivalent") {
        check { (expr: Expr) =>
            val jsonAst = expr.toJson
            val expr2 = jsonAst.convertTo [Expr]
            expr == expr2
        }
    }

    implicit val arbType = Arbitrary.tpe

    test ("Type to and from json are equivalent") {
        check { (tpe: Type) =>
            val jsonAst = tpe.toJson
            val tpe2 = jsonAst.convertTo [Type]
            tpe == tpe2
        }
    }

}
