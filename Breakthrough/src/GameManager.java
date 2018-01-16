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

enum GameStatus {
	PLAYING, WIN, LOSE
};

public class GameManager extends Application {

	public static final double BASE_SPEED = 50f;
	public static final int ALIEN_INIT_NUM = 10;
	public static final double MIN_REINFORCE_TIME = 30000;
	public static final double MAX_REINFORCE_TIME = 60000;

	CShip ship;
	BCBackground background;
	BCBonus bonus;
	AnchorPane pane;

	ArrayList<CAlien> aliens = new ArrayList<CAlien>();
	ArrayList<BCBullet> bullets = new ArrayList<BCBullet>();
	ArrayList<BCFlame> flames = new ArrayList<BCFlame>();
	ArrayList<BCBomb> bombs = new ArrayList<BCBomb>();

	SoundManager soundManager = new SoundManager();
	HUDManager hudManager = new HUDManager(this);

	long lastTime = 0, currentTime = 0, elapsedTime = 0;


	GameStatus gameStatus;

	// For Reinforce Method
	long timeCurrentWar;
	long reinforceTimeInterval;

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

	public void initGame() {

		timeCurrentWar = System.currentTimeMillis();
		generateReinforceTimeInterval();

		gameStatus = GameStatus.PLAYING;
		initCharacters();
		hudManager.initHUD();
	}

	private void initCharacters() {
		pane.getChildren().clear();

		background = new BCBackground(this);
		ship = new CShip(this);

		aliens.clear();
		spawnEnemy();
	}

	private void spawnEnemy() {
		for (int n = 0; n < ALIEN_INIT_NUM; n++) {
			CAlien alien = new CAlien(n * 80, Math.random() * 300, this);
			aliens.add(alien);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	AnimationTimer timer = new AnimationTimer() {

		@Override
		public void handle(long now) {
			// TODO Auto-generated method stub
			currentTime = System.currentTimeMillis();
			elapsedTime = currentTime - lastTime;

			updateAll(elapsedTime);

			gameplay(elapsedTime);

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

		if (ship != null) {
			ship.update(elapsedTime);

			// Clean Remaining V
			// ship.setVX(0);
		}
		if (bonus != null)
			bonus.update(elapsedTime);

		for (CAlien alien : aliens) {
			alien.update(elapsedTime);
			double x = alien.getCharacterX();
			if (x <= 0 || x + alien.getWidth() >= widthBkg)
				alien.setVX(-alien.getVX());
		}
		for (BCBullet bullet : bullets) {
			bullet.update(elapsedTime);
		}

		for (BCFlame flame : flames) {
			if (flame.currentFrameNum >= 0)
				flame.update(elapsedTime);
		}

		for (BCBomb bomb : bombs) {
			bomb.update(elapsedTime);
		}

		hudManager.updateHUD(elapsedTime);
	}

	private void gameplay(long elapsedTime) {

		checkGameStatus();

		if (gameStatus == GameStatus.PLAYING) {
			alienAttack();
			reinforceEnemy();
			generateRandomBonus();
		}
	}

	private void checkGameStatus() {

		if (ship != null && !ship.alive)
			gameStatus = GameStatus.LOSE;

		if (aliens.size() == 0)
			gameStatus = GameStatus.WIN;

		if (gameStatus != GameStatus.PLAYING) {
			hudManager.showGameEndScene();

			if (ship != null && ship.alive)
				ship.setVX(0);
		}
	}

	
	
	private void generateRandomBonus() {

		// if(gameStatus == GameStatus.PLAYING)
		// {
		if (bonus == null && Math.random() < 0.5)

			bonus = new BCBonus(this);
		// }
	}

	private void alienAttack() {
		for (Iterator<CAlien> it = aliens.iterator(); it.hasNext();) {
			CAlien alien = it.next();
			if (Math.random() * 100 < 0.2) {
				alien.throwBomb();
			}
		}
	}

	private void generateReinforceTimeInterval() {

		reinforceTimeInterval = (long) (Math.random() * (MAX_REINFORCE_TIME - MIN_REINFORCE_TIME + 1)
				+ MIN_REINFORCE_TIME);
	}

	private void reinforceEnemy() {

		// if(gameStatus != GameStatus.PLAYING)
		// return;

		long elapsedCurrentWarTime = currentTime - timeCurrentWar;
		//System.out.println(elapsedCurrentWarTime);
		//System.out.println(reinforceTimeInterval);
		if (elapsedCurrentWarTime >= reinforceTimeInterval) {
			timeCurrentWar = currentTime;
			generateReinforceTimeInterval();
			spawnEnemy();
		}

	}

	private void reclaimCharacters() {
		for (Iterator<BCBullet> it = bullets.iterator(); it.hasNext();) {
			BCBullet bullet = it.next();
			if (!bullet.alive) {
				pane.getChildren().remove(bullet.getView());
				it.remove();
			}
		}
		for (Iterator<CAlien> it = aliens.iterator(); it.hasNext();) {
			CAlien alien = it.next();
			if (!alien.alive) {
				pane.getChildren().remove(alien.getView());
				it.remove();
			}
		}
		for (Iterator<BCFlame> it = flames.iterator(); it.hasNext();) {
			BCFlame flame = it.next();
			if (!flame.alive) {
				pane.getChildren().remove(flame.getView());
				it.remove();
			}
		}

		for (Iterator<BCBomb> it = bombs.iterator(); it.hasNext();) {
			BCBomb bomb = it.next();
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

	private void keyStrike(KeyEvent e) {
		if (ship != null && gameStatus == GameStatus.PLAYING) {

			// this is with acceleration version
			if (e.getCode() == KeyCode.LEFT && ship.getVX() < 0) {
				ship.setVX(ship.getVX() - ship.getAcceleration());
			} else if (e.getCode() == KeyCode.RIGHT && ship.getVX() > 0) {
				ship.setVX(ship.getVX() + ship.getAcceleration());
			} else if (e.getCode() == KeyCode.LEFT)
				ship.setVX(-ship.getStartSpeed());
			else if (e.getCode() == KeyCode.RIGHT)
				ship.setVX(ship.getStartSpeed());

			// if you want to enable this pattern of movement
			// please uncomment [ship.setVX(0)] in[updateAll] Method
			// And Change The Initial Speed of The ship

			// if (e.getCode() == KeyCode.LEFT && ship.getVX() < 0) {
			// ship.setVX(ship.getVX() - acceleration);
			// } else if (e.getCode() == KeyCode.RIGHT && ship.getVX() > 0) {
			// ship.setVX(ship.getVX() + acceleration);
			// } else if (e.getCode() == KeyCode.LEFT)
			// ship.setVX(-CShip.SHIP_BASE_SPEED_NO_ACCELERATION);
			// else if (e.getCode() == KeyCode.RIGHT)
			// ship.setVX(CShip.SHIP_BASE_SPEED_NO_ACCELERATION);
			//
			if (e.getCode() == KeyCode.SPACE && ship.getBulletNum() > 0) {
				ship.shoot();
			}
		}
	}

}
