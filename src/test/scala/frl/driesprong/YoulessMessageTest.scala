/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package frl.driesprong;

import com.fasterxml.jackson.core.JsonParseException
import frl.driesprong.youless.YoulessMessage
import org.scalatest._

class YoulessMessageTest extends FlatSpec with Matchers {

  "A json representation of the message" should "be parsed" in {
    val json =
      """[
        |  {
        |    "tm": 1565032601,
        |    "net": 15.782,
        |    "pwr": 310,
        |    "ts0": 1564663800,
        |    "cs0": 0,
        |    "ps0": 0,
        |    "p1": 26.323,
        |    "p2": 6.911,
        |    "n1": 9.651,
        |    "n2": 7.801,
        |    "gas": 818.527,
        |    "gts": 1908052115
        |  }
        |]
        |""".stripMargin

    val message = YoulessMessage.parseMessage(json)

    message.tm should be(1565032601)
    message.net should be(15.782)
    message.pwr should be(310)
    message.p1 should be(26.323)
    message.p2 should be(6.911)
    message.n1 should be(9.651)
    message.n2 should be(7.801)
    message.gas should be(818.527)
  }

  it should "throw an exception if we can't parse the data" in {
    val json = """Invalid json..."""

    a [JsonParseException] should be thrownBy {
      YoulessMessage.parseMessage(json)
    }
  }
}