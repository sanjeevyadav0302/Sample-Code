name := "authentication-module"

version := "2.6.0-SNAPSHOT"

scalaVersion := "2.12.4"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += guice
libraryDependencies += jdbc
libraryDependencies += ws


libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.3"
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.8-dmr"
libraryDependencies += "com.jason-goodwin" %% "authentikat-jwt" % "0.4.5"
libraryDependencies += "com.typesafe.play" % "play-json-joda_2.12" % "2.6.0"



