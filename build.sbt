organization := "io.github.adsheada"
name := "shopping-basket"
version := "0.1.0-SNAPSHOT"
scalaVersion := "3.7.2"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.18" % Test,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
  "ch.qos.logback"              % "logback-classic" % "1.5.18",
  "ch.qos.logback" % "logback-core" % "1.5.18"
)