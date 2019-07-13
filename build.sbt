
lazy val root = (project in file("."))
  .settings(
    name := "IMDb-searcher",
    version := "0.1",
    scalaVersion := "2.12.8",
    mainClass := Some("com.myapp.Main"),

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      "org.scalamock" %% "scalamock" % "4.1.0" % Test,
    ),
  )