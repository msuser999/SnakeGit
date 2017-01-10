package Objects
import Misc._
import scala.swing.event.Key
import scala.collection.mutable.ArrayBuffer

//Mato eli kokelma Shape-olioita, täällä madon toteutus
class Snake {
  var shapes = ArrayBuffer[Shape](new Shape(210, 210))
  var dir = Direction.Right
  var growBoolean = true
  var moveBoolean = true

  def move(): Unit = { //MADON LIIKUTTAMINEN
    if (dir == Direction.Left) { //Liikkumisen suunta rippuu madon suunnasta
      shapes += new Shape(head().x - 10, head().y)

    } else if (dir == Direction.Right) {
      shapes += new Shape(head().x + 10, head().y)

    } else if (dir == Direction.Up) {
      shapes += new Shape(head().x, head().y - 10)

    } else if (dir == Direction.Down) {
      shapes += new Shape(head().x, head().y + 10)
    }

    if (!growBoolean && shapes.size > 3) { //Jos matoa ei ole kutsuttu kasvamaan tai pituus on yli 3, poistetaan häntä
      shapes.remove(0) //Siis mato kasvaa, jos häntää ei poisteta
    }
    growBoolean = false //Kasvu ei kestä kuin yhden liikkumisen verran
    moveBoolean = true //Vasta lliikumisen jälkeen suuntaa voi muuttaa (moveBoolean menee false:ksi suunnanmuutoksessa)

    ifOut()
  }

  def grow(): Unit = { //MADON KASVAMINEN
    growBoolean = true //Tapahtuu siis vasta kun mato liikkuu
  }

  def newDirection(newDir: Key.Value, resetPaused: Boolean): Unit = { //SUUNNANMUUTOS
    if (resetPaused) { //Kun mato on resetoitu, voidaan liikkua mihin tahansa suuntaan
      if (newDir == Key.Left) {
        dir = Direction.Left
      } else if (newDir == Key.Right) {
        dir = Direction.Right
      } else if (newDir == Key.Up) {
        dir = Direction.Up
      } else if (newDir == Key.Down) {
        dir = Direction.Down
      }
    } else { //Muuten pitää estää suunnan muutoksen vastasuuntaan (jotta mato ei liiku itseensä)
      if (newDir == Key.Left && dir != Direction.Right) {
        dir = Direction.Left
      } else if (newDir == Key.Right && dir != Direction.Left) {
        dir = Direction.Right
      } else if (newDir == Key.Up && dir != Direction.Down) {
        dir = Direction.Up
      } else if (newDir == Key.Down && dir != Direction.Up) {
        dir = Direction.Down
      }
    }
  }

  def selfCollision(): Boolean = { //OSUUKO MATO ITSEENSÄ
    for (i <- 0 until shapes.size - 1) { //Käydään läpi kaikki madon osat, ja katsotaan onko päällä ja jollain osalla samat koordinaatit
      if (head().collision(shapes(i))) {
        return true
      }
    }
    return false
  }

  def ifOut(): Unit = { //JOS MATO MENEE ULOS RAJOISTA, ASETETAAN PÄÄ RUUDUN TOISEEN PÄÄHÄN
    if (head().x < 10) {
      head().newX(400)
    }
    if (head().x > 400) {
      head().newX(10)
    }
    if (head().y < 10) {
      head().newY(400)
    }
    if (head().y > 400) {
      head().newY(10)
    }
  }

  def head(): Shape = { //MADON PÄÄ
    return shapes(shapes.size - 1) //Jostain syystä pelkkä oliomuuttuja päälle ei toiminut halutusti
  }

}