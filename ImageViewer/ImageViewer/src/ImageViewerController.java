
import java.io.File;
import java.nio.ByteBuffer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageViewerController {

    @FXML
    private ImageView imageviewerOriginal;

    @FXML
    private ImageView imageviewerShowSkin;

    @FXML
    private Label labelContrast;

    @FXML
    private Slider slider;

    @FXML
    private TextField textfieldContrast;

    @FXML
    private Button buttonSelect;

    @FXML
    private Button buttonShowSkin;
    
    private Stage primaryStage;
    
    private Image originalImage = null;
    
    @FXML
    private void initialize(){
    	//[wrong way]
    	//this.primaryStage = stage;
    	
    	slider.setMin(-255);
    	slider.setMax(255);
    	slider.setValue(0);
    	textfieldContrast.setText("0");
    	slider.valueProperty().addListener(e->setContrastBySlider());
    	textfieldContrast.textProperty().addListener(e->setContrastByTxtField());
    }
    
   	public void setContrastByTxtField() {
		// TODO Auto-generated method stub
   		String value =	textfieldContrast.getText();
   		
   		int num = (int)slider.getValue();
   		try{
   		  num = Integer.parseInt(value);
   		  if(num<-255 || num >255)
   			num = (int)slider.getValue();
   		  // is an integer!
   		} catch (NumberFormatException e) {
   		  // not an integer!
   		 num = (int)slider.getValue();
   	   }
   		slider.valueProperty().setValue(num);
    	updateImageByContrast(num);
	}

	public void setContrastBySlider() {
		// TODO Auto-generated method stub
		int value = (int)slider.getValue();
    	textfieldContrast.textProperty().setValue(String.valueOf(value));
    	updateImageByContrast(value);
	}
	
	
	public void updateImageByContrast(int constrastValue){
		if(originalImage== null)
			return;
		
		int width = (int)originalImage.getWidth();
		int height = (int)originalImage.getHeight();
		
		PixelReader reader = originalImage.getPixelReader();

		byte[] pixels  =new byte[width*height*4];
		reader.getPixels(0, 0, width, height,(WritablePixelFormat<ByteBuffer>)PixelFormat.getByteBgraInstance(), pixels,0, width*4);
		System.out.println(pixels.length);
		double f = getContrastRatio(constrastValue);
		int p = 0;
        for (int i = 0; i<height; i++) {
            for (int j=0; j<width; j++) {
                int b = pixels[p]&0xff;
                int g = pixels[p+1]&0xff;
                int r = pixels[p+2]&0xff;
                
                int newR = (int)(f*(r-128)+128);
                int newG = (int)(f*(g-128)+128);
                int newB = (int)(f*(b-128)+128);
                
                newR = (newR>255? 255:(newR<0?0:newR));
                newG = (newG>255? 255:(newG<0?0:newG));
                newB = (newB>255? 255:(newB<0?0:newB));
                
                pixels[p+2]= (byte)(newR&0xff);
                pixels[p+1] = (byte)(newG&0xff);
                pixels[p] = (byte)(newB&0xff);
                p += 4;
            }
        }
        System.out.println("Done!");
        
        WritableImage writeableImage =new WritableImage(width,height);
        PixelWriter writer = writeableImage.getPixelWriter();
        writer.setPixels(0, 0, width, height,(WritablePixelFormat<ByteBuffer>)PixelFormat.getByteBgraInstance(), pixels,0, width*4);
        imageviewerOriginal.setImage(writeableImage);
   
	}
	
	public double getContrastRatio(int constrastValue){
		return (double)(259*(constrastValue+255))/(255*(259-constrastValue));
	}

	public void openImage(Event event){
    	FileChooser fileChooser = new FileChooser();
    	File file = fileChooser.showOpenDialog(primaryStage);
    	fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files",
            "*.bmp", "*.png", "*.jpg", "*.gif"));// limit fileChooser options to image files
    	if(file != null){
    		String path = "file:/"+file.getPath().replace("\\","/");
    	    originalImage = new Image(path);
    	    imageviewerOriginal.setImage(originalImage);
    	}
    }
	
	public void showSkin(){
		if(originalImage == null)
		{
			System.out.println("need to load a image");
			
		}
		else {
			int width = (int)originalImage.getWidth();
			int height = (int)originalImage.getHeight();
			
			PixelReader reader = originalImage.getPixelReader();

			byte[] pixels  =new byte[width*height*4];
			reader.getPixels(0, 0, width, height,(WritablePixelFormat<ByteBuffer>)PixelFormat.getByteBgraInstance(), pixels,0, width*4);
			System.out.println(pixels.length);

			int p = 0;
	        for (int i = 0; i<height; i++) {
	            for (int j=0; j<width; j++) {
	                int b = pixels[p]&0xff;
	                int g = pixels[p+1]&0xff;
	                int r = pixels[p+2]&0xff;
	                if(!(r>g&&g>b))
	                	pixels[p] = pixels[p+1] = pixels[p+2] = 0;
				
	                p+=4;
	            }
	        }
	        System.out.println("Done!");
	        
	        WritableImage writeableImage =new WritableImage(width,height);
	        PixelWriter writer = writeableImage.getPixelWriter();
	        writer.setPixels(0, 0, width, height,(WritablePixelFormat<ByteBuffer>)PixelFormat.getByteBgraInstance(), pixels,0, width*4);
	        System.out.println("Done!2");
	        imageviewerShowSkin.setImage(writeableImage);
	        //originalImage = new Image(new ByteArrayInputStream(pixels));
		}
	}

}
