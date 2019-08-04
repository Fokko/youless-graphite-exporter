package frl.driesprong

import akka.actor.Props
import frl.driesprong.youless.YoulessPollActor

import scala.concurrent.duration._
import scala.language.postfixOps

object Entry extends App {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val system = akka.actor.ActorSystem("system")

  val tickActor = system.actorOf(Props(classOf[YoulessPollActor]))
  val cancellable = system.scheduler.schedule(0 seconds, 1 seconds, tickActor, None)

  System.out.println("Polling...")
}
