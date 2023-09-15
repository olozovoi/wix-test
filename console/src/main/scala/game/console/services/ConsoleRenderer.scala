package game.console.services

import core.model.{Board, Game}
import core.services.Renderer
import zio.*

object ConsoleRenderer extends Renderer {
  override def render(game: Game): UIO[Unit] = Console.printLine(draw(game.board)).orDie

  private def draw(board: Board): String = {
    board.flat.map {
      case Board.EMPTY => " xx "
      case n           => " %02d ".format(n)
    }.grouped(4).map(_.mkString("|")).mkString("\n-------------------\n")
  }

  override def showText(text: String): UIO[Unit] = Console.printLine(text).orDie

  lazy val layer: ULayer[Renderer] = ZLayer.succeed(ConsoleRenderer)
}
