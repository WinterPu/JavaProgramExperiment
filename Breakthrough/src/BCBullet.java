
public class BCBullet extends Character {

	public BCBullet(GameManager gm) {
		super("Resources/Images/bullet", 1, new int[] { 10 });
		setBoundary(0, gm.background.getWidth(), 0, gm.background.getHeight());
	}
}
