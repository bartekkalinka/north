name := "north-auctions"

version := "0.0.1"

scalaVersion := "2.12.1"

libraryDependencies ++= {
  val akkaV       = "2.4.17"
  val akkaHttpV   = "10.0.4"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-slf4j" %akkaV
  )
}
