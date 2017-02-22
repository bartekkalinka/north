name := "north-auctions"

version := "0.0.1"

scalaVersion := "2.12.1"

libraryDependencies ++= {
  val akkaV       = "2.4.17"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV
  )
}
