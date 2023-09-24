ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"
enablePlugins(ScalaJSPlugin)

lazy val root = (project in file("."))
  .settings(
    name := "dummies",
    idePackagePrefix := Some("dummies"),
    scalaJSUseMainModuleInitializer := true
)
  .settings(dependencies)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    "dev.zio" %% "zio" % "2.0.12",
    "dev.zio" %% "zio-json" % "0.5.0",
    "dev.zio" %% "zio-http" % "3.0.0-RC2",

    "org.postgresql" % "postgresql" % "42.2.8",
    "io.getquill" %% "quill-jdbc-zio" % "4.6.1",

    "dev.zio"       %% "zio-logging"% "2.1.13",
    "dev.zio" %% "zio-logging-slf4j2" % "2.1.14",
    "ch.qos.logback" % "logback-classic"   % "1.4.7",

    "com.h2database" % "h2" % "2.1.214",
    )
  )
