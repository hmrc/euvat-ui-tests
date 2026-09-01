import sbt.*

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "ui-test-runner"     % "0.55.0"   % Test,
    "org.mongodb.scala"       %% "mongo-scala-driver" % "5.11.0" cross CrossVersion.for3Use2_13,
    "com.oracle.database.jdbc" % "ojdbc8"             % "23.26.3.0.0", // Oracle JDBC driver
    "org.scalatest"           %% "scalatest"          % "3.2.20"   % Test,
    "org.seleniumhq.selenium"  % "selenium-java"      % "4.48.0"   % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play" % "7.0.2"    % Test,
    "org.scalatestplus"       %% "selenium-4-21"      % "3.2.19.0" % Test,
    "org.apache.poi"           % "poi-ooxml"          % "5.5.1"    % Test
  )

}
