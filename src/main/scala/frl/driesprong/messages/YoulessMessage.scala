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

package frl.driesprong.messages

import org.json4s.jackson.JsonMethods.parse
import org.json4s.{DefaultFormats, Formats}

/*
[{
	"tm": 1565032557,
	"net": 15.778,
	"pwr": 312,
	"ts0": 1564663800,
	"cs0": 0.000,
	"ps0": 0,
	"p1": 26.323,
	"p2": 6.907,
	"n1": 9.651,
	"n2": 7.801,
	"gas": 818.527,
	"gts": 1908052115
}]
 */

case class YoulessMessage(tm: Long, net: Double, pwr: Int, p1: Double, p2: Double, n1: Double, n2: Double, gas: Double) {
  override def toString: String =
    s"""Message ${tm}:
       |  Total: ${net} kWh
       |  Currently: ${pwr} Watt
       |  Gas: ${gas} m3
       |""".stripMargin
}

object YoulessMessage {
  implicit val formats: Formats = DefaultFormats

  def parseMessage(json: String): YoulessMessage =
    parse(json.mkString).extract[List[YoulessMessage]].head
}

