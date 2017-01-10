import Misc._
import scala.swing._
import scala.swing.event.Key
import javax.swing.JPanel

//Nappuloiden totetutus, asettaminen ja toiminta sekä näppäimistön toiminta
class KbAndBtnActions(var game: Game, var peer: JPanel) { 

  //Nappulat
  var resetBtn = new Button("Reset")
  var changeBtn = new Button("Change Speed")
  var speedBtnOne = new Button("Speed: 1")
  var speedBtnTwo = new Button("Speed: 2")
  var speedBtnThree = new Button("Speed: 3")
  var gamemodeBtn = new Button("Select Gamemode")
  var classicBtn = new Button("Classic")
  var classicEdgeBtn = new Button("Classic w/ Edges")
  var randObstBtn = new Button("Random Obstacles")

  resetBtn.focusable = false //focusable-komento estää että nappulat voi "aktivoida" (joka estää näppäimstön toiminnot)
  changeBtn.focusable = false
  speedBtnOne.focusable = false
  speedBtnTwo.focusable = false
  speedBtnThree.focusable = false
  gamemodeBtn.focusable = false
  classicBtn.focusable = false
  classicEdgeBtn.focusable = false
  randObstBtn.focusable = false
  
  //Nappuloiden asettaminen
  placeButton(resetBtn, 440, 100)
  placeButton(changeBtn, 440, 130)
  placeButton(speedBtnOne, 173, 110)
  placeButton(speedBtnTwo, 173, 140)
  placeButton(speedBtnThree, 173, 170)
  placeButton(gamemodeBtn, 440, 160)
  placeButton(classicBtn, 176, 250)
  placeButton(classicEdgeBtn, 149, 280)
  placeButton(randObstBtn, 144, 310)

  //Jotkut nappulat piilotettu/pois päältä alussa
  resetBtn.enabled = false
  changeBtn.enabled = false
  gamemodeBtn.enabled = false

  classicBtn.visible = false
  classicEdgeBtn.visible = false
  randObstBtn.visible = false

  def placeButton(button: Button, x: Int, y: Int): Unit = { //Huom. button-parametrin voi vaihtaa Component:ksi jos tarvitsee
    var p = button.peer
    p.setLocation(x, y)
    p.setSize(p.getPreferredSize())
    peer.add(p)
  }

  var changeToggle = false //"Toggle" nopeuden - ja pelimuotomuutosnappuloille
  var gamemodeToggle = false //
  var firstTimeSelection = true //Apumuuttuja, jotta alussa valitaan pelimuoto suoraan nopeuden valitsemisen jälkeen
  var keyListen = false //Tämän avulla näppäimistön toiminnot voidaan poistaa käytöstä (esim. kun nopeutta valitaan)

  def buttonFunctions(source: AbstractButton): Unit = { // TOIMINNOT NAPPULOILLE
    if (source == resetBtn) { //Reset
      game.reset()
    }

    if (source == changeBtn) { //NOPEUDENMUUTOS TOGGLE
      if (!changeToggle) {
        changeToggle = true
        keyListen = false
        resetBtn.enabled = false
        gamemodeBtn.enabled = false
        speedBtnOne.visible = true
        speedBtnTwo.visible = true
        speedBtnThree.visible = true
      } else {
        changeToggle = false
        keyListen = true
        resetBtn.enabled = true
        gamemodeBtn.enabled = true
        speedBtnOne.visible = false
        speedBtnTwo.visible = false
        speedBtnThree.visible = false
      }
    }

    if (source == speedBtnOne) { //Nopeustaso 1
      speedButtonAction(1)
    }

    if (source == speedBtnTwo) { //Nopeustaso 2
      speedButtonAction(2)
    }

    if (source == speedBtnThree) { //Nopeustaso 3
      speedButtonAction(3)
    }

    def speedButtonAction(level: Int): Unit = { //NOPEUDENMUUTOS
      if (game.timerOn) { //Sulkee ajastimen jos se on päällä, aina kun muuttaa ajastimen toimintaa pitää 
        game.timer.cancel() // sulkea vanha, luoda uusi Timer ja TimerTask ja vasta sitten voi käynnistää sen uudelleen
        game.timer.purge()
        game.timerOn = false 
        game.timer = new java.util.Timer
      }
      game.giveMode(game.gamemode) //Luodaan uusi TimerTask
      game.reset() //Peli resetoidaan

      
      if (level == 1) { //Tässä käynnistetään uusi ajastin halutulla nopeudella 
        game.startSpeed(1)
      } else if (level == 2) {
        game.startSpeed(2)
      } else if (level == 3) {
        game.startSpeed(3)
      }


      //Näppäimistö toimii taas ja tietyt nappulat tulevat taas toiminnollisiksi, sekä nopeudenmuutosnappulat peitetään
      keyListen = true
      speedBtnOne.visible = false
      speedBtnTwo.visible = false
      speedBtnThree.visible = false
      resetBtn.enabled = true
      changeBtn.enabled = true
      gamemodeBtn.enabled = true
      if (firstTimeSelection) { //Siis jos peli aloitetaan ensimmäistä kertaa, siirrytään suoraan valitsemaan pelimuoto
        firstTimeSelection = false //(jolloin pitää peittää/ottaa pois käytöstä joitain nappulota ja estää näppäimistötoiminnot)
        keyListen = false
        classicBtn.visible = true
        classicEdgeBtn.visible = true
        randObstBtn.visible = true
        resetBtn.enabled = false
        changeBtn.enabled = false
        gamemodeBtn.enabled = false
      }
    }

    if (source == gamemodeBtn) { //PELIMUOTO TOGGLE
      if (!gamemodeToggle) {
        gamemodeToggle = true
        keyListen = false
        resetBtn.enabled = false
        changeBtn.enabled = false
        classicBtn.visible = true
        classicEdgeBtn.visible = true
        randObstBtn.visible = true
      } else {
        gamemodeToggle = false
        keyListen = true
        resetBtn.enabled = true
        changeBtn.enabled = true
        classicBtn.visible = false
        classicEdgeBtn.visible = false
        randObstBtn.visible = false
      }
    }

    if (source == classicBtn) { //Classic (mato kasvaa kun syödään hedelmä jne.
      gamemodeButtonAction(Gamemode.Classic)
    }

    if (source == classicEdgeBtn) { //Sama kuin classic, mutta reunoilla esteet
      gamemodeButtonAction(Gamemode.ClassicEdge)
    }
    if (source == randObstBtn) { //Uudenlainen pelimuoto, luo satunnaiseen kohtaa esteitä kun syödään hedelmä, eikä mato kasva
      gamemodeButtonAction(Gamemode.Random) 
    }

    def gamemodeButtonAction(gamemode: Gamemode.Value): Unit = { //PELIMUODON MUUTOS
      if (game.timerOn) { //Samanlailla kuin nopudenmuutoksessa ajastin pitää ottaa pois päältä
        game.timer.cancel()
        game.timer.purge()
        game.timerOn = false
        game.timer = new java.util.Timer
      }

      if (gamemode.equals(Gamemode.Classic)) { //Annetaan Game-luokalle haluttu pelimuoto
        game.giveMode(Gamemode.Classic)
      } else if (gamemode.equals(Gamemode.ClassicEdge)) {
        game.giveMode(Gamemode.ClassicEdge)
      } else if (gamemode.equals(Gamemode.Random)) {
        game.giveMode(Gamemode.Random)
      }
      
      game.reset() //Pelin reset

      if (game.speed == 1) { //Riippuen aiemmin valitusta nopeudesta käynnistetään uusi ajastin
        game.startSpeed(1) // (siis luodaan uusi Timer, jolla on vanha nopeus, mutta uusi pelimuoto)
      } else if (game.speed == 2) {
        game.startSpeed(2)
      } else if (game.speed == 3) {
        game.startSpeed(3)
      }

      //Nappuloiden ja näppäimistön aktivoiminen ja pelimuotonappuloiden peittäminen
      keyListen = true
      resetBtn.enabled = true
      changeBtn.enabled = true
      gamemodeBtn.enabled = true
      classicBtn.visible = false
      classicEdgeBtn.visible = false
      randObstBtn.visible = false
    }
  }

  def kbFunctions(source: Key.Value): Unit = { //NÄPPÄIMISTÖN TOIMINTA
    if (keyListen) { //Jos halutaan että näppämistö toimii
      if (source == Key.Space) { //Spacella pausetetaan
        game.pause()
      }
      if (game.snake.moveBoolean) { //moveBoolean:lla estetään useat nopeudenmuutokset
        if (source == Key.Left || source == Key.Right || source == Key.Up || source == Key.Down) {
          game.snake.newDirection(source, game.resetPaused)
          game.snake.moveBoolean = false
        }
      }
      if (game.resetPaused) { //Jos peli on resetoitu, minkä tahansa nappulan painaminen aloittaa pelin
        game.pause()
      }
    }
  }
}