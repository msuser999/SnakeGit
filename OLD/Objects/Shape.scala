package Objects

//Mato ja esteet koostuvat Shape-olioista
class Shape(var x: Int, var y: Int) {
  
  def newX(nx: Int): Unit = { //UUSI X-KOORDINAATTI
    x = nx
  }
  
  def newY(ny: Int): Unit = { //UUSI Y-KOORDINAATTI
    y = ny
  }
  
  def collision(shape: Shape): Boolean = { //Katsoo onko parametrin Shape:lla samat koordinaatit
    return (this.x == shape.x) && (this.y == shape.y)
  }
}