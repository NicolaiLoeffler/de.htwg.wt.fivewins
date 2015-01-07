name := """hello-play-java"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.pac4j" % "play-pac4j_java" % "1.4.0-SNAPSHOT",
  "org.pac4j" % "pac4j-oauth" % "1.7.0-SNAPSHOT",
  "org.pac4j" % "pac4j-oidc" % "1.7.0-SNAPSHOT",
  "com.typesafe.play" % "play-cache_2.11" % "2.3.0"
)

resolvers ++= Seq(
"Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
"Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/",
"Pablo repo" at "https://raw.github.com/fernandezpablo85/scribe-java/mvn-repo/"
)
