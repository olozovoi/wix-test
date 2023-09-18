package game.indigo

import core.model.{Direction => D}
import core.model.{Board, Direction, Game}
import core.services.Command
import core.services.Command.Move
import game.indigo.subsystem.GameSubSystem
import game.indigo.subsystem.GameSubSystem.CommandEvent
import indigo.*
import indigo.scenes.*

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("IndigoGame")
object IndigoGame extends IndigoGame[Unit, Unit, Model, Unit] {

  val config: GameConfig = GameConfig.default.withViewport(518, 518)

  private def asset(n: Int): AssetName = AssetName(s"cell_$n")

  val assets: Set[AssetType] = Set
    .from(0 to 15 map (n => AssetType.Image(asset(n), AssetPath(s"assets/cell_$n.jpg"))))

  override def scenes(bootData: Unit): NonEmptyList[Scene[Unit, Model, Unit]] =
    NonEmptyList(Scene.empty)

  override def initialScene(bootData: Unit): Option[SceneName] = None

  override def eventFilters: EventFilters = EventFilters.Permissive

  override def boot(flags: Map[String, String]): Outcome[BootResult[Unit]] =
    Outcome(BootResult.noData(config).withAssets(assets).withSubSystems(GameSubSystem()))

  override def setup(
      bootData: Unit,
      assetCollection: AssetCollection,
      dice: Dice
  ): Outcome[Startup[Unit]] = Outcome(Startup.Success(()))

  override def initialModel(startupData: Unit): Outcome[Model] = Outcome(Model.initial)

  override def initialViewModel(startupData: Unit, model: Model): Outcome[Unit] = Outcome(())

  override def updateModel(
      context: FrameContext[Unit],
      model: Model
  ): GlobalEvent => Outcome[Model] =
    case GameSubSystem.RenderEvent.Render(game)   => Outcome(model.copy(game = game))
    case GameSubSystem.RenderEvent.ShowText(text) => Outcome(model)
    case KeyboardEvent.KeyUp(key) =>
      val event = key match
        case Key.UP_ARROW    => CommandEvent(Command.Move(D.Up))
        case Key.DOWN_ARROW  => CommandEvent(Command.Move(D.Down))
        case Key.LEFT_ARROW  => CommandEvent(Command.Move(D.Left))
        case Key.RIGHT_ARROW => CommandEvent(Command.Move(D.Right))

      Outcome(model).addGlobalEvents(event)
    case _ => Outcome(model)

  override def updateViewModel(
      context: FrameContext[Unit],
      model: Model,
      viewModel: Unit
  ): GlobalEvent => Outcome[Unit] = _ => Outcome(())

  override def present(
      context: FrameContext[Unit],
      model: Model,
      viewModel: Unit
  ): Outcome[SceneUpdateFragment] = Outcome(SceneUpdateFragment(drawGame(model.game)))

  private def drawGame(game: Game): Batch[Graphic[_]] = {
    val rects = game.board.flat.zipWithIndex.map { case (n, i) =>
      val (x, y) = (i % 4, i / 4)
      val size = 128
      val c = size + 2
      Graphic(Rectangle(size, size), Material.Bitmap(asset(n)).stretch).moveTo(x * c, y * c)
    }.toSeq

    Batch.fromSeq(rects)
  }
}

case class Model(game: Game)

object Model {
  def initial: Model = Model(Game(Board(0 to 15), 0))
}