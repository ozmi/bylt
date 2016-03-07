package bylt.web

import akka.actor.ActorSystem
import bylt.core.JsonProtocol._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling._
import spray.routing.{ExceptionHandler, SimpleRoutingApp}

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


    startServer (interface = "localhost", port = 8888) {
        path ("lib.json") {
            get {
                complete {
                    marshal (bylt.core.lib.module)
                }
            }
        } ~ {
            getFromDirectory ("web")
        }
    }

}
