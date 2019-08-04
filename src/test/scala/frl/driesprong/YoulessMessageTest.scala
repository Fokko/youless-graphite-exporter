package frl.driesprong;

import com.fasterxml.jackson.core.JsonParseException
import frl.driesprong.youless.YoulessMessage
import org.scalatest._

class YoulessMessageTest extends FlatSpec with Matchers {

  "A json representation of the message" should "be parsed" in {
    val json = """{"cnt":" 17,951","pwr":138,"lvl":0,"dev":"","det":"","con":"","sts":"","cs0":" 0,000","ps0":0,"raw":0}"""

    val message = YoulessMessage.parseMessage(json)

    message.cnt should be(17.951)
    message.pwr should be(138)
  }

  it should "throw an exception if we can't parse the data" in {
    val json = """Invalid json..."""

    a [JsonParseException] should be thrownBy {
      YoulessMessage.parseMessage(json)
    }
  }
}