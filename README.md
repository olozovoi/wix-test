# Run
To run game enter next command in console:
```shell
sbt console/run
```
Or launch `game.console.ConsoleGame` class from **console** module in your IDE.

Game supports 6 command:
- **s**tart - starts new game with regenerated board and reset steps counter
- **q**uit - will print farewell message and exit application
- **u**p - will move empty square up
- **d**own - will move empty square down
- **l**eft - will move empty square left
- **r**ight - will move empty square right

# Project structure
Project consist from 3 modules: **model**, **engine** and **console**.

Modules are presented in order such each next depends on previous

## model
Contains all entities needed to describe game logic

### `Direction`
Enum of 4 possible directions.

### `MoveError`
Case class to represent incorrect attempt to move.

### `GameStatus`
Enum to represent if game is ongoing or finished

### `Board`
Represented as `Vector` with some shuffle of values from 0 to 15, 0 meaning empty cell.

I could've chosen mutable `ArraySeq` for sake of performance, but I wanted solution to be in FP style. But still, from immutable collections `Vector` has the best combination of performances for random lookups and updates.

From representation point of view I could've chosen `Vector[Vector[Int]]`, but that would work only in case of mutable datta structures. With nested `Vector`s `Board#swap` would be much more complex and in some cases (moving up or down)
would involve 2 more updates (update corresponding horizontals and then update final structure).

Also, would be interesting to research if it is possible to provide some type-safe representation, e.g. have possible moves at type level as state as `Board[HList[Up, Left]]` or something like this. But I haven't found fast enough any variant, which would be possible to use, not to mention convenient. So I stayed with `Vector[Int]` version, because it gets work done and implementation isn't hard to read or test.

### `Game`
Simple wrapper of `Board` with step counter.