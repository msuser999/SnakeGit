package Misc

//Enumit selventävät toimintaa huomattavasti
object Direction extends Enumeration { //SUUNTA
  val Left, Right, Up, Down = Value
}

object Gamemode extends Enumeration { //PELIMUOTO
  val Classic, ClassicEdge, Random = Value
}