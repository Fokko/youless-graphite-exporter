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

package frl.driesprong

import akka.actor.Props
import frl.driesprong.youless.YoulessPollActor

import scala.concurrent.duration._
import scala.language.postfixOps

object Entry extends App {
  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val system = akka.actor.ActorSystem("system")

  val tickActor = system.actorOf(Props(classOf[YoulessPollActor]))
  val cancellable = system.scheduler.schedule(0 seconds, 1 seconds, tickActor, None)

  System.out.println("Polling...")
}
