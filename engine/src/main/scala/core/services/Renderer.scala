package core.services

import core.model.Game
import zio.*

trait Renderer {
  def render(game: Game): UIO[Unit]
  
  def showText(text: String): UIO[Unit]
}

object Renderer {
  def render(game: Game): URIO[Renderer, Unit] = ZIO.serviceWithZIO(_.render(game))
  
  def showText(text: String): URIO[Renderer, Unit] = ZIO.serviceWithZIO(_.showText(text))
}
