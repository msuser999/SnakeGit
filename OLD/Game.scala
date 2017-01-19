import Objects._
import Misc._
import scala.swing._
import scala.collection.mutable.ArrayBuffer
import java.util.TimerTask

//Luokka pelille, täällä kaikki pelin toimintaan liittyvä
class Game() {

  var score = 0
  var snake = new Snake()
  var obstacles = new Obstacles()
  var fruit = new Fruit()
  var gameOver = false 
  var refresh = true
  var paused = true
  var resetPaused = true
  var speed = 1
  var timer = new java.util.Timer
  var timerOn = false
  var panel: Panel = null
  var gamemode = Gamemode.Classic
  var gamemodeTask = classic()

  def giveMode(gm: Gamemode.Value): Unit = { //PELIMUODON MUUTTAMINEN/TIMERTASK:N "REFRESH" UUDELLE AJASTIMELLE
    gamemode = gm //Pelimuoto
    if (gm == Gamemode.Classic) { //Pelimuotoa vastaava TimerTask
      gamemodeTask = classic()
    } else if (gm == Gamemode.ClassicEdge) {
      gamemodeTask = classic()
    } else if (gm == Gamemode.Random) {
      gamemodeTask = randomObstacles()
    }
  }

  def givePanel(p: Panel): Unit = { //Annetaan Main-luokasta Panel tänne grafiikan päivittämiseen
    panel = p
  }

  def startSpeed(level: Int): Unit = { //AJASTIMEN KÄYNNISTYS
    timerOn = true 
    
    if (level == 1) { //Eri nopeuksille eri ajastin, pelimuoto otetaan oliomuuttujasta
      speed = 1
      timer.schedule(gamemodeTask, 0, 190)
    } else if (level == 2) {
      speed = 2
      timer.schedule(gamemodeTask, 0, 110)
    } else if (level == 3) {
      speed = 3
      timer.schedule(gamemodeTask, 0, 75)
    }
  }

  def pause(): Unit = { //PELIN PYSÄYTYS
    if (!gameOver) {
      if (paused) {
        paused = false
      } else {
        paused = true
      }
      panel.repaint() //Piirretään pause-ruudun grafiikka
    }
  }

  def reset(): Unit = { //RESETOINTI
    //Nollataan kaikki
    score = 0
    snake.shapes = ArrayBuffer[Shape](new Shape(210, 210))
    snake.dir = Direction.Right
    obstacles.obstacleShapes.clear()
    if (gamemode == Gamemode.ClassicEdge) { //Reuna-pelimuodon esteiden uudelleenasettaminen
      obstacles.edge()
    }
    
    fruit.newCoords(snake.shapes, obstacles.obstacleShapes) //Uusi hedelmä, pitää kutsua aina viimeisenä jotta se ei mene esteiden/madon sisään

    //Totuusmuuttujat reseoitaan ja peli on pausettu ja resetpausettu (resetpausen avulla estetään pause-grafiikka ja voidaan aloittaa 
    //peli millä tahansa näppäimellä resetin jälkeen
    resetPaused = true
    paused = true
    refresh = true
    gameOver = false
    panel.repaint() //Päivitetään grafiikka
  }

  def classic(): TimerTask = { //CLASSIC-PELIMUOTO
    var timertask = new TimerTask() {
      def run(): Unit = { //TimerTask:n vaatima run-metodi jossa itse toiminta
        
        if (!paused && !gameOver) { //Peli etenee jos peli ei ole pausettu tai se ei ole loppunut

          snake.move() //Edetessä mato aina liikkuu

          if (fruit.collision(snake.head())) { //Jos mato osuu hedelmään...
            fruit.newCoords(snake.shapes, obstacles.obstacleShapes) //Arvotaan hedelmälle uusi koordinaatti
            snake.grow() //Mato kasvaa

            if (speed == 1) { //Pistetys nopeuden mukaan
              score = score + 1
            } else if (speed == 2) {
              score = score + 2
            } else if (speed == 3) {
              score = score + 3
            }
          }

          for (obstacle <- obstacles.obstacleShapes) { //Jos mato osuu esteeseen lopetetaan peli
            if (snake.head().collision(obstacle)) {
              gameOver = true
              refresh = false
            }
          }

          if (snake.selfCollision()) { //Jos mato osuu itseensä lopetetaan peli
            gameOver = true
            refresh = false
          }

          if (refresh) { //Grafiikan päivitys
            panel.repaint()
          }

          if (resetPaused) { //Kun peli alkaa resetin jälkeen laitetaan resetPause:een false jotta pause-ruudun grafiikka ilmestyy j
            resetPaused = false //eikä kaikki näppäimet vaikuta peliin
          }
        }
      }
    }
    return timertask 
  }

  def randomObstacles(): TimerTask = { //RANDOM OBSTACLE - PELIMUOTO
    var timertask = new TimerTask() {
      def run(): Unit = {

        if (snake.shapes.size < 6) { //Jos madon pituus alle 6, se kasvaa 
          snake.grow()
        }

        if (!paused && !gameOver) { //Jos peli ei ole pausettu tai loppu
          snake.move()

          if (fruit.collision(snake.head())) { //Jos mato osuu hedelmään...
            obstacles.randomObstacle(snake.shapes, fruit)
            obstacles.randomObstacle(snake.shapes, fruit)
            obstacles.randomObstacle(snake.shapes, fruit)
            obstacles.randomObstacle(snake.shapes, fruit)
            obstacles.randomObstacle(snake.shapes, fruit) //Luodaan 5 uutta estettä satunnaisiin paikkoihin
            fruit.newCoords(snake.shapes, obstacles.obstacleShapes) //Ja arvotaan hedelmän uusi koordinaatti

            if (speed == 1) { //Pisteytys nopeuden mukaan
              score = score + 1
            } else if (speed == 2) {
              score = score + 2
            } else if (speed == 3) {
              score = score + 3
            }
          }

          //Alla olevat samat kuin classic-muodossa, perustoiminnot
          for (obstacle <- obstacles.obstacleShapes) { 
            if (snake.head().collision(obstacle)) {
              gameOver = true
              refresh = false
            }
          }

          if (snake.selfCollision()) {
            gameOver = true
            refresh = false
          }

          if (refresh) { 
            panel.repaint()
          }

          if (resetPaused) {
            resetPaused = false
          }
        }
      }
    }
    return timertask
  }
}