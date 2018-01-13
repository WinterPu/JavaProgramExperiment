import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Character {
	BaseVelocity velocity = new BaseVelocity();
	BaseBoundary boundary = new BaseBoundary();
	ObjectProperty<BasePosition> position =new SimpleObjectProperty<BasePosition>( new BasePosition());
	//region begin -- Position Explanation
	//Old version
	//--DoubleProperty X,Y;
	//X Y are both DoubleProperty when you want to change X,Y, it needs to call twice.
	//we want to encapsulate to one Property
		
	//new version
	//because [BasePosition] is the class you defined by yourself
	// -- ObjectProperty<BasePosition> position =new SimpleObjectProperty<BasePosition>( new BasePosition());
	//ObjectProperty<BasePosition> is an abstract class
	//SimpleObjectProperty<BasePosition>() can instantiate non-abstract class
	// the outside part is container,   [so have to add new Position()]
	//region end -- Position Explanation
	private int hp;
	boolean alive = true;
	
	BaseFrame characterFrame;
	int currentFrameNum;
	ImageView characterImageView = new ImageView();
	
	//choose which image to show
	long lastRemainTime = 0;
	
	
	//position is a property
	protected double getCharacterX(){
		return position.get().getX();
	}
	protected double getCharacterY(){
		return position.get().getY();
	}

	public Character(String frameName,int totalFrame,int[] durationTime){
		
		characterFrame = new BaseFrame(frameName,totalFrame,durationTime);
		
		currentFrameNum =0;
		alive = true;
		hp = 1;
		characterImageView.xProperty().set(getCharacterX());
		characterImageView.yProperty().set(getCharacterY());
		characterImageView.setImage(characterFrame.getFrameImage(0));
	}
	
	public int[] randomDurations(int num){
		return new int[num];
	}
	//Random
	public Character(String frameName,int totalFrame){
		characterFrame = new BaseFrame(frameName,totalFrame,randomDurations(totalFrame));
		
		currentFrameNum =0;
		alive = true;
		hp = 1;
		characterImageView.xProperty().set(getCharacterX());
		characterImageView.yProperty().set(getCharacterY());
		characterImageView.setImage(characterFrame.getFrameImage(0));
	}
	
	public Image getCurrFrame(){
		return characterFrame.getFrameImage(currentFrameNum);
	}
	public int getCurrFrameDuration(){
		return characterFrame.getFrameDuration(currentFrameNum);
	}
	public double getWidth(){
		return getCurrFrame().getWidth();
	}
	public double getHeight(){
		return getCurrFrame().getHeight();
	}
	
	
	//origin is at the Top_left point of the view
	//position is the Top_left point of the Image
	 public boolean collideWith(Character anotherOne) {
	        double left1 = getCharacterX();
	        double right1 = getCharacterX() + getWidth();
	        double top1 = getCharacterY();
	        double bottom1 = getCharacterY() + getHeight();
	        
	        double left2 = anotherOne.getCharacterX();
	        double right2 = anotherOne.getCharacterX() + anotherOne.getWidth();
	        double top2 = anotherOne.getCharacterY();
	        double bottom2 =anotherOne.getCharacterY() + anotherOne.getHeight();
	        
	        if (Math.max(left1, left2)<Math.min(right1, right2)
	            && Math.max(top1, top2)<Math.min(bottom1, bottom2))
	            return true;
	        else
	            return false;
	 }
	
	public void setPosition(double x,double y){
	
		position.set(new BasePosition(x,y));
		//this for event triggered
		//if   position.get().setX(x);  -- no event triggered
		
		characterImageView.setX(x);
		characterImageView.setY(y);
	} 
	 
	
	protected void updateCurrentFrame(long elapsedTime){
		long time = elapsedTime + lastRemainTime;
		while (time>getCurrFrameDuration()){
			time -=getCurrFrameDuration();
			//move to next the frame and recount
			currentFrameNum = (currentFrameNum +1) % characterFrame.getTotalFrameNum();
		}
		lastRemainTime = time;
	}
	
	protected double checkNewYInUpdateMethod(double newY){
		 if (newY<0)
	         return newY;
	     else if (newY+getHeight()>boundary.getBottom())
	    	 return boundary.getBottom() - getHeight();
	     else 
	    	 return newY;
	}
	
	public void update(long elapsedTime){
		// unit /1000 : ms to s
		double newX = getCharacterX() + velocity.getX() * (double)elapsedTime / 1000.0;
		double newY = getCharacterY() + velocity.getY() * (double)elapsedTime / 1000.0;
		
		 if (newX<0)
	         newX = 0;
	     else if (newX+getWidth()>boundary.getRight())
	    	 newX = boundary.getRight()-getWidth();
	     
		 newY = checkNewYInUpdateMethod(newY);
	    
		 setPosition(newX, newY);
		
		//will overwrite if the frame for temporary image :like explosion (just appear once)
		updateCurrentFrame(elapsedTime);
		characterImageView.setImage(getCurrFrame());
		
	}

	//region -- basic methods
	public ImageView getView() {
		return characterImageView;
	}

	// velocity
	public BaseVelocity getV() {
		return velocity;
	}

	public double getVX() {
		return velocity.getX();
	}

	public double getVY() {
		return velocity.getY();
	}

	public void setVX(double value) {
		velocity.setX(value);
	}

	public void setVY(double value) {
		velocity.setY(value);
	}

	// boundary
	public BaseBoundary getBoundary() {
		return boundary;
	}

	public void setBoundary(double vLeft, double vRight, double vTop, double vBottom) {
		boundary.setBoundary(vLeft, vRight, vTop, vBottom);
	}
	// Hp
	public int getHP()
	{
		return hp;
	}
	public void setHP(int value){
		hp =  value;
	}


}
