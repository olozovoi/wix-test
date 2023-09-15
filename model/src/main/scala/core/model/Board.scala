package core.model

import core.model.GameStatus.{Finished, Ongoing}

case class Board (board: Vector[Int]) {
  private lazy val emptyIndex: Int = board.indexOf(Board.EMPTY)

  lazy val status: GameStatus = if (emptyIndex == 15 && isSorted(board.slice(0,16))) Finished else Ongoing

  private def isSorted(vector: Vector[Int]) = vector == vector.sorted
}

object Board {
  val EMPTY: Int = 0
}
