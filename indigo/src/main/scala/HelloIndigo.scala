import indigo.*
import indigo.scenes.*

import scala.scalajs.js.annotation.JSExportTopLevel

//@JSExportTopLevel("IndigoGame")
object HelloIndigo extends IndigoGame[Unit, Unit, Model, Unit] {

  private val magnification = 3

  val config: GameConfig = GameConfig.default.withMagnification(magnification)

  private val assetName = AssetName("dots")

  val assets: Set[AssetType] = Set(AssetType.Image(AssetName("dots"), AssetPath("assets/dots.png")))

  def initialScene(bootData: Unit): Option[SceneName] = None

  def scenes(bootData: Unit): NonEmptyList[Scene[Unit, Model, Unit]] = NonEmptyList(Scene.empty)

  def eventFilters: EventFilters = EventFilters.Permissive

  def boot(flags: Map[String, String]): Outcome[BootResult[Unit]] =
    Outcome(BootResult.noData(config).withAssets(assets))

  def setup(bootData: Unit, assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def initialViewModel(startupData: Unit, model: Model): Outcome[Unit] = Outcome(())

  def updateViewModel(
      context: FrameContext[Unit],
      model: Model,
      viewModel: Unit
  ): GlobalEvent => Outcome[Unit] = _ => Outcome(())

  def initialModel(startupData: Unit): Outcome[Model] =
    Outcome(Model.initial(config.viewport.giveDimensions(magnification).center))

  def updateModel(context: FrameContext[Unit], model: Model): GlobalEvent => Outcome[Model] = {
    case MouseEvent.Click(pt) =>
      val adjustedPosition = pt - model.center

      Outcome(model.addDot(Dot(
        Point.distanceBetween(model.center, pt).toInt,
        Radians(Math.atan2(adjustedPosition.x.toDouble, adjustedPosition.y.toDouble))
      )))

    case FrameTick => Outcome(model.update(context.delta))

    case _ => Outcome(model)
  }

  def present(
      context: FrameContext[Unit],
      model: Model,
      viewModel: Unit
  ): Outcome[SceneUpdateFragment] = Outcome(SceneUpdateFragment(
    Graphic(Rectangle(0, 0, 32, 32), 1, Material.Bitmap(assetName)) ::
      drawDots(model.center, model.dots)
  ))

  private def drawDots(center: Point, dots: Batch[Dot]): Batch[Graphic[_]] = dots.map { dot =>
    val position = Point(
      (Math.sin(dot.angle.toDouble) * dot.orbitDistance + center.x).toInt,
      (Math.cos(dot.angle.toDouble) * dot.orbitDistance + center.y).toInt
    )
    println(Dot)

    Graphic(Rectangle(0, 0, 32, 32), 1, Material.Bitmap(assetName))
      .withCrop(Rectangle(16, 16, 16, 16)).withRef(8, 8).moveTo(position)
  }

}

case class Model(center: Point, dots: Batch[Dot]) {
  def addDot(dot: Dot): Model = this.copy(dots = dot :: dots)

  def update(timeDelta: Seconds): Model = this.copy(dots = dots.map(_.update(timeDelta)))
}
object Model {
  def initial(center: Point): Model = Model(center, Batch.empty)
}
case class Dot(orbitDistance: Int, angle: Radians) {
  def update(timeDelta: Seconds): Dot = this.copy(angle = angle + Radians.fromSeconds(timeDelta))
}
