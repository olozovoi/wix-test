package game.console

import core.runtime.GameRuntime
import core.services.ConsecutiveGenerator
import game.console.services.{ConsoleCommandProvider, ConsoleRenderer}
import zio.*

object ConsoleGame extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = GameRuntime.run
    .provide(ConsoleRenderer.layer, ConsoleCommandProvider.layer, ConsecutiveGenerator.layer)
}
