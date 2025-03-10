package week9.AddressBook

import scala.concurrent.Future
import scala.util.{Success, Failure}

import akka.http.scaladsl.server.{Directive1, Directives}

trait ContactDirectives extends Directives {

  def handle[T](f: Future[T])
               (e: Throwable => ApiError): Directive1[T] = onComplete(f) flatMap {
    case Success(t) => provide(t)
    case Failure(error) =>
      val apiError = e(error)
      complete(apiError.statusCode, apiError.message)
  }

  def handleWithGeneric[T](f: Future[T]): Directive1[T] =
    handle[T](f)(_ => ApiError.generic)
}