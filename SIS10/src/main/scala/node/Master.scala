package node

import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.remote.transport.ActorTransportAdapter.AskTimeout

import scala.util.{Failure, Success}

object Master {

  sealed trait Command

  final case class InitWork(text: String, replyTo: ActorRef[Node.Command]) extends Command

  final case class WorkerSleep(replyTo: ActorRef[Node.Command]) extends Command

  final case class ManageWork(text: String, workers: Set[ActorRef[Worker.Command]], replyTo: ActorRef[Command]) extends Command

  final case object ExceptionLog extends Command

  final case class WorkerFinished(count: Map[String, Int]) extends Command

  val serviceKey: ServiceKey[Command] = ServiceKey[Command]("master-service-key")


  def apply(): Behavior[Command] = {
    changeMasterState(Sleep())
  }

  def changeMasterState(state: MasterFSM): Behavior[Command] = {
    Behaviors.setup[Command] { context =>
      context.system.receptionist ! Receptionist.Register(serviceKey, context.self)

      Behaviors.receiveMessage { message =>
        state match {
          case Sleep(node) =>
            message match {
              case InitWork(text, replyTo) => {
                println(text)
                context.ask[Receptionist.Command, Receptionist.Listing](context.system.receptionist, Receptionist.Find(Worker.serviceKey, _)) {
                  case Success(value) =>
                    val workers = value.serviceInstances(Worker.serviceKey)
                    if (workers.isEmpty)
                      WorkerSleep(replyTo)
                    else {
                      ManageWork(text, workers, context.self)
                    }
                  case Failure(exception) =>
                    context.log.error("Huli failure?", exception)
                    replyTo ! Node.NodeSleep
                    ExceptionLog
                }
                changeMasterState(Sleep(replyTo))
              }
              case WorkerSleep(replyTo) => {
                replyTo ! Node.NodeSleep
                Behaviors.same
              }
              case ManageWork(text, workers, replyTo) => {
                val distributedText = text.grouped(text.size / 2)
                workers.foreach(worker => {
                  worker ! Worker.DoWork(distributedText.next, replyTo)
                })
                changeMasterState(state = Waiting(Seq()))
              }
            }
          case Waiting(intermediateResult, workerFinished) => {
            if (workerFinished == 2)
              changeMasterState(FinishJob)
            else {
              message match {
                case WorkerFinished(count) => {
                  changeMasterState(Waiting(intermediateResult :+ count, workerFinished + 1))
                }
              }
            }
          }
          case FinishJob => {
            val resultat = state.intermediateResult.foldLeft(Map[String, Int]()) { (element, acc) =>
              element.map {
                case (word, count) =>
                  acc.get(word).map(accCount => (word -> (accCount + count))).getOrElse(word -> count)
              } ++ (acc -- element.keys)
            }
            println(resultat)
            state.replyTo ! Node.Result(resultat)
            changeMasterState(Sleep(null))
          }
        }
      }
    }
  }

}
