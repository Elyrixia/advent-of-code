package day4

import utils.inputs.Input
import utils.run.Run

given day: Run.Day = 4
@main def example() = Run(Input.Example)(part1)
@main def ex1()     = Run(Input.Exercise)(part1)
@main def ex2()     = Run(Input.Exercise)(part2)

final case class Grid(value: Array[Array[Char]], width: Int, height: Int) {
  def get(x: Int, y: Int): Char = value(y)(x)
}
object Grid {
  def apply(value: Array[Array[Char]]): Grid = {
    val height = value.length
    val width = value.head.length
    Grid(value, width, height)
  }
}

private def readInput(input: String): Grid = Grid {
  input
    .linesIterator
    .toArray
    .map(_.toArray)
}

private def part1(input: String): Int = {
  val grid = readInput(input)

  val horizontalWords = for {
    x <- 0 until grid.width - 3 // XMAS is 4 characters
    y <- 0 until grid.height
  } yield
    (0 to 3).map(i => grid.get(x + i, y)).mkString

  val verticalWords = for {
    x <- 0 until grid.width
    y <- 0 until grid.height - 3
  } yield
    (0 to 3).map(i => grid.get(x, y + i)).mkString

  val diagonalWords = {
    for {
      x <- 0 until grid.width - 3
      y <- 0 until grid.height - 3
    } yield List(
      (0 to 3).map(i => grid.get(x + i, y + i)).mkString,
      (0 to 3).map(i => grid.get(x + 3 - i, y + i)).mkString
    )
  }.flatten

  val matches = Set("XMAS", "SAMX")
  val words = horizontalWords ++ verticalWords ++ diagonalWords
  words.count(matches.contains)
}

private def part2(input: String): Int = {
  val grid = readInput(input)

  val diagonalWords = for {
    x <- 0 until grid.width - 2 // This time we have only 3 characters
    y <- 0 until grid.height - 2
  } yield List(
    (0 to 2).map(i => grid.get(x + i, y + i)).mkString,
    (0 to 2).map(i => grid.get(x + 2 - i, y + i)).mkString
  )

  val matches = Set("MAS", "SAM")
  diagonalWords.foreach(println)
  diagonalWords.count(_.forall(matches.contains))
}
