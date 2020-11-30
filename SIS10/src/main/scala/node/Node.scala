package node

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.{Behaviors, Routers}
import akka.actor.typed.{ActorRef, Behavior}

object Node extends App {
  sealed trait Command

  final case class StartJob(text: String, replyTo: ActorRef[Command]) extends Command
  final case object NodeSleep extends Command
  final case class Result(count: Map[String, Int]) extends Command

  def apply(): Behavior[Command] = {
    Behaviors.setup[Command] { context =>
      implicit val system = context.system

      implicit val scheduler = context.system.scheduler
      val group = Routers.group(Master.serviceKey).withRoundRobinRouting()
      val master = context.spawnAnonymous(group)
      val serviceKey: ServiceKey[Command] = ServiceKey[Command]("node-service-key")
      context.system.receptionist ! Receptionist.Register(serviceKey, context.self)

      Behaviors.receiveMessage { message =>
        message match {
          case StartJob(text, replyTo) => {
            master ! Master.InitWork("text1 text 1 text2 text1 text3 text1", replyTo)
            Behaviors.same
          }
          case Result(count) => {
            println(count)
            Behaviors.same
          }
          case NodeSleep => {
            Behaviors.same
          }
        }
      }
    }
  }
}
