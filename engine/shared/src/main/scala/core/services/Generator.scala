package core.services

import core.model.Game
import zio.*

trait Generator {
  def gen: UIO[Game]
}

object Generator {
  def gen: URIO[Generator, Game] = ZIO.serviceWithZIO(_.gen)
}
