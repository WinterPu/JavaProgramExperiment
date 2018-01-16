import java.util.ArrayList;
import java.util.Iterator;

enum shootType {
	NORMAL, STRENGTHED, MAXPOWER;

	private static shootType[] values = values();

	public shootType getNextLevel() {
		if (this.ordinal() < values.length - 1)
			return values[this.ordinal() + 1];
		else
			return values[values.length - 1];
	}
};

public class CShip extends Character {

	private GameManager gameManager;
	public static final double SHIP_BASE_SPEED = 200f;
	// For the 2nd movement pattern
	public static final double SHIP_BASE_SPEED_NO_ACCELERATION = 800f;

	public static final int LIFE_LIMIT = 5;

	shootType shootMode;

	private int bulletNum;

	
	private double startSpeed;
	private double acceleration;
	
	
	public CShip(GameManager gm) {
		super("/Resources/Images/spaceship", 1, new int[] { 10 });
		// TODO Auto-generated constructor stub
		gameManager = gm;
		shootMode = shootType.NORMAL;
		
		acceleration =GameManager.BASE_SPEED ;
		startSpeed = SHIP_BASE_SPEED;
		
		setBoundary(0, gameManager.background.getWidth(), 0, gameManager.background.getHeight());
		setVX(0);
		setPosition(gameManager.background.getHeight() / 2.0, gameManager.background.getHeight() - getHeight());
		setHP(5);
		bulletNum = CAlien.ALIEN_INIT_LIFE * GameManager.ALIEN_INIT_NUM + 50;
		gm.pane.getChildren().add(getView());
	}

	public void shoot() {

		double vy = -GameManager.BASE_SPEED;

		switch (shootMode) {
		case NORMAL: {
			if (gameManager.bullets.size() < 6) {

				if (bulletNum >= 1) {
					bulletNum--;
					generateBullet(0, 0, 0, vy * 8);
					gameManager.soundManager.Play(SoundManager.SoundType.shoot);
				}
			}

			break;
		}

		case STRENGTHED: {
			if (gameManager.bullets.size() < 12) {

				if (bulletNum >= 2) {
					bulletNum -= 2;
					generateBullet(-5.0, 0, 0, vy * 8);

					generateBullet(8.0, 0, 0, vy * 8);
					gameManager.soundManager.Play(SoundManager.SoundType.shoot);
					checkShootMode();
				}
			}
			break;
		}

		case MAXPOWER: {

			if (gameManager.bullets.size() < 18) {
				if (bulletNum >= 3) {
					bulletNum -= 3;
					generateBullet(-5.0, 0, vy * 2, vy * 8);
					generateBullet(0, 0, 0, vy * 8);
					generateBullet(5.0, 0, -vy * 2, vy * 8);
					gameManager.soundManager.Play(SoundManager.SoundType.shoot);

					checkShootMode();
				}
			}

			break;

		}

		}

	}

	private void checkShootMode() {
		if (shootMode == shootType.STRENGTHED) {
			if (bulletNum < 2)
				shootMode = shootType.NORMAL;
		} else if (shootMode == shootType.MAXPOWER) {

			if (bulletNum == 2)
				shootMode = shootType.STRENGTHED;
			else if (bulletNum < 2)
				shootMode = shootType.NORMAL;
		}

	}

	// default position is : width: 1/2 ship width, height: the top of the ship
	public void generateBullet(double biasX, double biasY, double vx, double vy) {
		double x = getCharacterX();
		double y = getCharacterY();

		BCBullet bullet = new BCBullet(gameManager);
		bullet.position.addListener((ev) -> checkBullet(bullet));
		bullet.setVY(vy);
		bullet.setVX(vx);

		bullet.setPosition(x + getWidth() / 2.0 + biasX, y - bullet.getHeight() + biasY);
		gameManager.pane.getChildren().add(bullet.getView());
		gameManager.bullets.add(bullet);
	}

	private void checkBullet(BCBullet bullet) {
		if (bullet.getCharacterY() <= 0)
			bullet.alive = false;

		else if (gameManager.aliens.size() > 0) {
			for (Iterator<CAlien> it = gameManager.aliens.iterator(); it.hasNext();) {
				Character alien = it.next();
				if (alien.collideWith(bullet)) {

					gameManager.soundManager.Play(SoundManager.SoundType.hitOnEnemy);
					double x = alien.getCharacterX() + alien.getWidth() / 2.0;
					double y = alien.getCharacterY() + alien.getHeight() / 2.0;

					alien.setHP(alien.getHP() - 1);
					if (alien.getHP() <= 0) {
						alien.alive = false;
						alien.setHP(0); //prevent HP to less than 0
						gameManager.soundManager.Play(SoundManager.SoundType.died);
					}

					bullet.alive = false;
					BCFlame flame = new BCFlame(x, y, gameManager);
					gameManager.flames.add(flame);
				}
			}
		}
	}

	
	//Region -- Basic Methods
	public int getBulletNum() {
		return bulletNum;
	}

	public void setBulletNum(int value) {
		bulletNum = value;
	}

	public void setStartSpeed(double value){
		startSpeed = value;
	}
	public double getStartSpeed(){
		return startSpeed;
	}
	
	public void setAcceleration(double value){
		acceleration = value;
	}
	public double getAcceleration(){
		return acceleration;
	}
	
	//Region  -- Basic Methods End
	
	
}
