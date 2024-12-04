package day3

import utils.inputs.Input
import utils.run.Run

given day: Run.Day = 3
@main def example() = Run(Input.Example)(part1)
@main def ex1()     = Run(Input.Exercise)(part1)
@main def ex2()     = Run(Input.Exercise)(part2)

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
