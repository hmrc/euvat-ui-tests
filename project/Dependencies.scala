import sbt.*

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "ui-test-runner"     % "0.53.0" % Test,
    "org.mongodb.scala"       %% "mongo-scala-driver" % "5.1.1" cross CrossVersion.for3Use2_13,
    "com.oracle.database.jdbc" % "ojdbc8"             % "23.26.1.0.0" // Oracle JDBC driver
  )

}
