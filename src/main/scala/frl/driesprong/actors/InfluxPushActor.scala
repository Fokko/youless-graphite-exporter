package frl.driesprong.actors

import akka.actor.{Actor, ActorLogging}
import com.paulgoldbaum.influxdbclient.{InfluxDB, Point, WriteException}
import frl.driesprong.Config
import frl.driesprong.messages.YoulessMessage

class InfluxPushActor extends Actor with ActorLogging {

  import scala.concurrent.ExecutionContext.Implicits.global

  private lazy val influxdb: InfluxDB = InfluxDB.connect(Config.influxHost, 8086)
  private lazy val database = influxdb.selectDatabase(Config.influxDatabase)

  override def receive: Receive = {
    case message: YoulessMessage =>
      val point = Point("youless", message.tm * 1000L * 1000L * 1000L)
        .addField("total_kwh", message.net)
        .addField("current_watt", message.pwr)
        .addField("production_low_tariff", message.p1)
        .addField("production_high_tariff", message.p2)
        .addField("consumption_low_tariff", message.n1)
        .addField("consumption_high_tariff", message.n2)
        .addField("gas_m3", message.gas)
      database.write(point).recover {
        case e: Throwable => log.error(e, "Could not write the point")
      }

    case _ => unhandled()
  }
}
