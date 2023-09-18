package core.services

import core.model.Direction

enum Command {
  case StartNew
  case Quit
  case Move(direction: Direction)
}
