package day2

import utils.inputs.Input
import utils.run.Run

given day: Run.Day = 2
@main def example() = Run(Input.Example)(part1)
@main def ex1()     = Run(Input.Exercise)(part1)
@main def ex2()     = Run(Input.Exercise)(part2)

private def readInput(input: String): List[List[Int]] =
  input
    .linesIterator
    .map { line => line.split(" ").toList.map(_.toInt) }
    .toList

private def isSafe(line: List[Int]) = {
  val sliding2 = line.sliding(2).toList
  sliding2.headOption match {
    case Some(first :: second :: Nil) if first < second =>
      sliding2.forall {
        case a :: b :: Nil =>
          val diff = b - a
          diff > 0 && diff <= 3
      }
    case Some(first :: second :: Nil) if first > second =>
      sliding2.forall {
        case a :: b :: Nil =>
          val diff = a - b
          diff > 0 && diff <= 3
      }
    case _ => false
  }
}

private def part1(input: String): Int =
  readInput(input).count(isSafe)

private def part2(input: String): Int =
  readInput(input)
    .count { line =>
      (1 to line.size).exists { index =>
        val (firstHalf, secondHalf) = line.splitAt(index)
        val listWithUpToOneElementRemoved = firstHalf match {
          case firstHalfWithoutLast :+ _ => firstHalfWithoutLast ++ secondHalf
          case Nil                       => secondHalf // This manages the full list case
        }
        isSafe(listWithUpToOneElementRemoved)
      }
    }
