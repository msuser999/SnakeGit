package Misc

//Enumit selventävät huomattavasti
object Direction extends Enumeration { //SUUNTA
  val Left, Right, Up, Down = Value
}

object Gamemode extends Enumeration { //PELIMUOTO
  val Classic, ClassicEdge, ClassicCross, ClassicLines, Random, RandomMaze, Drawing= Value
}

object Sound extends Enumeration { //ÄÄNET
  val Tick, Beep, Grow, GameOver, Music = Value
}