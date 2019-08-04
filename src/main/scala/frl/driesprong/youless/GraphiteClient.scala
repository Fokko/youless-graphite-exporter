package frl.driesprong.youless

import java.io._

import javax.net.SocketFactory

class GraphiteClient(address: String = Config.statsd, port:Int = 2003) extends Closeable {

  private lazy val socket = {
    val s = SocketFactory.getDefault.createSocket(address, port)
    s.setKeepAlive(true)
    s
  }

  private lazy val writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream))

  def send(name: String, value: String, timestamp: Long): Unit = {
    val line = name + ' ' + value + ' ' + timestamp.toString + '\n'
    println(line)
    writer.write(line)
  }

  /** Closes underlying connection. */
  def close() {
    writer.flush()
    try socket.close() finally writer.close()
  }
}