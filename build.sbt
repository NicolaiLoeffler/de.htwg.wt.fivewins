name := """hello-play-java"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.webjars" % "bootstrap" % "2.3.1",
  "com.typesafe.play" % "play-cache_2.11" % "2.3.0",
  "com.google.inject" % "guice" % "3.0",
  "com.google.inject.extensions" % "guice-multibindings" % "3.0",
  "org.ektorp" % "org.ektorp"  % "1.2.2",
  "log4j" % "log4j" % "1.2.17",
  "com.google.code.gson" % "gson" % "2.3.1"
)

resolvers ++= Seq(
"Local Maven Repository" at "file:\\"+Path.userHome.absolutePath+"\\.m2\repository",
"Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/",
"Pablo repo" at "https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo/"
)
