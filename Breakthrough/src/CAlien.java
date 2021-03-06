
public class CAlien extends Character{

	public static final double ALIEN_BASE_SPEED = 50f;
	public static final int ALIEN_INIT_LIFE  = 5;
	
	private GameManager gameManager;
	
	//Random Duration Method
	@Override 
	public int[] generateDuration(int num){
		
		int[] durs = new int[num];
		for (int i = 0; i < durs.length; i++)
			durs[i] = 50 + (int) (50 * Math.random() + 1);
		
		return durs;
	}
	
	public CAlien(double x, double y,GameManager gm){
		//Random Duration
		super("/Resources/Images/SkeltonFrame", 15);
		gameManager = gm;
		setBoundary(0, gm.background.getWidth(), 0, gm.background.getHeight());
		setVX(ALIEN_BASE_SPEED * (Math.random() + 1));
		setPosition(x,y);
		setHP(ALIEN_INIT_LIFE);
		gm.pane.getChildren().add(getView());
	
	}
	
	
	
	public void throwBomb() {
		BCBomb bomb = new BCBomb(gameManager);
		bomb.position.addListener((e) -> checkBomb(bomb));
		
		double x = getCharacterX();
		double y = getCharacterY();
		
		bomb.setPosition(x + getWidth()/ 2.0, y + getHeight());
		gameManager.pane.getChildren().add(bomb.getView());
		gameManager.bombs.add(bomb);
	}

	private void checkBomb(Character bomb) {
		CShip ship = gameManager.ship;
		if (bomb.getCharacterY() + bomb.getHeight() >= bomb.getBoundary().getBottom())
			bomb.alive = false;
		else if (ship != null) {
			if (ship.collideWith(bomb)) {
				gameManager.soundManager.Play(SoundManager.SoundType.HIT_ON_SHIP);
				ship.setHP(ship.getHP() - 1);
				if (ship.getHP() <= 0) {
					ship.alive = false;
					ship.setHP(0); //prevent HP to less than 0 otherwise, HUD Manager will meet problems to show hearts
					gameManager.soundManager.Play(SoundManager.SoundType.SHIP_BOOM);
					gameManager.soundManager.Play(SoundManager.SoundType.GAMEOVER);
				}
				bomb.alive = false;
				double x = bomb.getCharacterX() + bomb.getWidth() / 2.0;
				double y = bomb.getCharacterY() + bomb.getHeight() / 2.0;
				
				
				BCFlame flame = new BCFlame(x,y,gameManager);
				gameManager.flames.add(flame);
			}
		}
	}

}
