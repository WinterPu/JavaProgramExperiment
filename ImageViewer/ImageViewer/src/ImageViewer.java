import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImageViewer extends Application{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Image Viewer");
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ImageViewer.class.getResource("ImageViewer.fxml"));
			VBox  rootLayout = (VBox) loader.load();
						
			//ImageViewerController controller = new ImageViewerController();
			//controller.initialize(primaryStage);
			

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch(IOException exception){
			exception.printStackTrace();
		}
		
		

		
	}

}
