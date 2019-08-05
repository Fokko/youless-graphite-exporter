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