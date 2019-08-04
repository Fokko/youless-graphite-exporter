package frl.driesprong.youless

import akka.actor.Actor

import scala.io.Source

class YoulessPollActor extends Actor {
  override def receive: PartialFunction[Any, Unit] = {
    case _ =>
      // convert to seconds
      val timestamp = System.currentTimeMillis() / 1000

      val json = Source.fromURL(s"http://${Config.youless}/a?f=j")
      val message = YoulessMessage.parseMessage(json.mkString)

      val graphite = new GraphiteClient()
      graphite.send("youless.kwh_total", message.cnt.toString, timestamp)
      graphite.send("youless.kwh_current", message.cnt.toString, timestamp)
      graphite.close()

      // For debugging
      println(message)
  }
}
