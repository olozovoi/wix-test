package core.model

import cats.syntax.all.*
import core.model.Direction.{Down, Up}
import core.model.GameStatus.{Finished, Ongoing}

case class Board(board: Vector[Int]) {
  private lazy val emptyIndex: Int = board.indexOf(Board.EMPTY)

  lazy val status: GameStatus =
    if (emptyIndex == 15 && isSorted(board.slice(0, 15))) Finished else Ongoing

  def move(direction: Direction): Either[MoveError, Board] = direction match
    case Up =>
      if (0 until 4 contains emptyIndex) MoveError(direction).asLeft
      else swap(emptyIndex, emptyIndex - 4).asRight
    case Down =>
      if (12 until 16 contains emptyIndex) MoveError(direction).asLeft
      else swap(emptyIndex, emptyIndex + 4).asRight
    case Direction.Left =>
      if (emptyIndex % 4 == 0) MoveError(direction).asLeft
      else swap(emptyIndex, emptyIndex - 1).asRight
    case Direction.Right =>
      if (emptyIndex % 4 == 3) MoveError(direction).asLeft
      else swap(emptyIndex, emptyIndex + 1).asRight

  private def swap(index1: Int, index2: Int): Board = {
    val value1 = board(index1)
    val value2 = board(index2)
    val newBoard = board.updated(index1, value2).updated(index2, value1)
    Board(newBoard)
  }

  private def isSorted(vector: Vector[Int]) = vector == vector.sorted
}

object Board {
  val EMPTY: Int = 0
}
