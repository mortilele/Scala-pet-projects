name := "SIS10"

version := "0.1"

scalaVersion := "2.13.4"

val akkaVersion = "2.6.10"
lazy val akkaHttpVersion = "10.2.1"
val circeVersion = "0.13.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed"           % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-typed"         % akkaVersion,

  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,

  "ch.qos.logback"    %  "logback-classic"             % "1.2.3",
  "com.typesafe.akka" %% "akka-multi-node-testkit"    % akkaVersion % Test,
  "org.scalatest"     %% "scalatest"                  % "3.0.8"     % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed"   % akkaVersion % Test,

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,


  "de.heikoseeberger" %% "akka-http-circe" % "1.31.0"
)
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
enablePlugins(AshScriptPlugin)
//
//mainClass in Compile := Some("kz.kbtu.Boot")
//dockerBaseImage := "java:8-jre-alpine"
//version in Docker := "latest"
//dockerExposedPorts := Seq(8000)
//dockerRepository := Some("Alik")