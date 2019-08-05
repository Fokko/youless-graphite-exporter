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

