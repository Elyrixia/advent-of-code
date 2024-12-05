package day5

import utils.inputs.Input
import utils.run.Run

given day: Run.Day = 5
@main def example() = Run(Input.Example)(part1)
@main def ex1()     = Run(Input.Exercise)(part1)
@main def ex2()     = Run(Input.Exercise)(part2)

private def readPriorities(input: String): Map[Int, Set[Int]] =
  input
    .linesIterator
    .collect { case s"${page}|${priorities}" => (page.toInt, priorities.toInt) }
    .toList
    .groupBy(_._1)
    .view
    .mapValues(_.map(_._2).toSet)
    .toMap

private def readUpdates(input: String): List[List[Int]] =
  input
    .linesIterator
    .collect {
      case update if update.contains(",") =>
        update.split(",").map(_.toInt).toList
    }
    .toList

sealed trait UpdateStatus
object UpdateStatus {
  final case class Valid(previousPages: Set[Int] = Set.empty) extends UpdateStatus
  case object Invalid extends UpdateStatus
}

private def part1(input: String): Int = {
  val priorities = readPriorities(input)
  val updates = readUpdates(input)

  updates
    .map { update =>
      update.foldLeft(UpdateStatus.Valid(): UpdateStatus) {
        case (stillValid @ UpdateStatus.Valid(_), nextPage) =>
          val prioritizedPages = priorities.getOrElse(nextPage, Set.empty)
          if (stillValid.previousPages.intersect(prioritizedPages).isEmpty)
            stillValid.copy(previousPages = stillValid.previousPages + nextPage)
          else UpdateStatus.Invalid
        case (UpdateStatus.Invalid, _) => UpdateStatus.Invalid
      } match {
        case UpdateStatus.Invalid => 0
        case UpdateStatus.Valid(_) => update(update.size / 2)
      }
    }
    .sum

}



private def part2(input: String): Int = {
  val priorities = readPriorities(input)
  val updates = readUpdates(input)

  def sortUpdate(sortedUpdate: List[Int])(
    checkedElements: List[Int] = List.empty,
    currentIndex: Int = 0
  ): List[Int] = {
    if (currentIndex >= sortedUpdate.size) sortedUpdate
    else {
      val priorityForIndex = priorities.getOrElse(sortedUpdate(currentIndex), Set.empty)
      if (checkedElements.exists(priorityForIndex.contains)) {
        val (before, after) = sortedUpdate.splitAt(currentIndex)
        val updated = after.head +: before :++ after.tail
        sortUpdate(updated)()
      } else {
        sortUpdate(sortedUpdate)(checkedElements :+ sortedUpdate(currentIndex), currentIndex + 1)
      }
    }
  }

  updates
    .flatMap { update =>
      update.foldLeft(UpdateStatus.Valid(): UpdateStatus) {
        case (stillValid @ UpdateStatus.Valid(_), nextPage) =>
          val prioritizedPages = priorities.getOrElse(nextPage, Set.empty)
          if (stillValid.previousPages.intersect(prioritizedPages).isEmpty) stillValid.copy(previousPages = stillValid.previousPages + nextPage)
          else UpdateStatus.Invalid
        case (UpdateStatus.Invalid, _) => UpdateStatus.Invalid
      } match {
        case UpdateStatus.Invalid => Some(update)
        case UpdateStatus.Valid(_) => None
      }
    }
    .map { badlySortedUpdate =>
      val sortedUpdate = sortUpdate(badlySortedUpdate)()
      sortedUpdate(sortedUpdate.size / 2)
    }
    .sum

}
