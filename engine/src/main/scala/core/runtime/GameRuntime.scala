package core.runtime

import core.model.Game
import core.services.Command.{Quit, StartNew}
import core.services.{Command, CommandProvider, Generator, Renderer}
import zio.*

object GameRuntime {
  private type Env = Renderer & CommandProvider & Generator

  private def gameLoop(game: Game): URIO[Env, Unit] = for {
    _ <- Renderer.render(game)
    cmd <- CommandProvider.nextCommand
    _ <- processCommand(game, cmd)
  } yield ()

  private def processCommand(game: Game, cmd: Command): URIO[Env, Unit] = cmd match
    case StartNew => run
    case Quit     => Renderer.showText("Bye Bye")
    case Command.Move(direction) => game.move(direction).fold(
        err => Renderer.showText(s"Cannot move to ${err.direction}") *> gameLoop(game),
        game => gameLoop(game)
      )

  def run: ZIO[Env, Nothing, Unit] = Renderer.showText("Staring new game") *>
    Generator.gen.flatMap(gameLoop)
}
