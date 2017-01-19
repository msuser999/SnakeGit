import Objects._
import Misc._
import scala.swing._
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import java.util.TimerTask

//Luokka pelille, täällä kaikki pelin toimintaan liittyvä
class Game(p: Panel) {

  var snake = new Snake()
  var obstacles = new Obstacles()
  var fruit = new Fruit(false)
  var bigFruit = new Fruit(true)
  var bigFruitActive = false

  var score = 0
  var paused = true
  var resetPaused = true
  var gameOver = false
  var refresh = true
  var timer = new java.util.Timer
  var timerOn = false
  var soundsOn = false
  var musicOn = false
  var coordAidOn = false
  var panel: Panel = null
  var speed = 1
  var gamemode = Gamemode.Classic
  var gamemodeTask = classic()

  panel = p

  def giveMode(gm: Gamemode.Value): Unit = { //PELIMUODON MUUTTAMINEN/TIMERTASK:N "REFRESH" UUDELLE AJASTIMELLE
    gamemode = gm
    if (gm == Gamemode.Classic || gm == Gamemode.ClassicEdge || gm == Gamemode.ClassicCross || gm == Gamemode.ClassicLines) { //Pelimuotoa vastaava TimerTask
      gamemodeTask = classic()
    } else if (gm == Gamemode.Random || gm == Gamemode.RandomMaze) {
      gamemodeTask = randomObstacles()
    } else if (gm == Gamemode.Drawing) {
      gamemodeTask = drawing()
    }
  }

  def startTimer(level: Int): Unit = { //AJASTIMEN KÄYNNISTYS
    timerOn = true
    if (level == 1) { //Eri nopeuksille eri ajastin, pelimuoto otetaan oliomuuttujasta
      speed = 1
      timer.schedule(gamemodeTask, 0, 110)
    } else if (level == 2) {
      speed = 2
      timer.schedule(gamemodeTask, 0, 75)
    } else if (level == 3) {
      speed = 3
      timer.schedule(gamemodeTask, 0, 55)
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
    snake.dir = Direction.Right
    obstacles.obstacleShapes.clear()
    if (gamemode == Gamemode.ClassicEdge) { //Pelimuodon esteiden uudelleenasettaminen
      obstacles.edge()
    } else if (gamemode == Gamemode.ClassicCross) {
      obstacles.cross()
    } else if (gamemode == Gamemode.ClassicLines) {
      obstacles.lines()
    }
    if (gamemode != Gamemode.Drawing) {
      snake.shapes = ArrayBuffer[Shape](new Shape(90, 120)) //Madon resetointi
      fruit.newCoords(snake.shapes, obstacles.obstacleShapes) //Uusi hedelmä, pitää kutsua aina viimeisenä jotta se ei mene esteiden/madon sisään
    } else {
      snake.shapes = ArrayBuffer[Shape](new Shape(10, 400)) //Piirtämismodessa madon pää ruudun kulmassa
      fruit = new Fruit(false)
    }
    if (!music.clip.isActive() && gameOver && musicOn) { //Taustamusiikin uudelleenaloitus, jos peli päättyi ja musiikki on laitettu päälle
      music.playSound()
    }

    //Totuusmuuttujat reseoitaan ja peli on pausettu ja resetpausettu (resetpausen avulla estetään pause-grafiikka ja voidaan aloittaa 
    //peli millä tahansa näppäimellä resetin jälkeen
    resetPaused = true
    paused = true
    refresh = true
    gameOver = false
    bigFruitActive = false
    bigFruitTimerInt = 0
    panel.repaint() //Päivitetään grafiikka
  }

  var tick = new SoundPlayer(Sound.Tick)
  var grow = new SoundPlayer(Sound.Grow)
  var over = new SoundPlayer(Sound.GameOver)
  var music = new SoundPlayer(Sound.Music)

  var bigFruitTimerInt = 0 //"ajastin" isolle hedelmälle (iso hedelmä katoaa ruudulta tietyn ajan jälkeen)

  def classic(): TimerTask = { //CLASSIC-PELIMUOTO
    var timertask = new TimerTask() {
      def run(): Unit = { //TimerTask:n vaatima run-metodi jossa itse toiminta

        if (!paused && !gameOver) { //Peli etenee jos peli ei ole pausettu tai se ei ole loppunut

          if (!bigFruitActive) { //Jos iso hedelmä ei ole ruudulla...
            var randInt = Random.nextInt(450)
            if (randInt == 0) { //1/500 todennäk. että iso hedelmä tulee ruudulle 
              bigFruitActive = true
              bigFruit.newCoords(snake.shapes, obstacles.obstacleShapes)
              bigFruitTimerInt = 0
            }
          } else if (bigFruitActive) { //"ajastin"
            bigFruitTimerInt = bigFruitTimerInt + 1
          }

          if (bigFruitTimerInt == 40) { //"ajastin" päättyy
            bigFruitActive = false
          }

          snake.move() //Edetessä mato aina liikkuu
          if (soundsOn) { //Liikkumisääni
            tick.playSound()
          }

          if (fruit.collision(snake.head())) { //Jos mato osuu hedelmään...
            fruit.newCoords(snake.shapes, obstacles.obstacleShapes) //Arvotaan hedelmälle uusi koordinaatti
            snake.grow() //Mato kasvaa
            if (soundsOn) { //Kasvuääni
              grow.playSound()
            }

            if (speed == 1) { //Pistetys nopeuden mukaan
              score = score + 1
            } else if (speed == 2) {
              score = score + 2
            } else if (speed == 3) {
              score = score + 3
            }
          }

          if (bigFruitActive && bigFruit.collision(snake.head())) {
            bigFruitActive = false
            bigFruit.newCoords(snake.shapes, obstacles.obstacleShapes) //Arvotaan hedelmälle uusi koordinaatti
            snake.grow() //Mato kasvaa
            if (soundsOn) { //Kasvuääni
              grow.playSound()
            }

            if (speed == 1) { //Pistetys nopeuden mukaan
              score = score + 10
            } else if (speed == 2) {
              score = score + 20
            } else if (speed == 3) {
              score = score + 30
            }
          }

          checkForGameOver() //Tarkistaa onko peli loppunut
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

          if (soundsOn) { //Liikkumisääni
            tick.playSound()
          }

          if (fruit.collision(snake.head())) { //Jos mato osuu hedelmään...

            if (gamemode == Gamemode.Random) {
              for (i <- 0 to 4) { //Luodaan 5 uutta estettä satunnaisiin paikkoihin
                obstacles.randomObstacle(snake.shapes, fruit, false)
              }

            } else if (gamemode == Gamemode.RandomMaze) {
              obstacles.obstacleShapes.clear() // Monta uutta estettä, joka kerralla vanhat poistetaan
              for (i <- 0 to 60) {
                obstacles.randomObstacle(snake.shapes, fruit, true)
              }
            }
            fruit.newCoords(snake.shapes, obstacles.obstacleShapes) //Uusi hedelmä

            if (soundsOn) { //Kasvuääni
              grow.playSound()
            }

            if (speed == 1) { //Pisteytys nopeuden mukaan
              score = score + 1
            } else if (speed == 2) {
              score = score + 2
            } else if (speed == 3) {
              score = score + 3
            }
          }

          checkForGameOver() //Tarkistaa onko peli loppunut
        }
      }
    }
    return timertask
  }

  def drawing(): TimerTask = { //Piirrustusmode, ei törmäyksia ja mato kasvaa loputtomasti
    var timertask = new TimerTask() {
      def run(): Unit = {

        if (!paused && !gameOver) {

          snake.grow()
          snake.move()
          if (soundsOn) {
            tick.playSound()
          }
          if (resetPaused) {
            resetPaused = false
          }
          panel.repaint()
        }
      }
    }
    return timertask
  }

  def checkForGameOver(): Unit = {
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

    if (refresh) { //Grafiikan päivitys, jos peli on loppunut eli refresh on fals, grafiikkaa ei päivitetä
      panel.repaint()
    }

    if (resetPaused) { //Kun peli alkaa resetin jälkeen laitetaan resetPause:een false jotta pause-ruudun grafiikka ilmestyy j
      resetPaused = false //eikä kaikki näppäimet vaikuta peliin
    }

    if (gameOver) { //Gameover-ääni ja taustamusiikin pysäytys
      if (musicOn) {
        over.playSound()
        music.stopSound()
      }
    }
  }
}