package game.indigo.services

import core.services.{Command, CommandProvider}
import zio.{Queue, UIO, ULayer, ZLayer}

case class IndigoCommandProvider(commandQueue: Queue[Command]) extends CommandProvider {
  override def nextCommand: UIO[Command] = commandQueue.take
}

object IndigoCommandProvider {
  def layer(commandQueue: Queue[Command]): ULayer[IndigoCommandProvider] = ZLayer
    .succeed(IndigoCommandProvider(commandQueue))
}
