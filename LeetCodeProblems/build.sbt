name := "LeetCodeProblems"

version := "0.1"

scalaVersion := "2.13.3"

val circeVersion = "0.12.3"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

val AkkaVersion = "2.6.10"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion