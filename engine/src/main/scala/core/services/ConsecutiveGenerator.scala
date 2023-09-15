package core.services
import core.model.{Board, Game}
import zio.*

object ConsecutiveGenerator extends Generator {
  override def gen: UIO[Game] = ZIO.succeed(Game(Board((0 to 15).toVector), 0))
  
  val layer: ULayer[Generator] = ZLayer.succeed(ConsecutiveGenerator)
}
