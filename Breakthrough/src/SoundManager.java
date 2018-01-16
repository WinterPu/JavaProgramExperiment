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
	private MediaPlayer backgroundMedia = new MediaPlayer(
			new Media(getClipResourceString("/Resources/Audio/background.mp3")));
	private AudioClip hitOnShipAudio = new AudioClip(getClipResourceString("/Resources/Audio/hitOnShip.mp3"));
	private AudioClip bonusAudio = new AudioClip(getClipResourceString("/Resources/Audio/bonus.wav"));
	private AudioClip winAudio = new AudioClip(getClipResourceString("/Resources/Audio/Win Sound Effects.mp3"));

	
	private boolean flagPlayGameEndAudio;
	
	public SoundManager(){
		flagPlayGameEndAudio = false;
	}
	public void initSoundManager(){
		flagPlayGameEndAudio = false;
	}
	
	public enum SoundType {
		SHOOT, GAMEOVER, HIT_ON_ENEMY, BACKGROUND, DIED, SHIP_BOOM, HIT_ON_SHIP, BONUS, WIN
	}

	public void Play(SoundType type) {
		switch (type) {
		case SHOOT:
			shootAudio.play();
			break;

		case HIT_ON_ENEMY:
			hitOnEnemyAudio.play();
			break;

		case SHIP_BOOM:
			shipBoomAudio.play();
			break;
		case BACKGROUND:
			backgroundMedia.play();
			backgroundMedia.setOnEndOfMedia(new Runnable() {
				@Override
				public void run() {
					backgroundMedia.seek(Duration.ZERO);
				}

			});
			break;

		case DIED:
			diedAudio.play();
			break;

		case HIT_ON_SHIP:
			hitOnShipAudio.play();
			break;

		case BONUS:
			bonusAudio.play();
			break;


		case GAMEOVER:
			if(!flagPlayGameEndAudio)
			{
				gameoverAudio.play();
				flagPlayGameEndAudio = true;
			}
			break;
		case WIN:
			if(!flagPlayGameEndAudio)
			{
				winAudio.play();
				flagPlayGameEndAudio = true;
			}
			break;
		}
	}

	public void stopBGM() {
		backgroundMedia.stop();
	}

	private String getClipResourceString(String clipName) {
		return getClass().getResource(clipName).toString();
	}
}
