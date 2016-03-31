package bylt.web

import akka.actor.ActorSystem
import bylt.core.JsonProtocol._
import bylt.core._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling._
import spray.routing.{ExceptionHandler, SimpleRoutingApp}

import scala.util.Properties
import scala.util.control.NonFatal

object Server extends App with SimpleRoutingApp {

    implicit val system = ActorSystem ("simple-routing-system")

    implicit val exceptionHandler =
        ExceptionHandler {
            case NonFatal (e) =>
                requestUri { uri =>
                    complete(InternalServerError, e.getMessage)
                }
        }


    startServer (interface = "0.0.0.0", port = Properties.envOrElse("PORT", "8888").toInt) {
        path ("lib.json") {
            get {
                complete {
                    val m1 = Map (lib.module.name -> lib.module)
                    val m2 = Module.fromDirectory (new java.io.File ("./repos"), Name.fromString ("")).modules
                    val rootModule = Module (Name.fromString (""), Module.mergeModuleMaps (m1, m2), Map.empty)
                    marshal (rootModule)
                }
            }
        } ~ {
            getFromDirectory ("web")
        }
    }

}
