
enum bonusType{
	LIFE_BLESSING,ATTACK_ENHANCE
}


public class CBonus extends Character{

	private GameManager gameManager;
	
	bonusType bonusMode;
	//Generate Duration Method
	@Override 
	public int[] generateDuration(int num){
		
		int[] durs = new int[12];
		for (int i = 0; i < durs.length; i++)
			durs[i] = 100;
		
		return durs;
	}
	
	public CBonus(GameManager gm) {
		super("Resources/Images/bonus", 12);
		// TODO Auto-generated constructor stub
		gameManager = gm;
		setBoundary(0, gm.background.getWidth(), 0, gm.background.getHeight());
		setVX(0);
		setVY(gm.BASE_SPEED * 2);
		setPosition(Math.random() * gm.background.getWidth(), 50);
		randomBonusType();
		position.addListener(e -> checkBonus());
		gm.pane.getChildren().add(getView());
	}

	
	
	private void randomBonusType(){
		if (Math.random() * 10 < 5)
			bonusMode =  bonusType.LIFE_BLESSING;
		else 
			bonusMode = bonusType.ATTACK_ENHANCE;
	}

	
	
	private void checkBonus() {
		CShip ship = gameManager.ship;
		
		if (this.getCharacterY()+this.getHeight() >= this.getBoundary().getBottom())
			this.alive = false;
		
		else if (this.collideWith(ship)) // ship¦Y¨ìbonus
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
			}
			

		}

	}

}
