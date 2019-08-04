package frl.driesprong.youless

import org.json4s.JsonAST.{JDouble, JString}
import org.json4s.{CustomSerializer, DefaultFormats, Formats}
import org.json4s.jackson.JsonMethods.parse

case class YoulessMessage(cnt: Double, pwr: Int) {
  override def toString: String = s"sCurrent consumption ${pwr}, total ${cnt}"
}

object YoulessMessage {
  object StringToDouble extends CustomSerializer[Double](format => ( {
    // In the EU we use a comma instead of a dot
    case JString(x) => x.replace(',', '.').toDouble
  }, {
    case x: Double => JDouble(x)
  }))

  implicit val formats: Formats = DefaultFormats + StringToDouble

  def parseMessage(json: String): YoulessMessage =
    parse(json.mkString).extract[YoulessMessage]
}

