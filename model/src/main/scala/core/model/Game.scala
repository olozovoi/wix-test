package core.model

case class Game(board: Board, steps: Int) {
  lazy val status: GameStatus = board.status

  def move(direction: Direction): Either[MoveError, Game] = board.move(direction)
    .map(Game(_, steps + 1))
}
