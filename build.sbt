val catsEffectV = "2.3.0"
val circeV = "0.13.0"
val circeGoldenV = "0.2.1"
val cirisV = "1.2.1"
val fs2V = "2.4.6"
val fs2EsV = "0.3.0"
val http4sV = "0.21.14"
val http4sJdkClientV = "0.3.2"
val log4catsV = "1.1.1"
val munitV = "0.7.19"
val scalaJSReactV = "1.7.7"
val endpointsV = "1.2.0"
val endpointsHttp4sV = "4.0.0"
val reactV = "17.0.1"

val commonSettings = Seq(
  organization := "dev.rpeters",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.13.4",
  testFrameworks += new TestFramework("munit.Framework"),
  addCompilerPlugin(
    "org.typelevel" %% "kind-projector" % "0.11.2" cross CrossVersion.full
  ),
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
)

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .in(file("modules/core"))
  .settings(
    commonSettings,
    name := "circe-bug-core",
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeV,
      "org.scalameta" %%% "munit-scalacheck" % munitV % "test",
      "org.typelevel" %%% "cats-effect-laws" % catsEffectV % "test",
      "io.circe" %% "circe-golden" % circeGoldenV % "test"
    )
  )

lazy val webClient = (project in file("modules/web-client"))
  .settings(
    commonSettings,
    name := "circe-bug-web-client",
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-parser" % circeV,
      "io.github.cquiroz" %%% "scala-java-time" % "2.0.0"
    ),
    scalaJSUseMainModuleInitializer := true,
    webpackBundlingMode := BundlingMode.LibraryAndApplication(),
    webpack / version := "4.44.2",
    Compile / npmDependencies ++= Seq(
      // "react" -> reactV,
      // "react-dom" -> reactV
    )
  )
  .dependsOn(core.js % "compile->compile;test->test")
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

lazy val root = (project in file("."))
  .aggregate(core.js, core.jvm, webClient)
