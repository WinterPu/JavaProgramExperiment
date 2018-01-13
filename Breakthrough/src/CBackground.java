
public class CBackground extends Character{
	
	public CBackground(GameManager gm) {
		super("/Resources/Images/bg_image", 1, new int[] { 10 });
		// TODO Auto-generated constructor stub
		setBoundary(0, getWidth(), 0, getHeight());
		gm.pane.getChildren().add(getView());
	}
}
