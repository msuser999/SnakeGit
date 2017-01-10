package Objects
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

//Esteet ovat myös kokoelma Shape-olioita
class Obstacles {
  var obstacleShapes = ArrayBuffer[Shape]()

  def edge(): Unit = { //LUO REUNOILLE ESTEET
    for (i <- 1 until 41) {
      obstacleShapes += new Shape(i * 10, 10)
      obstacleShapes += new Shape(i * 10, 400)

      if (i != 1 && i != 40) {
        obstacleShapes += new Shape(10, i * 10)
        obstacleShapes += new Shape(400, i * 10)
      }
    }
  }

  def randomObstacle(snakeShapes: ArrayBuffer[Shape], fruit: Fruit): Unit = { //LUODAAN ESTE SATUNNAISEEN PAIKKAAN (RANDOM-OBSTACLES PELIMUODOLLE)
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
      if ((x < head.x + 40 && x > head.x - 40) && (y < head.y + 40 && y > head.y - 40)) { //...estää turhauttavat tilanteet jossa este ilmestyy suoraan eteen
        dontEnd = true
      }

      if (!dontEnd) { //Jos dontEnd ei ole muuttunut, uusi este on sopivassa paikassa
        obstacleShapes += new Shape(x, y) //Lisätään este kokoelmaan
        loop = false //Ja lopetetaan loop
      }
    }
  }
}