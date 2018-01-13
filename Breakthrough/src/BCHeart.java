
public class BCHeart extends Character {

	public BCHeart(GameManager gm) {
		super("Resources/Images/heart", 2, new int[] { 100, 100 });
		// TODO Auto-generated constructor stub
		setBoundary(0, gm.background.getWidth(), 0, gm.background.getHeight());
		setVX(0);
		setVY(0);
	}

}
