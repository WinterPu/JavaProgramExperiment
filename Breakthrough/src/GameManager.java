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

	CShip ship;
	BCBackground background;
	BCBonus bonus;
	AnchorPane pane;

	ArrayList<CAlien> aliens = new ArrayList<CAlien>();
	ArrayList<BCBullet> bullets = new ArrayList<BCBullet>();
	ArrayList<BCFlame> flames = new ArrayList<BCFlame>();
	ArrayList<BCBomb> bombs = new ArrayList<BCBomb>();
	ArrayList<BCHeart> hearts = new ArrayList<BCHeart>();

	SoundManager soundManager = new SoundManager();

	long lastTime = 0, currentTime = 0, elapsedTime = 0;
	double acceleration = 0.0;

	GameStatus gameStatus;
	
	
	
	Button btnRestart;
	boolean flagLoseShow = false;

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
		flagLoseShow = false;
		acceleration = BASE_SPEED * 2.0;
		gameStatus = GameStatus.PLAYING;
		initCharacters();
	}

	private void initCharacters() {
		pane.getChildren().clear();

		background = new BCBackground(this);
		ship = new CShip(this);

		aliens.clear();
		for (int n = 0; n < 10; n++) {
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
			ship.setVX(0);
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
		for (BCHeart heart : hearts) {
			heart.update(elapsedTime);
		}
		for (BCFlame flame : flames) {
			if (flame.currentFrameNum >= 0)
				flame.update(elapsedTime);
		}

		for (BCBomb bomb : bombs) {
			bomb.update(elapsedTime);
		}
	}

	private void gameplay(long elapsedTime) {

		alienAttack();
		generateRandomBonus();
		showShipHp();
		
		
		
		checkGameStatus();

	}

	private void checkGameStatus(){
			
		
		if (ship != null && !ship.alive) 
			gameStatus = GameStatus.LOSE;
	

	
		if(gameStatus == GameStatus.LOSE){
			
			
			if(!flagLoseShow)
			{
				flagLoseShow = true;
				btnRestart = new Button("Restart Game");
				btnRestart.setScaleX(2);
				btnRestart.setScaleY(2);
				btnRestart.setLayoutX(background.getWidth() / 2.0 - btnRestart.getWidth() / 2.0);
				btnRestart.setLayoutY(background.getHeight() / 2.0 - btnRestart.getHeight() / 2.0);
				pane.getChildren().add(btnRestart);
				btnRestart.setOnMouseClicked((e) -> {
					initGame();
					pane.requestFocus();
				});
			}
		}
		
	}
	
	private void showShipHp() {

		if (ship == null)
			return;
		else {

			// Show Heart
			double base = 10;

			for (int i = 1; i <= ship.getHP(); i++) {
				if (i > hearts.size()) {
					BCHeart heart = new BCHeart(this);

					heart.setPosition((base + heart.getWidth()) * i, base);
					pane.getChildren().add(heart.getView());
					hearts.add(heart);
				}
			}

			// Remove extra hearts
			for (int i = hearts.size() - 1; i >= ship.getHP(); i--) {
				pane.getChildren().remove(hearts.get(i).getView());
				hearts.remove(i);
			}
			if (hearts.size() == 0) {
				gameStatus = GameStatus.LOSE;
				return;
			}
		}

	}

	private void generateRandomBonus() {
		if (bonus == null && Math.random() < 0.5)
			bonus = new BCBonus(this);
	}

	private void alienAttack() {
		for (Iterator<CAlien> it = aliens.iterator(); it.hasNext();) {
			CAlien alien = it.next();
			if (Math.random() * 100 < 0.2) {
				alien.throwBomb();
			}
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
		if (ship != null) {

			if (e.getCode() == KeyCode.LEFT && ship.getVX() < 0) {
				ship.setVX(ship.getVX() - acceleration);
			} else if (e.getCode() == KeyCode.RIGHT && ship.getVX() > 0) {
				ship.setVX(ship.getVX() + acceleration);
			} else if (e.getCode() == KeyCode.LEFT)
				ship.setVX(-CShip.SHIP_BASE_SPEED);
			else if (e.getCode() == KeyCode.RIGHT)
				ship.setVX(CShip.SHIP_BASE_SPEED);
			if (e.getCode() == KeyCode.SPACE) {
				ship.shoot();
			}
		}
	}

}
