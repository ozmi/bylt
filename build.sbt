name := "bylt"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")

// --- Dependencies ---
resolvers ++= Seq (
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo" at "http://repo.spray.io"
)

libraryDependencies ++= Seq (
    "com.googlecode.kiama"      %% "kiama"              % "1.8.0",
    "io.spray"                  %% "spray-json"         % "1.3.2",
    "com.typesafe.akka"         %% "akka-actor"         % "2.3.9",
    "io.spray"                  %% "spray-can"          % "1.3.3",
    "io.spray"                  %% "spray-routing"      % "1.3.3",
    "org.scalatest"             %% "scalatest"          % "2.2.0"       % "test",
    "org.scalacheck"            %% "scalacheck"         % "1.12.2"      % "test"
)
