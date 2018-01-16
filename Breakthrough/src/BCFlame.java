
public class BCFlame extends Character {
	
	public BCFlame(double x, double y, GameManager gm) {
		super("Resources/Images/ExplodeFrame", 9,
				new int[] { 100, 100, 100, 100, 100, 100, 100, 100, 100 });
		// TODO Auto-generated constructor stub
		setBoundary(0, gm.background.getWidth(), 0, gm.background.getHeight());
		setPosition(x,y);
		gm.pane.getChildren().add(getView());
		
	}
	
	@Override
	protected void updateCurrentFrame(long elapsedTime) {
		long t = elapsedTime + lastRemainTime;
		while (t > getCurrFrameDuration()) {
			t -= getCurrFrameDuration();
			currentFrameNum++;
			if (currentFrameNum >= characterFrame.getTotalFrameNum()) {
				currentFrameNum--;
				alive = false;
				break;
			}
		}
		lastRemainTime = t;
	}
	
	
	
	@Override // original method in [Character.java]
	protected double checkNewYInUpdateMethod(double newY){
		return newY;
	}

}
