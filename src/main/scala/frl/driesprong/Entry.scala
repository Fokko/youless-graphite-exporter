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

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives.{complete, get}
import akka.stream.ActorMaterializer
import frl.driesprong.actors.{InfluxPushActor, YoulessPollActor}

import scala.concurrent.duration._
import scala.io.StdIn

object Entry extends App {
  private val MaxLagInSeconds: Int = 22

  private implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  private implicit val system: ActorSystem = akka.actor.ActorSystem("system")
  private implicit val materializer: ActorMaterializer = ActorMaterializer()

  val youlessActor: ActorRef = system.actorOf(Props(classOf[YoulessPollActor]))
  val influxActor: ActorRef = system.actorOf(Props(classOf[InfluxPushActor]))

  system.scheduler.schedule(Duration.Zero, 1 seconds, youlessActor: ActorRef, "vo")

  val route =
    get {
      val lag = (System.currentTimeMillis / 1000) - YoulessPollActor.lastSeenTimestamp
      val msg = s"Last message $lag seconds ago"
      println(msg)

      if (lag <= MaxLagInSeconds) {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, msg))
      } else {
        complete(HttpResponse(InternalServerError, entity = msg))
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8000)

  println(s"Press RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
