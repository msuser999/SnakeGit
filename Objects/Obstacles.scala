package Objects
import Misc._
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

//Esteet ovat myös kokoelma Shape-olioita
class Obstacles {
  var obstacleShapes = ArrayBuffer[Shape]()

  def edge(): Unit = { //REUNAESTEET
    for (i <- 1 until 41) {
      obstacleShapes += new Shape(i * 10, 10)
      obstacleShapes += new Shape(i * 10, 400)

      if (i != 1 && i != 40) {
        obstacleShapes += new Shape(10, i * 10)
        obstacleShapes += new Shape(400, i * 10)
      }
    }
  }

  def cross(): Unit = { //RISTIESTE
    for (i <- 20 to 22) {
      for (o <- 1 to 40) {
        obstacleShapes += new Shape(i * 10, o * 10)
        if (o < 20 || o > 22) {
          obstacleShapes += new Shape(o * 10, i * 10)
        }
      }
    }
  }

  def lines(): Unit = { //VIIVAESTE
    for (i <- 1 to 40) {
      obstacleShapes += new Shape(i * 10, 10)
      obstacleShapes += new Shape(i * 10, 400)
      if (i < 19 || i > 23) {
        obstacleShapes += new Shape(i * 10, 100)
        obstacleShapes += new Shape(i * 10, 200)
        obstacleShapes += new Shape(i * 10, 300)
      }
    }
  }

  def randomObstacle(snakeShapes: ArrayBuffer[Shape], fruit: Fruit, randObstTwo: Boolean): Unit = { //LUODAAN ESTE SATUNNAISEEN PAIKKAAN (RANDOM-OBSTACLES PELIMUODOLLE)
    var loop = true
    while (loop) { //Koordinaattien arpominen jatkuu kunnes loop-muuttuja laitetaan false:ksi
      var x = (Random.nextInt(40) + 1) * 10 //Arvotaan uudet koordinaatit
      var y = (Random.nextInt(40) + 1) * 10

      var dontEnd = false //Tämä muuttuu, jos joko madon osalla, esteellä tai hedelmällä on sama koordinaatit kuin äsken arvotut
      for (s <- snakeShapes) { //Mato
        if (x == s.x && y == s.y) {
          dontEnd = true
        }
      }
      for (o <- obstacleShapes) { //Esteet
        if (x == o.x && y == o.y) {
          dontEnd = true
        }
      }
      if (x == fruit.x && y == fruit.y) { //Hedelmä
        dontEnd = true
      }

      var head = snakeShapes(snakeShapes.size - 1) //Jos uuden esteen koordinaatit ovat liian lähellä madon päätä, sitä ei hyväksytä
      if (!randObstTwo) {
        if ((x < head.x + 40 && x > head.x - 40) && (y < head.y + 40 && y > head.y - 40)) { //...estää turhauttavat tilanteet jossa este ilmestyy suoraan eteen
          dontEnd = true
        }
      } else {
        if ((x < head.x + 50 && x > head.x - 50) && (y < head.y + 50 && y > head.y - 50)) { //RandomTwo gamemodessä vielä isompi väli jolle uudet esteet eivät ilmesty
          dontEnd = true
        }
      }

      if (!dontEnd) { //Jos dontEnd ei ole muuttunut, uusi este on sopivassa paikassa
        obstacleShapes += new Shape(x, y) //Lisätään este kokoelmaan
        loop = false //Ja lopetetaan loop
      }
    }
  }
}