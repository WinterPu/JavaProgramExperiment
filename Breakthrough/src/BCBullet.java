
public class BCBullet extends Character {

	private GameManager gameManager;
	public BCBullet(String frameName, int totalFrame, int[] durationTime,GameManager gm) {
		super(frameName, totalFrame, durationTime);
		// TODO Auto-generated constructor stub
		gameManager = gm;
	}
	
	public BCBullet(GameManager gm) {
		super("Resources/Images/bullet", 1, new int[] { 10 });
		// TODO Auto-generated constructor stub
		gameManager = gm;
		setBoundary(0, gm.background.getWidth(), 0, gm.background.getHeight());
	}
}
