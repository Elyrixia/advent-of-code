package day1

import utils.inputs.Input
import utils.run.Run

given day: Run.Day = 1
@main def example() = Run(Input.Example)(part1)
@main def ex1()     = Run(Input.Exercise)(part1)
@main def ex2()     = Run(Input.Exercise)(part2)

private def readInput(input: String): (List[Int], List[Int]) =
  input
    .linesIterator
    .map { line => line.split(" {3}").take(2) }
    .map { line => (line.head.toInt, line.last.toInt) }
    .toList
    .unzip

private def part1(input: String): Int = {
  val (list1, list2) = readInput(input)
  val (sortedList1, sortedList2) = (list1.sorted, list2.sorted)
  sortedList1.zip(sortedList2).map { case (l1, l2) => Math.abs(l1 - l2) }.sum
}

private def part2(input: String): Int = {
  val (list1, list2) = readInput(input)
  val integersOccurrence: Map[Int, Int] = list2.groupBy(v => v).map((k, v) => (k, v.size))
  list1.map(i => i * integersOccurrence.getOrElse(i, 0)).sum
}
