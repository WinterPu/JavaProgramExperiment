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

public class GameManager extends Application{

	CShip ship;
	CBackground background;
	Character bonus;
	AnchorPane pane;

	ArrayList<CAlien> aliens = new ArrayList<CAlien>();
	ArrayList<CBullet> bullets = new ArrayList<CBullet>();
	ArrayList<CFlame> flames = new ArrayList<CFlame>();
	
	ArrayList<Character> bombs = new ArrayList<Character>();
	ArrayList<Character> hearts = new ArrayList<Character>();

	SoundManager soundManager = new SoundManager();

	long lastTime = 0, currentTime = 0, elapsedTime = 0;
	double acceleration = 0.0;
	public static final double BASE_SPEED = 50f;



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

		background = new CBackground(this);

		ship = new CShip(this);
	

		aliens.clear();
		for (int n = 0; n < 10; n++) {
			
			CAlien one = new CAlien(n * 80, Math.random() * 300,this);
			aliens.add(one);
			
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
				ship.setVX(-CShip.SHIP_BASE_SPEED);
			else if (e.getCode() == KeyCode.RIGHT)
				ship.setVX(CShip.SHIP_BASE_SPEED);
			if (e.getCode() == KeyCode.SPACE) {
				ship.shoot(shootMode);
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

		for (CAlien alien : aliens) {
			alien.update(elapsedTime);
			double x = alien.getCharacterX();
			if (x <= 0 || x + alien.getWidth() >= widthBkg)
				alien.setVX(-alien.getVX());
		}
		for (CBullet bullet : bullets) {
			bullet.update(elapsedTime);
		}
		for (Character heart : hearts) {
			heart.update(elapsedTime);
		}
		for (CFlame flame : flames) {
			if (flame.currentFrameNum >= 0)
				flame.update(elapsedTime);
		}

		for (Character bomb : bombs) {
			bomb.update(elapsedTime);
		}
	}

//	private void gameplay(long elapsedTime) {
//		
//		alienAttack();
//		randomBonus();
//		
//		if (ship != null && !ship.alive) {
//			flagGameOver = true;
//		
//		}
//	}
	
	
	
//	private void alienAttack(){
//		for (Iterator<Character> it = aliens.iterator(); it.hasNext();) {
//			Character a = it.next();
//			if (Math.random() * 100 < 0.2) {
//				throwBomb(a);
//			}
//		}
//		
//  }
	
  
//	private void throwBomb(Character alien) {
//		Character bomb = new Character("bomb", 1, new int[] { 10 });
//		bomb.position.addListener((e) -> checkBomb(bomb));
//		bomb.setBoundary(0, background.getWidth(), 0, background.getHeight());
//		bomb.setVX(0);
//		bomb.setVY(BASE_SPEED * 5);
//		double x = alien.getCharacterX();
//		double y = alien.getCharacterY();
//		bomb.setPos(x + (a.getFrame().getWidth() - bomb.getFrame().getWidth()) / 2, y + a.getFrame().getHeight());
//		pane.getChildren().add(bomb.getView());
//		bombs.add(bomb);
//	}
//	
	

	private void reclaimCharacters() {
		for (Iterator<CBullet> it = bullets.iterator(); it.hasNext();) {
			CBullet bullet = it.next();
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
		for (Iterator<CFlame> it = flames.iterator(); it.hasNext();) {
			CFlame flame = it.next();
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


	
}

