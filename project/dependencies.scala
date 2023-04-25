import sbt._
import Keys._


object dependencies {
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.5.0"
  val akkaHttpSpray = "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.0"
  val akkaHttpCore = "com.typesafe.akka" %% "akka-http-core" % "10.5.0"
  val akkaActorTyped = "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0"
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.8.0"
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.8.0"

  val slf4jNop = "org.slf4j" % "slf4j-nop" % "2.0.5"
}