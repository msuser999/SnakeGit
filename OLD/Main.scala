import scala.swing._
import scala.swing.event._

//Itse Swing-applikaatio
object Main extends SimpleSwingApplication { 
  var game = new Game()

  def top = new MainFrame() {
    title = "Snake"

    contents = new Panel() {
      peer.setLayout(null)
      game.givePanel(this)
      focusable = true
      preferredSize = new Dimension(700, 420)

      var graphics = new Graphics() //GRAFIIKKA, toteutus siis Graphics-luokassa
      override def paintComponent(g: Graphics2D) { 
        super.paintComponent(g)
        graphics.sidePanel(g) //Sivupaneeli (SNAKE-teksti)
        graphics.grid(g) //Taustalla oleva ruudukkko
        graphics.objects(game.fruit, game.snake.shapes, game.obstacles.obstacleShapes, g) //Mato, esteet ja hedelmä
        graphics.pause(game, g) //Pause-ruudun grafiikka
        graphics.score(game.score, g) //Pistemäärä
      }

      var kbbtn = new KbAndBtnActions(game, peer) //Toimintoluokka nappuloille (myös niiden asettaminen) ja näppäimistölle

      //Komennot jotka tarvitsee jotta toiminnot ovat voimassa
      listenTo(keys)
      listenTo(kbbtn.resetBtn)
      listenTo(kbbtn.changeBtn)
      listenTo(kbbtn.speedBtnOne)
      listenTo(kbbtn.speedBtnTwo)
      listenTo(kbbtn.speedBtnThree)
      listenTo(kbbtn.gamemodeBtn)
      listenTo(kbbtn.classicBtn)
      listenTo(kbbtn.classicEdgeBtn)
      listenTo(kbbtn.randObstBtn)
      
      reactions += {
        case keyPress: KeyPressed => //Näppäimistö
          kbbtn.kbFunctions(keyPress.key)

        case press: ButtonClicked => //Nappulat
          var source = press.source
          kbbtn.buttonFunctions(source)
      }
    }
  }
}