
public class BaseBoundary {
	private double left;
	private double right;
	private double top;
	private double bottom;
	
	public BaseBoundary(){}
	public BaseBoundary(double vLeft,double vRight,double vTop,double vBottom){
		left = vLeft;
		right = vRight;
		top = vTop;
		bottom = vBottom;
	}
	
	public void setBoundary(double vLeft,double vRight,double vTop,double vBottom){
		left = vLeft;
		right = vRight;
		top = vTop;
		bottom = vBottom;
	}
	
	
	public double getLeft(){
		return left;
	}
	public double getRight(){
		return right;
	}
	public double getTop(){
		return top;
	}
	public double getBottom(){
		return bottom;
	}
}
