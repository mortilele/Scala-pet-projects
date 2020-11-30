package node

import akka.actor.typed.ActorRef

sealed trait MasterFSM {
  val replyTo: ActorRef[Node.Command] = null
  val workerFinished: Int = 0
  val intermediateResult: Seq[Map[String, Int]] = Seq()
}

final case class Sleep(override val replyTo: ActorRef[Node.Command] = null) extends MasterFSM
final case class Waiting(override val intermediateResult: Seq[Map[String, Int]], override val workerFinished: Int = 0) extends  MasterFSM
final case object FinishJob extends MasterFSM
