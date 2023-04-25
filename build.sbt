import dependencies._
val scala3Version = "3.0.2"
parallelExecution in Test := false
fork := true
concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)
lazy val akkaDependencies = Seq(
  akkaHttp,
  akkaHttpSpray,
  akkaHttpCore,
  akkaActorTyped,
  akkaStream,
  akkaActor,
  slf4jNop
)





lazy val core: Project = Project(id = "bettler-Core", base = file("Core"))
  .dependsOn(tools, persistence)
  .settings(
    name := "bettler-Core",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    libraryDependencies += "com.google.inject" % "guice" % "4.2.3",
    libraryDependencies += ("net.codingwell" %% "scala-guice" % "5.0.2").cross(CrossVersion.for3Use2_13),
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.1",
    libraryDependencies += ("com.typesafe.play" %% "play-json" % "2.10.0-RC5"),
    libraryDependencies ++= akkaDependencies,


    jacocoExcludes := Seq("*aview.*", "*Bettler*", "*BettlerModule*"),
    jacocoCoverallsServiceName := "github-actions",
    jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
    jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
    jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN")
  )
  .enablePlugins(JacocoCoverallsPlugin)
parallelExecution in Test := false

lazy val tools: Project = Project(id = "bettler-Tools", base = file("Tools"))
  .settings(
    name := "bettler-Tools",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    libraryDependencies += "com.google.inject" % "guice" % "4.2.3",
    libraryDependencies += ("net.codingwell" %% "scala-guice" % "5.0.2").cross(CrossVersion.for3Use2_13),
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.1",
    libraryDependencies += ("com.typesafe.play" %% "play-json" % "2.10.0-RC5"),
    libraryDependencies ++= akkaDependencies,




    jacocoExcludes := Seq("*aview.*", "*Bettler*", "*BettlerModule*"),
    jacocoCoverallsServiceName := "github-actions",
    jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
    jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
    jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN")
  )
  .enablePlugins(JacocoCoverallsPlugin)
parallelExecution in Test := false

lazy val view: Project = Project(id = "bettler-view", base = file("view"))
  .dependsOn(core)
  .settings(
    name := "bettler-view",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
    libraryDependencies += "com.google.inject" % "guice" % "4.2.3",
    libraryDependencies += ("net.codingwell" %% "scala-guice" % "5.0.2").cross(CrossVersion.for3Use2_13),
    libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.1",
    libraryDependencies += ("com.typesafe.play" %% "play-json" % "2.10.0-RC5"),
    libraryDependencies ++= akkaDependencies,




    jacocoExcludes := Seq("*aview.*", "*Bettler*", "*BettlerModule*"),
    jacocoCoverallsServiceName := "github-actions",
    jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
    jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
    jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN")
  )
  .enablePlugins(JacocoCoverallsPlugin)
parallelExecution in Test := false

lazy val persistence: Project = Project(id = "bettler-Persistence", base = file("Persistence"))
  .dependsOn(tools)
  .settings(
      name := "bettler-Persistence",
      version := "0.1.0-SNAPSHOT",

      scalaVersion := scala3Version,

      libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10",
      libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test",
      libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
      libraryDependencies += "com.google.inject" % "guice" % "4.2.3",
      libraryDependencies += ("net.codingwell" %% "scala-guice" % "5.0.2").cross(CrossVersion.for3Use2_13),
      libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.1",
      libraryDependencies += ("com.typesafe.play" %% "play-json" % "2.10.0-RC5"),
      libraryDependencies ++= akkaDependencies,




    jacocoExcludes := Seq("*aview.*", "*Bettler*", "*BettlerModule*"),
      jacocoCoverallsServiceName := "github-actions",
      jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
      jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
      jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN")
  )
  .enablePlugins(JacocoCoverallsPlugin)
parallelExecution in Test := false

lazy val root: Project = Project(id = "bettler", base = file("."))
  .dependsOn(core, tools, view, persistence)
  .aggregate(core, tools, view, persistence)
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


    libraryDependencies ++= akkaDependencies,




    jacocoExcludes := Seq("*aview.*", "*Bettler*", "*BettlerModule*"),
    jacocoCoverallsServiceName := "github-actions",
    jacocoCoverallsBranch := sys.env.get("CI_BRANCH"),
    jacocoCoverallsPullRequest := sys.env.get("GITHUB_EVENT_NAME"),
    jacocoCoverallsRepoToken := sys.env.get("COVERALLS_REPO_TOKEN")
  )
  .enablePlugins(JacocoCoverallsPlugin)


