ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "dummies",
    idePackagePrefix := Some("dummies")
  )
  .settings(dependencies)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    "dev.zio" %% "zio" % "2.0.12",
    "dev.zio" %% "zio-json" % "0.5.0",
    "dev.zio" %% "zio-http" % "3.0.0-RC2",

    "io.getquill" %% "quill-jdbc-zio" % "4.6.1",
    "io.getquill" %% "quill-codegen-jdbc" % "4.6.1",

    "com.h2database" % "h2" % "2.1.214",
    )
  )