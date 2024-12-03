package day3

import utils.inputs.Input

@main def example(): Unit =
  println(s"The solution is ${part1(Input.loadExampleForDay(3))}")

@main def ex1(): Unit =
  println(s"The solution is ${part1(Input.loadForDay(3))}")

@main def ex2(): Unit =
  println(s"The solution is ${part2(Input.loadForDay(3))}")

private val regexMul = """mul\(([0-9]{1,3}),([0-9]{1,3})\)""".r
private def sumMul(text: String): Int =
  regexMul
    .findAllMatchIn(text)
    .map { matchedString => matchedString.group(1).toInt * matchedString.group(2).toInt }
    .sum

private def part1(input: String): Int =
  sumMul(input)

private val dontDoRegex = """don't\(\)((?!do\(\)).)*""".r
private def part2(input: String): Int =
  sumMul(dontDoRegex.replaceAllIn(input.mkString, ""))
