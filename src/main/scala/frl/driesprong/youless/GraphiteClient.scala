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

import java.io._

import javax.net.SocketFactory

class GraphiteClient(address: String = Config.statsd, port: Int = 2003) extends Closeable {

  private lazy val socket = {
    val s = SocketFactory.getDefault.createSocket(address, port)
    s.setKeepAlive(true)
    s
  }

  private lazy val writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream))

  def send(name: String, value: String, timestamp: Long): Unit =
    writer.write(name + ' ' + value + ' ' + timestamp.toString + '\n')

  def close() {
    writer.flush()
    try socket.close() finally writer.close()
  }
}