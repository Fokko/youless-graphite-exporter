package frl.driesprong

import java.net.InetSocketAddress

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}
import frl.driesprong.actors.YoulessPollActor

object HealthProbe {

  def start() {
    val server = HttpServer.create(new InetSocketAddress(8000), 0)
    server.createContext("/", new HealthProbe())
    server.setExecutor(null)
    server.start()

    println("Hit any key to exit...")

    System.in.read()
    server.stop(0)
  }
}

class HealthProbe extends HttpHandler {

  val MaxLagInSeconds: Int = 22

  def handle(t: HttpExchange) {
    sendResponse(t)
  }

  private def sendResponse(t: HttpExchange) {
    val response = "Ack!"
    val lag = (System.currentTimeMillis / 1000) - YoulessPollActor.lastSeenTimestamp

    println(s"Last message $lag seconds ago")

    val responseCode = if (lag > MaxLagInSeconds) {
      200 // Everything looks good
    } else {
      500 // Oops, something is off :'(
    }

    t.sendResponseHeaders(responseCode, response.length())
    val os = t.getResponseBody
    os.write(response.getBytes)
    os.close()
  }

}
