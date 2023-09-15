package game.console.services

import cats.syntax.all.*
import core.model.Direction
import core.model.Direction.{Down, Up}
import core.services.Command.{Move, Quit, StartNew}
import core.services.{Command, CommandProvider}
import zio.*

object ConsoleCommandProvider extends CommandProvider {
  override def nextCommand: UIO[Command] = for {
    input <- Console.readLine("Command: ").orDie
    cmd <- parse(input).fold(
      err => Console.printLine(s"Unknown command \"${err.input}\"").orDie *> nextCommand,
      cmd => ZIO.succeed(cmd)
    )
  } yield cmd

  private case class ParseCommandError(input: String)

  private def parse(input: String): Either[ParseCommandError, Command] = input match
    case "start" | "s" => StartNew.asRight
    case "quit" | "q"  => Quit.asRight
    case "up" | "u"    => Move(Up).asRight
    case "down" | "d"  => Move(Down).asRight
    case "left" | "l"  => Move(Direction.Left).asRight
    case "right" | "r" => Move(Direction.Right).asRight
    case _             => ParseCommandError(input).asLeft

  lazy val layer: ULayer[CommandProvider] = ZLayer.succeed(ConsoleCommandProvider)
}
