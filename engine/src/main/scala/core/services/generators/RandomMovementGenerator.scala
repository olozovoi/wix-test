package core.services.generators

import core.model.{Board, Direction, Game}
import core.services.Generator
import zio.*

case class RandomMovementGenerator(depth: Int) extends Generator {
  override def gen: UIO[Game] = genBoard(finishedBoard, depth).map(Game(_, 0))

  private val finishedBoard = Board((1 to 15) :+ Board.EMPTY)

  private val directions = Direction.values

  private def genBoard(board: Board, depths: Int): UIO[Board] = ZIO
    .iterate((board, depths))(_._2 > 0) { case (board, depth) =>
      for {
        index <- Random.nextIntBounded(4)
        direction = directions(index)
        res = board.move(direction).fold(_ => (board, depth), board => (board, depth - 1))
      } yield res
    }.map(_._1)
}

object RandomMovementGenerator {
  def layer(depth: Int): ULayer[Generator] = ZLayer.succeed(RandomMovementGenerator(depth))
}
