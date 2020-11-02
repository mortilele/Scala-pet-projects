package week8.PingPong

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import org.slf4j.{Logger, LoggerFactory}

import scala.util.{Failure, Success}


object HTTPServerRouting {
  implicit val log: Logger = LoggerFactory.getLogger(getClass)

  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    import system.executionContext

    val futureBinding = Http().newServerAt("localhost", 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
//
//  def main(args: Array[String]): Unit = {
//
//    val route = {
//      path("ping") {
//        get {
//          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "pong"))
//        }
//      }
//    }
//
//    val rootBehavior = Behaviors.setup[Nothing] { context =>
//      startHttpServer(route)(context.system)
//      Behaviors.empty
//    }
//
//    val system = ActorSystem[Nothing](rootBehavior, "PingPongHttpServer")
//  }
}