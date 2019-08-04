package frl.driesprong.youless

import com.typesafe.config.ConfigFactory

object Config {
  private val conf = ConfigFactory.load();

  val statsd: String = conf.getString("statsd");
  val youless: String = conf.getString("youless");
}
