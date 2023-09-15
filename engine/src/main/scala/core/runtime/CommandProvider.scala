package core.runtime

import zio.*

trait CommandProvider {
  def nextCommand: UIO[Command]
}

object CommandProvider {
  def nextCommand: URIO[CommandProvider, Command] = ZIO.serviceWithZIO(_.nextCommand)
}
