import java.util.ArrayList;
import java.util.Iterator;

enum shootType {
	NORMAL, STRENGTHED, MAXPOWER
};

public class CShip extends Character  {

	private GameManager gameManager;
	public static final double SHIP_BASE_SPEED = 800f;
	
	public CShip(String frameName, int totalFrame, int[] durationTime,GameManager gm) {
		super(frameName, totalFrame, durationTime);
		// TODO Auto-generated constructor stub
		gameManager = gm;
	}

	public CShip(GameManager gm) {
		super("/Resources/Images/spaceship", 1, new int[] { 10 });
		// TODO Auto-generated constructor stub
		gameManager = gm;
		setBoundary(0, gameManager.background.getWidth(), 0, gameManager.background.getHeight());
		setVX(SHIP_BASE_SPEED);
		setPosition(gameManager.background.getHeight() / 2.0, gameManager.background.getHeight() - getHeight());
		setHP(5);
		gm.pane.getChildren().add(getView());
	}

	public void shoot(shootType mode) {

		double vy = -GameManager.BASE_SPEED;
		
		switch (mode) {
		case NORMAL: {
			if (gameManager.bullets.size() < 6) {
				generateBullet(0, 0, 0, vy * 8);
				gameManager.soundManager.Play(SoundManager.SoundType.shoot);
			}

			break;
		}

		case STRENGTHED: {
			if (gameManager.bullets.size() < 12) {

				generateBullet(-5.0, 0, 0, vy * 8);
				generateBullet(8.0, 0, 0, vy * 8);
				gameManager.soundManager.Play(SoundManager.SoundType.shoot);
			}
			break;
		}

		case MAXPOWER: {

			if (gameManager.bullets.size() < 18) {
				generateBullet(-5.0, 0, vy * 2, vy * 8);
				generateBullet(0, 0, 0, vy * 8);
				generateBullet(5.0, 0, -vy * 2, vy * 8);
				gameManager.soundManager.Play(SoundManager.SoundType.shoot);
			}

			break;

		}

	  }

	}

	//default position is : width: 1/2 ship width, height: the top of the ship
	public void generateBullet(double biasX, double biasY, double vx,double vy)
	{
		double x = getCharacterX();
		double y = getCharacterY();
		
		CBullet bullet = new CBullet(gameManager);
		bullet.position.addListener((ev) -> checkBullet(bullet));
		bullet.setVY(vy);
		bullet.setVX(vx);

		bullet.setPosition(x + getWidth() / 2.0+biasX, y - bullet.getHeight()+ biasY);
		gameManager.pane.getChildren().add(bullet.getView());
		gameManager.bullets.add(bullet);
	}
	
	
	private void checkBullet(CBullet bullet) {
		if (bullet.getCharacterY() < 0)
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
						gameManager.soundManager.Play(SoundManager.SoundType.died);
					}
					
					
					bullet.alive = false;
					CFlame flame = new CFlame(gameManager);
					flame.setPosition(x,y);
					gameManager.pane.getChildren().add(flame.getView());
					gameManager.flames.add(flame);
				}
			}
		}
	}
	
}
