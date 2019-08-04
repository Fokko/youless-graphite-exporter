name := "youless-statsd-exporter"

version := "0.1"

scalaVersion := "2.13.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.23",
  "org.json4s" %% "json4s-jackson" % "3.6.7",
  "com.typesafe" % "config" % "1.3.4",

  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

