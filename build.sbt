name := "zio-example"

version := "0.1"

scalaVersion := "2.13.1"

val http4sVersion     = "0.21.0-M6"
val circeVersion      = "0.12.1"
val scalatestVersion  = "3.1.0"
val scalacheckVersion = "1.14.2"
val logbackVersion    = "1.2.3"
val zioVersion        = "1.0.0-RC17"
val zioCatsVersion    = "2.0.0.0-RC10"

libraryDependencies ++= List(
  "org.http4s"     %% "http4s-blaze-server" % http4sVersion,
  "org.http4s"     %% "http4s-blaze-client" % http4sVersion,
  "org.http4s"     %% "http4s-circe"        % http4sVersion,
  "org.http4s"     %% "http4s-dsl"          % http4sVersion,
  "io.circe"       %% "circe-generic"       % circeVersion,
  "ch.qos.logback" % "logback-classic"      % logbackVersion,
  "dev.zio"        %% "zio"                 % zioVersion,
  "dev.zio"        %% "zio-interop-cats"    % zioCatsVersion,
  "org.scalatest"  %% "scalatest"           % scalatestVersion % Test,
  "org.scalacheck" %% "scalacheck"          % scalacheckVersion % Test
)
