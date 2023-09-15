ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file(".")).settings(name := "wix-test")
  .aggregate(model, engine, console, indigo).dependsOn(model, engine, console, indigo)

lazy val model = project.settings(name := "model", settings, libraryDependencies ++= commonDeps)

lazy val engine = project
  .settings(name := "engine", settings, libraryDependencies ++= commonDeps ++ Seq(deps.zio))
  .dependsOn(model)

lazy val console = project
  .settings(name := "console", settings, libraryDependencies ++= commonDeps ++ Seq(deps.zio))
  .dependsOn(engine)

lazy val indigo = project.enablePlugins(ScalaJSPlugin, SbtIndigo)
  .settings(name := "indigo", settings, indigoSettingsAndDeps, libraryDependencies ++= commonDeps)

lazy val deps = new {
  val version = new {
    val cats = "2.10.0"
    val zio = "2.0.16"
    val indigo = "0.15.0-RC3"
  }

  val zio = "dev.zio" %% "zio" % version.zio

  val catsCore = "org.typelevel" %% "cats-core" % version.cats

//  val indigo = "io.indigoengine" %%% "indigo" % version.indigo
//  val indigoExtras = "io.indigoengine" %%% "indigo-extras" % version.indigo
//  val indigoJsonCirce = "io.indigoengine" %%% "indigo-json-circe" % version.indigo
}

lazy val commonDeps = Seq(deps.catsCore)

//lazy val indigoDeps = Seq(deps.indigo, deps.indigoExtras, deps.indigoJsonCirce)

lazy val settings = scalafmtSettings

lazy val scalafmtSettings = Seq(scalafmtOnCompile := true)

lazy val indigoSettingsAndDeps = Seq(
  showCursor := true,
  title := "15 Puzzle",
  gameAssetsDirectory := "assets",
  windowStartWidth := 480, // Width of Electron window, used with `indigoRun`.
  windowStartHeight := 480, // Height of Electron window, used with `indigoRun`.
  libraryDependencies ++= Seq(
    "io.indigoengine" %%% "indigo" % deps.version.indigo,
    "io.indigoengine" %%% "indigo-extras" % deps.version.indigo,
    "io.indigoengine" %%% "indigo-json-circe" % deps.version.indigo
  )
)

addCommandAlias("buildGame", ";indigo/compile;indigo/fastOptJS;indigo/indigoBuild")
addCommandAlias("runGame", ";indigo/compile;indigo/fastOptJS;indigo/indigoRun")
addCommandAlias("buildGameFull", ";indigo/compile;indigo/fullOptJS;indigo/indigoBuildFull")
addCommandAlias("runGameFull", ";indigo/compile;indigo/fullOptJS;indigo/indigoRunFull")