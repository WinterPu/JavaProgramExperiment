import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundManager {

	private AudioClip shipBoomAudio = new AudioClip(getClipResourceString("/Resources/Audio/shipBoom.mp3"));
	private AudioClip hitOnEnemyAudio = new AudioClip(getClipResourceString("/Resources/Audio/hitOnEnemy.mp3"));
	private AudioClip shootAudio = new AudioClip(getClipResourceString("/Resources/Audio/shoot.mp3"));
	private AudioClip gameoverAudio = new AudioClip(getClipResourceString("/Resources/Audio/gameover.wav"));
	private AudioClip diedAudio = new AudioClip(getClipResourceString("/Resources/Audio/died.wav"));
	private MediaPlayer backgroundMedia = new MediaPlayer(new Media(getClipResourceString("/Resources/Audio/background.mp3")));
	private AudioClip hitOnShipAudio = new AudioClip(getClipResourceString("/Resources/Audio/hitOnShip.mp3"));
	private AudioClip bonusAudio = new AudioClip(getClipResourceString("/Resources/Audio/bonus.wav"));
	
	public enum SoundType {
		shoot, gameover, hitOnEnemy, background, died, shipBoom, hitOnShip, bonus
	}

	public void Play(SoundType type) {
		switch (type) {
		case shoot:
			shootAudio.play();
			break;
			
		case hitOnEnemy:
			hitOnEnemyAudio.play();
			break;
			
		case shipBoom:
			shipBoomAudio.play();
			break;
			
		case gameover:
			gameoverAudio.play();
			break;
			
		case background:
			backgroundMedia.play();
			backgroundMedia.setOnEndOfMedia(new Runnable()
					{
				@Override
				public void run()
				{
					backgroundMedia.seek(Duration.ZERO);
				}
				
			});
			break;
			
		case died:
			diedAudio.play();
			break;
			
		case hitOnShip:
			hitOnShipAudio.play();
			break;
			
		case bonus:
			bonusAudio.play();
			break;
		}
	}

	private String getClipResourceString(String clipName) {
		return getClass().getResource(clipName).toString();
	}
}
