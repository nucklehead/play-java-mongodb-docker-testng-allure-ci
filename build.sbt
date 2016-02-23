name := """play-java-mongodb-docker-testng-allure-ci"""

version := "1.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.google.code.morphia" % "morphia" % "0.99",
  "org.mongodb" % "mongo-java-driver" % "2.10.1",
  "com.google.code.morphia" % "morphia-logging-slf4j" % "0.99",
  "org.dbunit" % "dbunit" % "2.4.9",
  "org.codehaus.jackson" % "jackson-mapper-asl" % "1.8.5",
  javaWs
)

resolvers ++= Seq(
  "Maven repository" at "http://morphia.googlecode.com/svn/mavenrepo/",
  "MongoDb Java Driver Repository" at "http://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/"
)


// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


fork in run := true