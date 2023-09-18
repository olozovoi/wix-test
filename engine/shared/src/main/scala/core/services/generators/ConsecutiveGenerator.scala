package core.services.generators

import core.model.{Board, Game}
import core.services.Generator
import zio.*

object ConsecutiveGenerator extends Generator {
  override def gen: UIO[Game] = ZIO.succeed(Game(Board(0 to 15), 0))

  val layer: ULayer[Generator] = ZLayer.succeed(ConsecutiveGenerator)
}
