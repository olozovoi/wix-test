package game.indigo.subsystem

import core.model.Game
import core.runtime.GameRuntime
import core.runtime.GameRuntime.Env
import core.services.Command
import core.services.generators.RandomMovementGenerator
import game.indigo.services.{IndigoCommandProvider, IndigoRenderer}
import game.indigo.subsystem.GameSubSystem.{CommandEvent, GameSubSystemEnqueue, RenderEvent}
import indigo.shared.Outcome
import indigo.shared.collections.Batch
import indigo.shared.events.{FrameTick, GlobalEvent}
import indigo.shared.scenegraph.SceneUpdateFragment
import indigo.shared.subsystems.{SubSystem, SubSystemFrameContext, SubSystemId}
import org.scalajs.dom.EventTarget
import zio.*

case class GameSubSystem(commandQueue: Queue[Command], renderQueue: Queue[RenderEvent])
    extends SubSystem {
  override type EventType = GlobalEvent
  override type SubSystemModel = Unit

  override def id: SubSystemId = SubSystemId("[15 Puzzle SubSystem]")

  override def eventFilter: GlobalEvent => Option[GlobalEvent] =
    case FrameTick       => Some(GameSubSystemEnqueue)
    case e: CommandEvent => Some(e)
    case _               => None

  override def initialModel: Outcome[Unit] = Outcome(())

  override def update(context: SubSystemFrameContext, model: Unit): GlobalEvent => Outcome[Unit] =
    case CommandEvent(cmd) =>
      GameSubSystem.runUIO(commandQueue.offer(cmd))
      Outcome(())
    case GameSubSystemEnqueue =>
      val events = GameSubSystem.runUIO(renderQueue.takeAll)
      Outcome((), Batch.fromSeq(events))

    case _ => Outcome(())

  override def present(context: SubSystemFrameContext, model: Unit): Outcome[SceneUpdateFragment] =
    Outcome(SceneUpdateFragment.empty)

}

object GameSubSystem {
  private val runtime = Runtime.default

  private def runUIO[A](zio: UIO[A]) = Unsafe.unsafe { implicit unsafe =>
    runtime.unsafe.run(zio).getOrThrowFiberFailure()
  }

  def apply(): GameSubSystem = {
    val gameLoop = for {
      commandQueue <- Queue.bounded[Command](1)
      renderQueue <- Queue.unbounded[RenderEvent]
      _ <- GameRuntime.run.provide(
        IndigoCommandProvider.layer(commandQueue),
        IndigoRenderer.layer(renderQueue),
        RandomMovementGenerator.layer(1)
      ).forkDaemon
    } yield (commandQueue, renderQueue)

    val (commandQueue, renderQueue) = runUIO(gameLoop)

    GameSubSystem(commandQueue, renderQueue)
  }

  enum RenderEvent extends GlobalEvent:
    case Render(game: Game)
    case ShowText(text: String)

  case class CommandEvent(cmd: Command) extends GlobalEvent

  case object GameSubSystemEnqueue extends GlobalEvent
}
