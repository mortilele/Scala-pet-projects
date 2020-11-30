package node

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors

object Worker {

  sealed trait Command

  final case class DoWork(text: String, replyTo: ActorRef[Master.Command]) extends Command

  val serviceKey: ServiceKey[Command] = ServiceKey[Command]("worker-service-key")

  def apply(): Behavior[Command] = {
    Behaviors.setup[Command] { context =>
      context.system.receptionist ! Receptionist.Register(serviceKey, context.self)

      Behaviors.receiveMessage {
        case DoWork(text: String, replyTo) => {
          val intermediateResult = text.split(" ").groupMapReduce(identity)(v => 1)(_ + _)
          replyTo ! Master.WorkerFinished(intermediateResult)
          Behaviors.same
        }
      }
    }
  }
}
