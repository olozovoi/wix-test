import indigo.*
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("IndigoGame") // Pandering to mdoc
object HelloIndigo extends IndigoSandbox[Unit, Unit] {

  val config: GameConfig = GameConfig.default

  val animations: Set[Animation] = Set()

  private val emoji = AssetName("emoji")

  val assets: Set[AssetType] = Set(AssetType.Image(emoji, AssetPath("assets/emoji.png")))

  val fonts: Set[FontInfo] = Set()

  val shaders: Set[Shader] = Set()

  def setup(assetCollection: AssetCollection, dice: Dice): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def initialModel(startupData: Unit): Outcome[Unit] = Outcome(())

  def updateModel(context: FrameContext[Unit], model: Unit): GlobalEvent => Outcome[Unit] =
    _ => Outcome(())

  def present(context: FrameContext[Unit], model: Unit): Outcome[SceneUpdateFragment] =
    Outcome(SceneUpdateFragment.empty)

}
