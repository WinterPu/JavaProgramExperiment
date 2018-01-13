
public class CAlien extends Character{

	public static final double ALIEN_BASE_SPEED = 50f;
	private GameManager gameManager;
	public CAlien(String frameName, int totalFrame, int[] durationTime) {
		super(frameName, totalFrame, durationTime);
		// TODO Auto-generated constructor stub
	}
	

	
	//Random Duration Method
	@Override 
	public int[] randomDurations(int num){
		
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
		setHP(5);
		gm.pane.getChildren().add(getView());
	
	}
}
