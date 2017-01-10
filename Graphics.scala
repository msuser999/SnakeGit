import Objects._
import scala.swing._
import scala.collection.mutable.ArrayBuffer
import java.awt.font._

//Grafiikan määrittely
class Graphics() {

  def sideHelper(head: Shape, g: Graphics2D): Unit = { //LISÄÄ PISTEET RUUDUN REUNOILLE MADON PÄÄN KOHDALLE 
    g.setColor(java.awt.Color.black)
    g.fillOval(head.x, 414, 5, 5)
    g.fillOval(414, head.y, 5, 5)
    g.fillOval(head.x, 1, 5, 5)
    g.fillOval(1, head.y, 5, 5)
  }

  def pause(game: Game, g: Graphics2D): Unit = { //PAUSE-RUUTU
    if (!game.gameOver && !game.resetPaused && game.paused) { //Jos peli ei ole loppu, eikä resetoitu mutta on pausettu
      g.setColor(new Color(0, 0, 0, 50))
      g.fillRect(10, 10, 400, 400)

      g.setColor(java.awt.Color.white)
      g.setFont(new Font("Arial", 0, 40))
      g.drawString("PAUSED", 130, 230)
    }
  }

  def sidePanel(g: Graphics2D): Unit = { //SIVUPANEELI
    g.setColor(java.awt.Color.red)

    g.setFont(new Font("Elephant", 0, 50))
    g.drawString("SNAKE", 450, 50)
  }

  def score(score: Int, g: Graphics2D): Unit = { //PISTEMÄÄRÄ
    g.setColor(java.awt.Color.black)
    g.setFont(new Font("Arial", 0, 15))
    g.drawString("SCORE: " + score, 450, 70)
  }

  def grid(g: Graphics2D): Unit = { //TAUSTARUUDUKKO
    for (x <- 1 until 41) {
      for (y <- 1 until 41) {
        g.setColor(new Color(220, 220, 220))
        g.drawRect(x * 10, y * 10, 10, 10)
      }
    }
  }

  def objects(fruit: Fruit, snake: ArrayBuffer[Shape], obstacles: ArrayBuffer[Shape], g: Graphics2D): Unit = { //MATO, ESTEET JA HEDELMÄ
    g.setColor(java.awt.Color.red)
    g.fillRect(fruit.x, fruit.y, 10, 10)

    g.setColor(java.awt.Color.black)
    for (shape <- snake) {
      g.fillRect(shape.x, shape.y, 10, 10)
    }

    g.setColor(java.awt.Color.gray)
    for (o <- obstacles) {
      g.fillRect(o.x, o.y, 10, 10)
    }

    g.setColor(java.awt.Color.blue)
    g.fillRect(snake(snake.size - 1).x, snake(snake.size - 1).y, 10, 10)
  }
}