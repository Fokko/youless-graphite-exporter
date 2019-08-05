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
      graphite.send("youless.watt_current", message.pwr.toString, timestamp)
      graphite.close()

      // For debugging
      println(message)
  }
}
