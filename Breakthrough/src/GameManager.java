import java.util.ArrayList;
import java.util.Iterator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameManager extends Application {

	Character ship;
	Character background;
	Character bonus;
	AnchorPane pane;

	ArrayList<Character> aliens = new ArrayList<Character>();
	ArrayList<Character> bullets = new ArrayList<Character>();
	ArrayList<Character> flames = new ArrayList<Character>();
	ArrayList<Character> bombs = new ArrayList<Character>();
	ArrayList<Character> hearts = new ArrayList<Character>();

	SoundManager soundManager = new SoundManager();

	long lastTime = 0, currentTime = 0, elapsedTime = 0;
	final double BASE_SPEED = 50f;
	final double SHIP_BASE_SPEED = 800f;
	double acceleration = 0.0;

	public enum shootType {
		NORMAL, STRENGTHED, MAXPOWER
	};

	shootType shootMode = shootType.NORMAL;

	boolean flagGameOver = false;

	@Override
	public void start(Stage primaryStage) {
		pane = new AnchorPane();
		pane.setOnKeyPressed(e -> keyStrike(e));
		initGame();
		soundManager.Play(SoundManager.SoundType.background);

		Scene scene = new Scene(pane, background.getWidth(), background.getHeight());
		primaryStage.setScene(scene);
		primaryStage.show();
		pane.requestFocus();

		lastTime = System.currentTimeMillis();
		timer.start();
	}

	private void initGame() {
		acceleration = BASE_SPEED * 2.0;
		shootMode = shootType.NORMAL;
		flagGameOver = false;
		initCharacters();
	}

	private void initCharacters() {
		pane.getChildren().clear();

		background = new Character("/Resources/Images/bg_image", 1, new int[] { 10 });
		background.SetBoundary(0, background.getWidth(), 0, background.getHeight());
		pane.getChildren().add(background.getView());

		ship = new Character("/Resources/Images/spaceship", 1, new int[] { 10 });
		ship.SetBoundary(0, background.getWidth(), 0, background.getHeight());
		ship.setVX(BASE_SPEED);
		ship.setPosition(background.getHeight() / 2.0, background.getHeight() - ship.getHeight());
		ship.setHP(5);
		pane.getChildren().add(ship.getView());

		aliens.clear();
		for (int n = 0; n < 10; n++) {
			int[] durs = new int[15];
			for (int i = 0; i < durs.length; i++)
				durs[i] = 50 + (int) (50 * Math.random() + 1);

			Character one = new Character("/Resources/Images/SkeltonFrame", 15, durs);
			one.SetBoundary(0, background.getWidth(), 0, background.getHeight());
			one.setVX(BASE_SPEED * (Math.random() + 1));
			one.setPosition(n * 80, Math.random() * 300);
			one.setHP(5);
			aliens.add(one);
			pane.getChildren().add(one.getView());
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

	private void keyStrike(KeyEvent e) {
		if (ship != null) {

			if (e.getCode() == KeyCode.LEFT && ship.getVX() < 0) {
				ship.setVX(ship.getVX() - acceleration);
			} else if (e.getCode() == KeyCode.RIGHT && ship.getVX() > 0) {
				ship.setVX(ship.getVX() + acceleration);
			} else if (e.getCode() == KeyCode.LEFT)
				ship.setVX(-SHIP_BASE_SPEED);
			else if (e.getCode() == KeyCode.RIGHT)
				ship.setVX(SHIP_BASE_SPEED);
			if (e.getCode() == KeyCode.SPACE) {
				shoot(shootMode);
			}
		}
	}

	AnimationTimer timer = new AnimationTimer() {

		@Override
		public void handle(long now) {
			// TODO Auto-generated method stub
			currentTime = System.currentTimeMillis();
			elapsedTime = currentTime - lastTime;

			updateAll(elapsedTime);
			
			
			ship.setVX(0);
			//gameplay(elapsedTime);
			
			
			reclaimCharacters();

			lastTime = currentTime;

			try {
				Thread.sleep((long) (10));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	};

	public void updateAll(long elapsedTime) {
		double widthBkg = background.getWidth();

		background.update(elapsedTime);

		if (ship != null)
			ship.update(elapsedTime);
		if (bonus != null)
			bonus.update(elapsedTime);

		for (Character alien : aliens) {
			alien.update(elapsedTime);
			double x = alien.getCharacterX();
			if (x <= 0 || x + alien.getWidth() >= widthBkg)
				alien.setVX(-alien.getVX());
		}
		for (Character bullet : bullets) {
			bullet.update(elapsedTime);
		}
		for (Character heart : hearts) {
			heart.update(elapsedTime);
		}
		for (Character flame : flames) {
			if (flame.currentFrameNum >= 0)
				flame.update(elapsedTime);
		}

		for (Character bomb : bombs) {
			bomb.update(elapsedTime);
		}
	}


	
	
	private void reclaimCharacters() {
		for (Iterator<Character> it = bullets.iterator(); it.hasNext();) {
			Character bullet = it.next();
			if (!bullet.alive) {
				pane.getChildren().remove(bullet.getView());
				it.remove();
			}
		}
		for (Iterator<Character> it = aliens.iterator(); it.hasNext();) {
			Character alien = it.next();
			if (!alien.alive) {
				pane.getChildren().remove(alien.getView());
				it.remove();
			}
		}
		for (Iterator<Character> it = flames.iterator(); it.hasNext();) {
			Character flame = it.next();
			if (!flame.alive) {
				pane.getChildren().remove(flame.getView());
				it.remove();
			}
		}

		for (Iterator<Character> it = bombs.iterator(); it.hasNext();) {
			Character bomb = it.next();
			if (!bomb.alive) {
				pane.getChildren().remove(bomb.getView());
				it.remove();
			}
		}

		if (ship != null && !ship.alive) {
			pane.getChildren().remove(ship.getView());
			ship = null;
		}

		if (bonus != null && !bonus.alive) {
			pane.getChildren().remove(bonus.getView());
			bonus = null;
		}
	}

	void shoot(shootType mode) {

		double x = ship.getCharacterX();
		double y = ship.getCharacterY();

		switch (mode) {
		case NORMAL: {
			if (bullets.size() < 6) {
				Character bullet = new Character("Resources/Images/bullet", 1, new int[] { 10 });
				bullet.position.addListener((ev) -> checkBullet(bullet));
				bullet.SetBoundary(0, background.getWidth(), 0, background.getHeight());
				bullet.setVY(-BASE_SPEED * 8);

				bullet.setPosition(x + ship.getWidth() / 2.0, y - bullet.getHeight());
				pane.getChildren().add(bullet.getView());
				bullets.add(bullet);
				soundManager.Play(SoundManager.SoundType.shoot);
			}

			break;
		}

		case STRENGTHED: {
			if (bullets.size() < 12) {

				Character bullet1 = new Character("Resources/Images/bullet", 1, new int[] { 10 });
				bullet1.position.addListener((ev) -> checkBullet(bullet1));
				bullet1.SetBoundary(0, background.getWidth(), 0, background.getHeight());
				bullet1.setVY(-BASE_SPEED * 8);

				bullet1.setPosition(x + ship.getWidth() / 2.0 - 5.0, y - bullet1.getHeight());
				pane.getChildren().add(bullet1.getView());
				bullets.add(bullet1);

				Character bullet2 = new Character("Resources/Images/bullet", 1, new int[] { 10 });
				bullet2.position.addListener((ev) -> checkBullet(bullet2));
				bullet2.SetBoundary(0, background.getWidth(), 0, background.getHeight());
				bullet2.setVY(-BASE_SPEED * 8);

				bullet2.setPosition(x + ship.getWidth() / 2.0 + 8.0, y - bullet2.getHeight());
				pane.getChildren().add(bullet2.getView());
				bullets.add(bullet2);

				soundManager.Play(SoundManager.SoundType.shoot);
			}
			break;
		}

		case MAXPOWER: {

			if (bullets.size() < 18) {

				Character bullet1 = new Character("Resources/Images/bullet", 1, new int[] { 10 });
				bullet1.position.addListener((ev) -> checkBullet(bullet1));
				bullet1.SetBoundary(0, background.getWidth(), 0, background.getHeight());
				bullet1.setVY(-BASE_SPEED * 8);
				bullet1.setVX(-BASE_SPEED * 2);

				bullet1.setPosition(x + ship.getWidth() / 2.0 - 5.0, y - bullet1.getHeight());
				pane.getChildren().add(bullet1.getView());
				bullets.add(bullet1);

				Character bullet2 = new Character("Resources/Images/bullet", 1, new int[] { 10 });
				bullet2.position.addListener((ev) -> checkBullet(bullet2));
				bullet2.SetBoundary(0, background.getWidth(), 0, background.getHeight());
				bullet2.setVY(-BASE_SPEED * 8);

				bullet2.setPosition(x + ship.getWidth() / 2.0, y - bullet2.getHeight());
				pane.getChildren().add(bullet2.getView());
				bullets.add(bullet2);

				Character bullet3 = new Character("Resources/Images/bullet", 1, new int[] { 10 });
				bullet3.position.addListener((ev) -> checkBullet(bullet3));
				bullet3.SetBoundary(0, background.getWidth(), 0, background.getHeight());
				bullet3.setVY(-BASE_SPEED * 8);
				bullet1.setVX(BASE_SPEED * 2);

				bullet3.setPosition(x + ship.getWidth() / 2.0 + 5.0, y - bullet3.getHeight());
				pane.getChildren().add(bullet3.getView());
				bullets.add(bullet3);

				soundManager.Play(SoundManager.SoundType.shoot);
			}

			break;

		}

		}
	}
	
	private void checkBullet(Character bullet) {
		if (bullet.getCharacterY() < 0)
			bullet.alive = false;
		
		else if (aliens.size() > 0) {
			for (Iterator<Character> it = aliens.iterator(); it.hasNext();) {
				Character alien = it.next();
				if (alien.collideWith(bullet)) {
					
					soundManager.Play(SoundManager.SoundType.hitOnEnemy);
					double x = alien.getCharacterX() + alien.getWidth() / 2.0;
					double y = alien.getCharacterY() + alien.getHeight() / 2.0;
					
					alien.setHP(alien.getHP() - 1);
					if (alien.getHP() <= 0) {
						alien.alive = false;
						soundManager.Play(SoundManager.SoundType.died);
					}
					
					
					bullet.alive = false;
					Character flame = new SpecialCharacter("Resources/Images/ExplodeFrame", 9,
							new int[] { 100, 100, 100, 100, 100, 100, 100, 100, 100 });
					flame.SetBoundary(0, background.getWidth(), 0, background.getHeight());
					flame.setPosition(x,y);
					pane.getChildren().add(flame.getView());
					flames.add(flame);
				}
			}
		}
	}
	
	
}


class SpecialCharacter extends Character{

	public SpecialCharacter(String frameName, int totalFrame, int[] durationTime) {
		super(frameName, totalFrame, durationTime);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void updateCurrentFrame(long elapsedTime) {
		long t = elapsedTime + lastRemainTime;
		while (t > getCurrFrameDuration()) {
			t -= getCurrFrameDuration();
			currentFrameNum++;
			if (currentFrameNum >= characterFrame.getTotalFrameNum()) {
				currentFrameNum--;
				alive = false;
				break;
			}
		}
		lastRemainTime = t;
	}
	
	@Override
	protected double checkNewYInUpdateMethod(double newY){
		return newY;
	}

}
