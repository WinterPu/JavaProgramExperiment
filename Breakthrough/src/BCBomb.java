
public class BCBomb extends Character{

	private GameManager gameManager;
	public BCBomb(GameManager gm) {
		super("Resources/Images/bomb", 1, new int[] { 10 });
		// TODO Auto-generated constructor stub
		setBoundary(0,gm.background.getWidth(), 0, gm.background.getHeight());
		setVX(0);
		setVY(GameManager.BASE_SPEED * 5);
	}

}
