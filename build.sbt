ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "wix-test"
  )
  .aggregate(model, engine, console)
  .dependsOn(model, engine, console)

lazy val model = project
  .settings(
    name := "model",
    settings,
    libraryDependencies ++= commonDeps
  )

lazy val engine = project
  .settings(
    name := "engine",
    settings,
    libraryDependencies ++= commonDeps ++ Seq(deps.zio)
  )
  .dependsOn(model)

lazy val console = project
  .settings(
    name := "console",
    settings,
    libraryDependencies ++= commonDeps ++ Seq(deps.zio)
  )
  .dependsOn(engine)

lazy val deps = new {
  val version = new {
    val cats = "2.10.0"
    val zio = "2.0.16"
  }

  val zio = "dev.zio" %% "zio" % version.zio
  val catsCore = "org.typelevel" %% "cats-core" % version.cats
}

lazy val commonDeps = Seq(deps.catsCore)

lazy val settings = scalafmtSettings

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true
  )