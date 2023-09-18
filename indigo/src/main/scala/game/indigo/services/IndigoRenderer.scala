package game.indigo.services

import core.model.Game
import core.services.Renderer
import game.indigo.subsystem.GameSubSystem.RenderEvent
import game.indigo.subsystem.GameSubSystem.RenderEvent.{Render, ShowText}
import indigo.shared.events.GlobalEvent
import zio.{Queue, UIO, ULayer, ZIO, ZLayer, Console}

case class IndigoRenderer(renderQueue: Queue[RenderEvent]) extends Renderer {
  override def render(game: Game): UIO[Unit] = renderQueue.offer(Render(game)).ignore

  override def showText(text: String): UIO[Unit] =
    Console.printLine(text).orDie // renderQueue.offer(ShowText(text)).ignore
}

object IndigoRenderer {
  def layer(renderQueue: Queue[RenderEvent]): ULayer[IndigoRenderer] = ZLayer
    .succeed(IndigoRenderer(renderQueue))
}
