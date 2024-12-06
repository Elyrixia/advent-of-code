package day6

import utils.inputs.Input
import utils.run.Run

given day: Run.Day = 6
@main def example() = Run(Input.Example)(part1)
@main def ex1()     = Run(Input.Exercise)(part1)
@main def ex2()     = Run(Input.Exercise)(part2)

sealed trait Direction
object Direction {
  case object Up extends Direction
  case object Down extends Direction
  case object Left extends Direction
  case object Right extends Direction
}

final case class Obstacle(x: Int, y: Int)
final case class Guard(x: Int, y: Int, direction: Direction) {
  def move: Guard = direction match {
    case Direction.Up    => copy(y = y - 1)
    case Direction.Down  => copy(y = y + 1)
    case Direction.Left  => copy(x = x - 1)
    case Direction.Right => copy(x = x + 1)
  }
  def turn: Guard = direction match {
    case Direction.Up    => copy(direction = Direction.Right)
    case Direction.Down  => copy(direction = Direction.Left)
    case Direction.Left  => copy(direction = Direction.Up)
    case Direction.Right => copy(direction = Direction.Down)
  }
}

final case class Grid(
  height: Int,
  width: Int,
  obstacles: Set[Obstacle]
)

final case class Game(
  grid: Grid,
  guard: Guard,
  passed: Set[(Int, Int)] = Set.empty,
  passedWithDirection: Set[(Int, Int, Direction)] = Set.empty,
) {
  def moveGuard: Game = {
    val newTarget = guard.move
    val newGuard =
      if (grid.obstacles.contains(Obstacle(newTarget.x, newTarget.y)))
        guard.turn
      else
        newTarget

    // For some reason the typing must be explicit here
    val newPassedWithDirection: (Int, Int, Direction) = (guard.x , guard.y, guard.direction)
    copy(
      guard = newGuard,
      passed = passed + (guard.x -> guard.y),
      passedWithDirection = passedWithDirection + newPassedWithDirection
    )
  }

  def isGuardOut: Boolean = guard.x < 0 || guard.x >= grid.width || guard.y < 0 || guard.y >= grid.height
  def isGuardStuck: Boolean = {
    val guardPosWithDirection: (Int, Int, Direction) = (guard.x , guard.y, guard.direction)
    passedWithDirection.contains(guardPosWithDirection)
  }
  def isGameFinished: Boolean = isGuardStuck || isGuardOut
}

private def readInput(input: String): Game = {
  val lines = input.linesIterator.toList
  val height = lines.size
  val width = lines.head.length
  val obstaclesAndGuard: List[Obstacle | Guard] =
    lines
      .zipWithIndex
      .flatMap { (line, y) =>
        line.zipWithIndex.collect {
          case ('#', x) => Obstacle(x, y)
          case ('v', x) => Guard(x, y, Direction.Down)
          case ('^', x) => Guard(x, y, Direction.Up)
          case ('>', x) => Guard(x, y, Direction.Right)
          case ('<', x) => Guard(x, y, Direction.Left)
        }.toList: List[Obstacle | Guard]
      }
  val obstacles = obstaclesAndGuard.collect { case o: Obstacle => o }.toSet
  val guard = obstaclesAndGuard.collect { case g: Guard => g }.head
  val grid = Grid(height, width, obstacles)
  Game(grid, guard)
}

private def part1(input: String): Int = {
  val game: Game = readInput(input)
  LazyList
    .iterate(game)(_.moveGuard)
    .takeWhile(!_.isGuardOut)
    .last
    .moveGuard // we need to apply the last move again as we stopped before applying it with the takeWhile
    .passed
    .size
}

private def part2(input: String): Int = {
  val game: Game = readInput(input)

  (
    for {
      x <- 0 until game.grid.width
      y <- 0 until game.grid.height
    } yield {
      val additionalObstacle = Obstacle(x, y)
      if (
        game.grid.obstacles.contains(additionalObstacle) ||
          (game.guard.x == x && game.guard.y == y)
      ) false
      else {
        val newGame = game.copy(grid = game.grid.copy(obstacles = game.grid.obstacles + additionalObstacle))
        val result = LazyList
          .iterate(newGame)(_.moveGuard)
          .takeWhile(!_.isGameFinished)
          .last
          .moveGuard
        result.isGuardStuck
      }
    }
  ).count(_ == true)
}
