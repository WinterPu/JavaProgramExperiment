public class BasePosition {
	private double x;
	private double y;

	public BasePosition() {}

	public BasePosition(double valueX, double valueY) {
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
	public void add(BasePosition position){
		x += position.x;
		y += position.y;
	}
}
