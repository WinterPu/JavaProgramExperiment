
enum bonusType {
	LIFE_BLESSING, ATTACK_ENHANCE, BULLET_SUPPLEMENT
}

public class BCBonus extends Character {

	public static final int BULLET_SUPPLEMENT_VALUE = 10;
	
	private GameManager gameManager;

	bonusType bonusMode;

	// Generate Duration Method
	@Override
	public int[] generateDuration(int num) {

		int[] durs = new int[12];
		for (int i = 0; i < durs.length; i++)
			durs[i] = 100;

		return durs;
	}

	public BCBonus(GameManager gm) {
		super("Resources/Images/Bonus", 12);
		// TODO Auto-generated constructor stub
		gameManager = gm;
		setBoundary(0, gm.background.getWidth(), 0, gm.background.getHeight());
		setVX(0);
		setVY(GameManager.BASE_SPEED * 2);
		setPosition(Math.random() * gm.background.getWidth(), 50);
		randomBonusType();
		position.addListener(e -> checkBonus());
		gm.pane.getChildren().add(getView());
	}

	private void randomBonusType() {
		double bonusProbability = Math.random() * 10;
		if (bonusProbability < 10.0 && bonusProbability>=5.0)
			bonusMode = bonusType.BULLET_SUPPLEMENT;
		else if (bonusProbability < 5.0 && bonusProbability>=2.0)
			bonusMode = bonusType.LIFE_BLESSING;
		else
			bonusMode = bonusType.ATTACK_ENHANCE;
	}

	private void checkBonus() {
	
		if (this.getCharacterY()+this.getHeight() >= this.getBoundary().getBottom())
			this.alive = false;
		else 
		{
			CShip ship = gameManager.ship;
			if(ship == null)
				return;
			
			if (this.collideWith(ship)) // ship¦Y¨ìbonus
			{
			gameManager.soundManager.Play(SoundManager.SoundType.bonus);
			this.alive = false;
			
			// Enhace Part
			switch(bonusMode){
			case LIFE_BLESSING:{
				
				if(ship.getHP() <  CShip.LIFE_LIMIT)
					ship.setHP(ship.getHP()+1);
				break;
			
			}
			
			case ATTACK_ENHANCE:{
				ship.shootMode = ship.shootMode.getNextLevel();
				break;
			  }
			
			
			case BULLET_SUPPLEMENT:{
				ship.setBulletNum(ship.getBulletNum() +  BULLET_SUPPLEMENT_VALUE);
				break;
			  }
			}
		}
	}
  }
}
