package Misc
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

//Luokka äänien soittamiseen 
class SoundPlayer(var sound: Sound.Value) {

  var filepath = "sounds/" //Äänitiedostot
  if (sound == Sound.Tick) {
    filepath += "tick.wav"
  } else if (sound == Sound.Beep) {
    filepath += "beep.wav"
  } else if (sound == Sound.Grow) {
    filepath += "grow.wav"
  } else if (sound == Sound.GameOver) {
    filepath += "gameover.wav"
  } else if (sound == Sound.Music) {
    filepath += "music.wav"
  }

  var ais = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());
  var clip = AudioSystem.getClip();
  clip.open(ais)

  def playSound(): Unit = {
    ais = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile()); //ais ja clip pitää "päivittää" uusilla jotta äänen voi soittaa uudelleen
    clip = AudioSystem.getClip();
    clip.open(ais)
    clip.start()
  }

  def stopSound(): Unit = { //Pysäyttää äänen
    clip.stop()
  }
}