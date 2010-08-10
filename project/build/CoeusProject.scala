import sbt._

class CoeusProject(info: ProjectInfo) extends DefaultProject(info) {

  val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"

}
