
public class CFlame extends Character {
	
	private GameManager gameManager;
	
	public CFlame(String frameName, int totalFrame, int[] durationTime,GameManager gm) {
		super(frameName, totalFrame, durationTime);
		// TODO Auto-generated constructor stub
		gameManager = gm;
	}
	
	public CFlame(double x, double y, GameManager gm) {
		super("Resources/Images/ExplodeFrame", 9,
				new int[] { 100, 100, 100, 100, 100, 100, 100, 100, 100 });
		// TODO Auto-generated constructor stub
		gameManager = gm;
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
	
	@Override
	protected double checkNewYInUpdateMethod(double newY){
		return newY;
	}

}
