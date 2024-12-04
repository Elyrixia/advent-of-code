package utils.run

import utils.inputs.Input

object Run {

  opaque type Day = Int
  given int2Day: Conversion[Int, Day] with
    def apply(x: Int): Day = x

  def apply(input: Input)(f: String => Int)(using day: Day) = {
    val result = input match {
      case Input.Example  => f(Input.loadExampleForDay(day))
      case Input.Exercise => f(Input.loadForDay(day))
    }
    println(s"The solution is $result")
  }

}
