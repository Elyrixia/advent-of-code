package utils.inputs

import utils.locations.Directory.currentDir

import scala.util.Using
import scala.io.Source

object Input {

  def loadFileSync(path: String): String =
    Using.resource(Source.fromFile(path))(_.mkString)

  def loadExampleForDay(day: Int): String =
    loadFileSync(s"$currentDir/../../input/day$day/example.txt")

  def loadForDay(day: Int): String =
    loadFileSync(s"$currentDir/../../input/day$day/input.txt")

}
