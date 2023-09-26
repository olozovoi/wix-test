package core.runtime

import core.model.Game
import core.model.GameStatus.{Finished, Ongoing}
import core.services.Command.{Quit, StartNew}
import core.services.{Command, CommandProvider, Generator, Renderer}
import zio.*

object GameRuntime {
  private type Env = Renderer & CommandProvider & Generator

  private def gameLoop(game: Game): URIO[Env, Unit] = for {
    _ <- Renderer.render(game)

    _ <- game.status match
      case Finished => for {
          _ <- Renderer.showText(s"Congratulations! You finished in ${game.steps} steps")
          cmd <- CommandProvider.nextCommand.repeatWhile(cmd => cmd.isInstanceOf[Command.Move])
          _ <- processCommand(game, cmd)
        } yield ()
      case Ongoing => for {
          cmd <- CommandProvider.nextCommand
          _ <- processCommand(game, cmd)
        } yield ()
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
