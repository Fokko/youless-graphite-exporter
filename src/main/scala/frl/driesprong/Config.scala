package frl.driesprong

import com.typesafe.config.ConfigFactory

object Config {
  private val conf = ConfigFactory.load();

  val youless: String = conf.getString("youless");

  private val influx = conf.getConfig("influx")
  val influxHost: String = influx.getString("host");
  val influxDatabase: String = influx.getString("database");
}
