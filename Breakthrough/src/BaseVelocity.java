
public class BaseVelocity {

	private double x;
	private double y;
	
	public BaseVelocity() {}

	public BaseVelocity(double valueX, double valueY) {
		x = valueX;
		y = valueY;
	}

	public void setX(double value) {
		x = value;
	}

	public void setY(double value) {
		y = value;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public void add(BaseVelocity velocity){
		x += velocity.x;
		y += velocity.y;
	}
}
