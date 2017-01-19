import Objects._
import Misc._
import scala.swing._
import scala.collection.mutable.ArrayBuffer
import java.awt.font._
import java.awt.image.BufferedImage
import javax.swing.ImageIcon
import java.io.File
import javax.imageio.ImageIO

//Grafiikan määrittely
class Graphics(game: Game) {

  var eyeImg = ImageIO.read(new File("eye.png"))
  var pieceImg = ImageIO.read(new File("piece.png"))
  var fruitImg = ImageIO.read(new File("fruit.png"))
  var bigFruitImg = ImageIO.read(new File("bigfruit.png"))

  def background(g: Graphics2D) { //Pelialustan tausta
    g.setColor(java.awt.Color.black)
    g.fillRect(10, 10, 400, 400)
  }

  def coordinateAid(g: Graphics2D): Unit = { //LISÄÄ PISTEET RUUDUN REUNOILLE MADON PÄÄN KOHDALLE 
    if (game.coordAidOn) {
      var head = game.snake.head()
      g.setColor(java.awt.Color.white)
      g.fillOval(head.x + 2, 412, 5, 5) //Alaruutu
      g.fillOval(412, head.y + 2, 5, 5) // Oikea puoli
      g.fillOval(head.x + 2, 3, 5, 5) //Yläruutu
      g.fillOval(3, head.y + 2, 5, 5) //Vasen puoli
    }
  }

  def pause(g: Graphics2D): Unit = { //PAUSE-RUUTU
    //Jos peli ei ole loppu, eikä resetoitu mutta on pausettu, eikä 
    if (!game.gameOver && !game.resetPaused && game.paused && game.gamemode != Gamemode.Drawing) { 
      g.setColor(new Color(0, 0, 0, 50))
      g.fillRect(10, 10, 400, 400)
      g.setColor(java.awt.Color.white)
      g.setFont(new Font("Arial", 0, 40))
      g.drawString("PAUSED", 130, 210)
    }
  }

  def sidePanel(g: Graphics2D): Unit = { //SIVUPANEELI
    g.setFont(new Font("Elephant", 0, 50))
    g.setColor(java.awt.Color.red)
    g.drawString("SNAKE", 450, 50)

    g.setFont(new Font("Arial", 0, 15))
    g.setColor(java.awt.Color.white)
    g.drawString("" + game.speed, 560, 178)
  }

  def score(g: Graphics2D): Unit = { //PISTEMÄÄRÄ
    g.setColor(java.awt.Color.white)
    g.setFont(new Font("Arial", 0, 15))
    g.drawString("SCORE: " + game.score, 450, 70)
  }

  def grid(g: Graphics2D): Unit = { //TAUSTARUUDUKKO
    for (x <- 1 until 41) {
      for (y <- 1 until 41) {
        g.setColor(new Color(50, 50, 50))
        g.drawRect(x * 10, y * 10, 10, 10)
      }
    }
  }

  def objects(g: Graphics2D): Unit = { //MATO, ESTEET JA HEDELMÄ

    if (game.bigFruitActive) { //Iso hedelmä
      g.drawImage(bigFruitImg, game.bigFruit.x - 10, game.bigFruit.y - 10, null)
    }

    g.drawImage(fruitImg, game.fruit.x - 10, game.fruit.y - 10, null) //Hedelmä
    for (shape <- game.snake.shapes) { //Mato
      g.drawImage(pieceImg, shape.x - 10, shape.y - 10, null)
    }

    for (o <- game.obstacles.obstacleShapes) { //Esteet
      g.drawImage(pieceImg, o.x - 10, o.y - 10, null)
    }
    g.drawImage(pieceImg, game.snake.head().x - 10, game.snake.head().y - 10, null) //Pää

    if (game.snake.dir == Direction.Left) { //Madon silmät
      g.drawImage(eyeImg, game.snake.head().x - 15, game.snake.head().y - 7, null)
      g.drawImage(eyeImg, game.snake.head().x - 15, game.snake.head().y - 13, null)
    } else if (game.snake.dir == Direction.Right) {
      g.drawImage(eyeImg, game.snake.head().x - 5, game.snake.head().y - 7, null)
      g.drawImage(eyeImg, game.snake.head().x - 5, game.snake.head().y - 13, null)
    } else if (game.snake.dir == Direction.Up) {
      g.drawImage(eyeImg, game.snake.head().x - 14, game.snake.head().y - 15, null)
      g.drawImage(eyeImg, game.snake.head().x - 8, game.snake.head().y - 15, null)
    } else if (game.snake.dir == Direction.Down) {
      g.drawImage(eyeImg, game.snake.head().x - 14, game.snake.head().y - 5, null)
      g.drawImage(eyeImg, game.snake.head().x - 8, game.snake.head().y - 5, null)
    }
  }
}