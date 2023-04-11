
val scala3Version = "3.1.2"

lazy val util: Project = Project(id = "UNO-Util", base = file("Util"))
  .dependsOn(model)
  .settings(
      name := "bettler",
      version := "0.1.0-SNAPSHOT",

      scalaVersion := scala3Version,

      libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10",
      libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test",
      libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
      libraryDependencies += "com.google.inject" % "guice" % "4.2.3",
      libraryDependencies += ("net.codingwell" %% "scala-guice" % "5.0.2").cross(CrossVersion.for3Use2_13),
      libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.1",
      libraryDependencies += ("com.typesafe.play" %% "play-json" % "2.10.0-RC5"),

      jacocoExcludes := Seq("*aview.*", "*Bettler*", "*BettlerModule*"),
      jacocoCoverallsServiceName := "github-actions",
      jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
      jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
      jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN")
  )
  .enablePlugins(JacocoCoverallsPlugin)
parallelExecution in Test := false

lazy val core: Project = Project(id = "bettler-Core", base = file("core"))
  .dependsOn(model, util)
  .settings(
      name:="bettler-Core",
      version:="0.1.0-SNAPSHOT",
      scalaVersion := scala3Version,
      settings,
  )

lazy val model: Project = Project(id = "bettler-Model", base = file("model"))
  .settings(
      name:="bettler-Model",
      version:="0.1.0-SNAPSHOT",
      scalaVersion := scala3Version,
      settings,
  )

lazy val view: Project = Project(id = "bettler-Ui", base = file("view"))
  .dependsOn(core)
  .settings(
      name:="bettler-Ui",
      version:="0.1.0-SNAPSHOT",
      scalaVersion := scala3Version,
      settings,
  )

lazy val root: Project = Project(id = "bettler", base = file("."))
  .dependsOn(util, core, model, view)
  .aggregate(util, core, model, view)
  .settings(
      name:="bettler",
      version:="0.1.0-SNAPSHOT",
      scalaVersion := scala3Version,
      settings,
  )

lazy val settings: Seq[Def.Setting[_]] = Seq(
    jacocoReportSettings := JacocoReportSettings(
        "Jacoco Coverage Report",
        None,
        JacocoThresholds(),
        Seq(
            JacocoReportFormats.ScalaHTML,
            JacocoReportFormats.XML
        ), // note XML formatter
        "utf-8"
    ),
    jacocoExcludes := Seq(
        "*aview.*",
        "*fileIOComponent.*",
        "*.UnoModule.scala",
        "*.Uno.scala"
    ),
    jacocoCoverallsServiceName := "github-actions",
    jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
    jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
    jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN")
)
