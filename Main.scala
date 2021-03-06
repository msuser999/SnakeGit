import scala.swing._
import scala.swing.event._

//Itse Swing-applikaatio
object Main extends SimpleSwingApplication {
  def top = new MainFrame() {
    title = "Snake"
    contents = new Panel() {
      var game = new Game(this)
      peer.setLayout(null)
      focusable = true
      preferredSize = new Dimension(700, 420)
      background = new Color(30, 30, 30)

      var graphics = new Graphics(game) //GRAFIIKKA, toteutus siis Graphics-luokassa
      override def paintComponent(g: Graphics2D) {
        super.paintComponent(g)
        graphics.background(g) //Pelialustan tausta
        graphics.coordinateAid(g) //Koordinaatti apupisteet ruudun reunoille
        graphics.sidePanel(g) //Sivupaneeli (SNAKE-teksti)
        graphics.grid(g) //Taustalla oleva ruudukkko
        graphics.objects(g) //Mato, esteet ja hedelmä
        graphics.pause(g) //Pause-ruudun grafiikka
        graphics.score(g) //Pistemäärä
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
      listenTo(kbbtn.classicCrossBtn)
      listenTo(kbbtn.classicLinesBtn)
      listenTo(kbbtn.randObstBtn)
      listenTo(kbbtn.randObstMazeBtn)
      listenTo(kbbtn.drawingBtn)

      listenTo(kbbtn.coordAidBtn)

      listenTo(kbbtn.musicBtn)
      listenTo(kbbtn.soundBtn)
      listenTo(kbbtn.helpBtn)

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