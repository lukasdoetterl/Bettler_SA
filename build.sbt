import dependencies._
val scala3Version = "3.1.2"
parallelExecution in Test := false
fork := true
concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)
connectInput in run := true
lazy val akkaDependencies = Seq(
  akkaHttp,
  akkaHttpSpray,
  akkaHttpCore,
  akkaActorTyped,
  akkaStream,
  akkaActor,
  slf4jNop,
  scalatic,
  scalatest,
  scalalan,
  googleinject,
  codingwel,
  scalaxml,
  playjson
)






lazy val core: Project = Project(id = "bettler-Core", base = file("Core"))
  .dependsOn(tools, persistence)
  .settings(
    name := "bettler-Core",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,


    libraryDependencies ++= akkaDependencies,

  )
  .enablePlugins(JacocoCoverallsPlugin)
parallelExecution in Test := false



lazy val view: Project = Project(id = "bettler-view", base = file("view"))
  .dependsOn(core)
  .settings(
    name := "bettler-view",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= akkaDependencies,



  )
  .enablePlugins(JacocoCoverallsPlugin)
parallelExecution in Test := false

lazy val persistence: Project = Project(id = "bettler-Persistence", base = file("Persistence"))
  .dependsOn(tools)
  .settings(
      name := "bettler-Persistence",
      version := "0.1.0-SNAPSHOT",

      scalaVersion := scala3Version,
  )
  .enablePlugins(JacocoCoverallsPlugin)

lazy val cardgeneration: Project = Project(id = "bettler-cardgeneration", base = file("Cardgenerating"))
  .dependsOn()
  .settings(
    name := "bettler-Persistence",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= akkaDependencies,
  )
  .enablePlugins(JacocoCoverallsPlugin)
parallelExecution in Test := false

lazy val tools: Project = Project(id = "bettler-Tools", base = file("Tools"))
  .dependsOn(cardgeneration)
  .settings(
    name := "bettler-Tools",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= akkaDependencies

  )
  .enablePlugins(JacocoCoverallsPlugin)
parallelExecution in Test := false

lazy val root: Project = Project(id = "bettler", base = file("."))
  .dependsOn(core, tools, view, persistence, cardgeneration)
  .aggregate(core, tools, view, persistence, cardgeneration)
  .settings(
    name := "bettler",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,



    libraryDependencies ++= akkaDependencies,


  )
  .enablePlugins(JacocoCoverallsPlugin)


