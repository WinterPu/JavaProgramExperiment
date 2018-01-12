import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BaseFrame {

	private Image[] frameImage;
	private int[] duration; //show time for each image
	private int totalNum;
	
	public BaseFrame(String frameName,int numberOfFrames,int[] durationTime){
		totalNum = numberOfFrames;
		String fullName;
		frameImage = new Image[numberOfFrames];
		duration = new int[numberOfFrames];
		for(int i=0;i<numberOfFrames;i++){
			
			fullName = frameName + i+".png"; //fullName ="Package Name"+ frameName + i+".png";
			
			frameImage[i] = new Image(fullName);
			duration[i] = durationTime[i];
		}
	}
	
	public Image getFrameImage(int num){
		return frameImage[num];
	}
	
	public int getFrameDuration(int num){
		return duration[num];
	}
	public int getTotalFrameNum(){
		return totalNum;
	}

}
