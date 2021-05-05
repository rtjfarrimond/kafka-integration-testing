ThisBuild / scalaVersion     := "2.13.4"
ThisBuild / organization     := "io.github.rtjfarrimond"

lazy val root = (project in file("."))
  .settings(
    name := "producer",
    libraryDependencies ++= Seq(
      "com.github.fd4s" %% "fs2-kafka" % "2.0.0",
      "org.scalatest"   %% "scalatest" % "3.2.2" % Test
    )
  )
