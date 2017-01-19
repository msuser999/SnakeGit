import Misc._
import scala.swing._
import scala.swing.event.Key
import javax.swing.JPanel
import javax.swing.JOptionPane

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
  var classicCrossBtn = new Button("Classic w/ Cross")
  var classicLinesBtn = new Button("Classic w/ Lines")
  var randObstBtn = new Button("Random Obstacles")
  var randObstMazeBtn = new Button("Random Obstacle Maze")
  var drawingBtn = new Button("Drawing Mode")

  var coordAidBtn = new Button("Coordinate Aid")

  var musicBtn = new Button("MUSIC")
  var soundBtn = new Button("SOUND")
  var helpBtn = new Button("?")

  resetBtn.focusable = false //focusable-komento estää että nappulat voi "aktivoida" (joka estää näppäimstön toiminnot)
  changeBtn.focusable = false
  speedBtnOne.focusable = false
  speedBtnTwo.focusable = false
  speedBtnThree.focusable = false
  gamemodeBtn.focusable = false
  classicBtn.focusable = false
  classicEdgeBtn.focusable = false
  classicCrossBtn.focusable = false
  classicLinesBtn.focusable = false
  randObstBtn.focusable = false
  randObstMazeBtn.focusable = false
  coordAidBtn.focusable = false
  musicBtn.focusable = false
  soundBtn.focusable = false
  helpBtn.focusable = false

  //Nappuloiden asettaminen
  placeButton(resetBtn, 440, 100)

  placeButton(changeBtn, 440, 160)
  placeButton(speedBtnOne, 173, 80)
  placeButton(speedBtnTwo, 173, 110)
  placeButton(speedBtnThree, 173, 140)

  placeButton(gamemodeBtn, 440, 130)
  placeButton(classicBtn, 176, 190)
  placeButton(classicEdgeBtn, 149, 220)
  placeButton(classicCrossBtn, 149, 250)
  placeButton(classicLinesBtn, 149, 280)
  placeButton(randObstBtn, 144, 310)
  placeButton(randObstMazeBtn, 130, 340)
  placeButton(drawingBtn, 158, 370)

  placeButton(coordAidBtn, 440, 350)

  placeButton(musicBtn, 440, 383)
  placeButton(soundBtn, 515, 383)
  placeButton(helpBtn, 650, 383)

  //Jotkut nappulat piilotettu/pois päältä alussa
  resetBtn.enabled = false
  changeBtn.enabled = false
  gamemodeBtn.enabled = false
  classicBtn.visible = false
  classicEdgeBtn.visible = false
  classicCrossBtn.visible = false
  classicLinesBtn.visible = false
  randObstBtn.visible = false
  randObstMazeBtn.visible = false
  drawingBtn.visible = false

  //Nappuloiden taustavärit
  resetBtn.background = java.awt.Color.white
  changeBtn.background = java.awt.Color.white
  speedBtnOne.background = java.awt.Color.white
  speedBtnTwo.background = java.awt.Color.white
  speedBtnThree.background = java.awt.Color.white
  gamemodeBtn.background = java.awt.Color.white
  classicBtn.background = java.awt.Color.white
  classicEdgeBtn.background = java.awt.Color.white
  classicCrossBtn.background = java.awt.Color.white
  classicLinesBtn.background = java.awt.Color.white
  randObstBtn.background = java.awt.Color.white
  randObstMazeBtn.background = java.awt.Color.white
  drawingBtn.background = java.awt.Color.white
  coordAidBtn.background = java.awt.Color.white
  helpBtn.background = java.awt.Color.white

  musicBtn.background = new Color(255, 155, 155)
  soundBtn.background = new Color(255, 155, 155)

  def placeButton(button: Button, x: Int, y: Int): Unit = {
    var p = button.peer
    p.setLocation(x, y)
    p.setSize(p.getPreferredSize())
    peer.add(p)
  }

  var changeToggle = false //"Toggle" nopeuden - ja pelimuotomuutosnappuloille
  var gamemodeToggle = false
  var firstTimeSelection = true //Apumuuttuja, jotta alussa valitaan pelimuoto suoraan nopeuden valitsemisen jälkeen
  var keyListen = false //Tämän avulla näppäimistön toiminnot voidaan poistaa käytöstä (esim. kun nopeutta valitaan)

  var beep = new SoundPlayer(Sound.Beep) //Ääni nappuloiden painamiseeen

  def buttonFunctions(source: AbstractButton): Unit = { // TOIMINNOT NAPPULOILLE
    if (game.soundsOn) { //Nappulan painaminen
      beep.playSound()
    }
    
    if (source == helpBtn) { //INFORMAATIO PELISTÄ 
      JOptionPane.showMessageDialog(null, helpMessage, "Info", JOptionPane.INFORMATION_MESSAGE)
    }

    if (source == musicBtn) { //TAUSTAMUSIIKKI TOGGLE
      if (!game.musicOn) {
        musicBtn.background = new Color(165, 239, 154)
        game.music.playSound()
        game.musicOn = true
      } else {
        musicBtn.background = new Color(255, 155, 155)
        game.music.stopSound()
        game.musicOn = false
      }
    }

    if (source == soundBtn) { //ÄÄNIEFEKTI TOGGLE
      if (game.soundsOn) {
        soundBtn.background = new Color(255, 155, 155)
        game.soundsOn = false
      } else {
        beep.playSound()
        soundBtn.background = new Color(165, 239, 154)
        game.soundsOn = true
      }
    }

    if (source == coordAidBtn) { //KOORDINAATTIAPU
      if (game.coordAidOn) {
        game.coordAidOn = false
      } else {
        game.coordAidOn = true
      }
    }

    if (source == resetBtn) { //RESET
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

    if (source == gamemodeBtn) { //PELIMUOTO TOGGLE
      if (!gamemodeToggle) {
        gamemodeToggle = true
        keyListen = false
        resetBtn.enabled = false
        changeBtn.enabled = false

        classicBtn.visible = true
        classicEdgeBtn.visible = true
        classicCrossBtn.visible = true
        classicLinesBtn.visible = true
        randObstBtn.visible = true
        randObstMazeBtn.visible = true
        drawingBtn.visible = true
      } else {
        gamemodeToggle = false
        keyListen = true
        resetBtn.enabled = true
        changeBtn.enabled = true

        classicBtn.visible = false
        classicEdgeBtn.visible = false
        classicCrossBtn.visible = false
        classicLinesBtn.visible = false
        randObstBtn.visible = false
        randObstMazeBtn.visible = false
        drawingBtn.visible = false
      }
    }

    //Pelimuotonappulat
    if (source == classicBtn || source == classicEdgeBtn || source == classicCrossBtn || source == classicLinesBtn
      || source == randObstBtn || source == randObstMazeBtn || source == drawingBtn) {
      gamemodeButtonAction(source)
    }
  }

  def kbFunctions(source: Key.Value): Unit = { //NÄPPÄIMISTÖN TOIMINTA
    if (keyListen) { //Jos halutaan että näppämistö toimii
      if (source == Key.Enter || source == Key.Space) { //Pause
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
      game.startTimer(1)
    } else if (level == 2) {
      game.startTimer(2)
    } else if (level == 3) {
      game.startTimer(3)
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
      classicCrossBtn.visible = true
      classicLinesBtn.visible = true
      randObstBtn.visible = true
      randObstMazeBtn.visible = true
      drawingBtn.visible = true

      resetBtn.enabled = false
      changeBtn.enabled = false
      gamemodeBtn.enabled = false
    }
  }

  def gamemodeButtonAction(source: AbstractButton): Unit = { //PELIMUODON MUUTOS
    if (game.timerOn) { //Samanlailla kuin nopudenmuutoksessa ajastin pitää ottaa pois päältä
      game.timer.cancel()
      game.timer.purge()
      game.timerOn = false
      game.timer = new java.util.Timer
    }

    if (source == classicBtn) {
      game.giveMode(Gamemode.Classic) //Classic
    } else if (source == classicEdgeBtn) {
      game.giveMode(Gamemode.ClassicEdge) //Reunaeste
    } else if (source == classicCrossBtn) {
      game.giveMode(Gamemode.ClassicCross) //Ristieste
    } else if (source == classicLinesBtn) {
      game.giveMode(Gamemode.ClassicLines) //Viivaeste
    } else if (source == randObstBtn) {
      game.giveMode(Gamemode.Random) ////Uudenlainen pelimuoto, luo satunnaiseen kohtaa esteitä kun syödään hedelmä, eikä mato kasva
    } else if (source == randObstMazeBtn) {
      game.giveMode(Gamemode.RandomMaze)
    } else if (source == drawingBtn) {
      game.giveMode(Gamemode.Drawing)
    }

    game.reset() //Pelin reset

    if (game.speed == 1) { //Riippuen aiemmin valitusta nopeudesta käynnistetään uusi ajastin
      game.startTimer(1) // (siis luodaan uusi Timer, jolla on vanha nopeus, mutta uusi pelimuoto)
    } else if (game.speed == 2) {
      game.startTimer(2)
    } else if (game.speed == 3) {
      game.startTimer(3)
    }

    //Nappuloiden ja näppäimistön aktivoiminen ja pelimuotonappuloiden peittäminen
    keyListen = true
    resetBtn.enabled = true
    changeBtn.enabled = true
    gamemodeBtn.enabled = true

    classicBtn.visible = false
    classicEdgeBtn.visible = false
    classicCrossBtn.visible = false
    classicLinesBtn.visible = false
    randObstBtn.visible = false
    randObstMazeBtn.visible = false
    drawingBtn.visible = false
  }
  
  def helpMessage(): String = {
    var msg = ""
    msg += "Reset - Resetoi peli lähtökohtaan\n\n"
    msg += "Change Gamemode - Muuta pelimuotoa \n      "
    msg += "Classic - Perus matopeli. Ruudulle ilmestyy satunnaisesti ja hetkellisesti iso hedelmä josta saa lisäpisteitä\n      "
    msg += "Classic w/... - Perus matopeli erilaisilla esteillä\n      "
    msg += "Random Obstacles - Aina kun syöt hedelmän, ruudulle ilmestyy 5 estettä satunnaiseen kohtaan\n      "
    msg += "Random Obstacle Maze - Aina kun syöt hedelmän, ruudulle ilmestyy useaan satunnaiseen kohtaa esteitä vanhojen tilalle\n      "
    msg += "Drawing Mode - Ei hedelmiä eikä esteitä, mato ei voi törmätä itseenä ja mato kasvaa loputtomasti. Piirrä kuvioita madolla\n\n"
    msg += "Change Speed - Muuta pelin etenemisnopeutta, 1 hitain, 3 nopein\n\n"
    msg += "Coordinate Aid - Lisää pisteet ruudun reunoille jotka ovat madon pään kohdalla.\n\n"
    msg += "MUSIC & SOUND - Toggle ääni-ja musiikkiefekteille, punainen tarkoittaa että on pois päältä\n\n"
    msg += "Ohjaa matoa nuolinäppäimillä, pauseta peli enterillä tai spacella"
    return msg
  }
}