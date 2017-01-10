package Objects
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

//Pelin hedelmä 
class Fruit() {
  var x = 0
  var y = 0
  
  def newCoords(shapes: ArrayBuffer[Shape], obstacles: ArrayBuffer[Shape]) { //HEDELMÄN UUDEN PAIKAN ARPOMINEN
    var loop = true
    while (loop == true) {
      var newX = (Random.nextInt(40) + 1)* 10
      var newY = (Random.nextInt(40)+ 1) * 10

      var dontEnd = false //Jos hedelmä on madon tai jonkin esteen kohdalla, tämä muuttuu
      for (s <- shapes) { //Mato
        if (s.x == newX && s.y == newY) {
          dontEnd = true
        }
      }
      for (o <- obstacles) { //Esteet
        if (o.x == newX && o.y == newY) {
          dontEnd = true
        }
      }
      if (dontEnd == false) { //Jos hedelmä ei osunut madon tai esteen kohdalle, paikka on sopiva ja muutetaan hedelmän koordinaatit
        loop = false
        this.x = newX
        this.y = newY
      }
    }
  }

  def collision(shape: Shape): Boolean = {
    return (this.x == shape.x && this.y == shape.y)
  }
}