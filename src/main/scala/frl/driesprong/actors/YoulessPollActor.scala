package frl.driesprong.actors

import java.net.NoRouteToHostException

import akka.actor.{Actor, ActorLogging}
import com.fasterxml.jackson.core.JsonParseException
import frl.driesprong.messages.YoulessMessage
import frl.driesprong.{Config, Entry}

import scala.io.Source

object YoulessPollActor {
  var lastSeenTimestamp: Long = System.currentTimeMillis / 1000
}

class YoulessPollActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case _ => try {
      // convert to seconds
      val json = Source.fromURL(s"http://${Config.youless}/e?f=j")
      val jsonPayload = json.mkString

      log.info("Got: " + jsonPayload)

      val message = YoulessMessage.parseMessage(jsonPayload)

      YoulessPollActor.lastSeenTimestamp = message.tm

      // Send the parsed message to the sink
      Entry.influxActor ! message
    } catch {
      case e: NoRouteToHostException => log.warning("Could not connect: " + e)
      case e: JsonParseException => log.warning("Could not parse the payload: " + e)
      case e: Throwable => log.warning("Unknown exception: " + e)
    }
  }
}
