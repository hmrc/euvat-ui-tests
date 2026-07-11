import sbt.*

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "ui-test-runner" % "0.54.0" % Test,
    ("org.mongodb.scala" %% "mongo-scala-driver" % "5.8.0" % Test).cross(CrossVersion.for3Use2_13),
    "com.oracle.database.jdbc" % "ojdbc8" % "23.26.2.0.0" % Test, // Oracle JDBC driver
    "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.2.17" % Test,
    ("com.typesafe.play" %% "play-json" % "2.10.8" % Test).cross(CrossVersion.for3Use2_13)
  )

}
