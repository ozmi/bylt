name := "bylt"

version := "1.0"

scalaVersion := "2.11.7"

// --- Dependencies ---
resolvers ++= Seq (
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo" at "http://repo.spray.io"
)

libraryDependencies ++= Seq (
    "com.googlecode.kiama"      %% "kiama"              % "1.8.0",
    "io.spray"                  %% "spray-json"         % "1.3.2",
    "org.scalatest"             %% "scalatest"          % "2.2.0"       % "test",
    "org.scalacheck"            %% "scalacheck"         % "1.12.2"      % "test"
)
