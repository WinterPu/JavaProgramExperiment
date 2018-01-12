import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;


public class PolygonPainter extends Application{

	private List<Path> pathList = new ArrayList<>();
	private Path path = new Path();
	
	private int paintingMode = Mode.Polygon; 
	private boolean initialFlag = true;
	
	private double initalX =0;
	private double initalY =0;
	
	private double currentX = 0;
	private double currentY = 0;

	private boolean hintMode = false;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Poly Painter");
		// TODO Auto-generated method stub
		BorderPane mainPane = new BorderPane();
		AnchorPane paintingPane =new AnchorPane();		
		HBox buttonPane =new HBox();
		
		
		Button drawButton = new Button("Polygon");
		Button clearButton = new Button("Clear");
		
		drawButton.setPrefWidth(300);
		drawButton.setPrefHeight(50);
		clearButton.setPrefWidth(300);
		clearButton.setPrefHeight(50);
		buttonPane.getChildren().add(drawButton);
		buttonPane.getChildren().add(clearButton);

///////////////////////////////////////////////////////////////////////////////////////
		
		drawButton.setOnAction(e->{
			paintingMode = paintingMode^1;	
			if(paintingMode == Mode.Polygon)
			{
				drawButton.setText("Polygon");
				initialFlag = true;
				path =new Path();
				paintingPane.getChildren().add(path);
				pathList.add(path);
				hintMode =false;
				
			}
			else if(paintingMode == Mode.Polyline)
			{
				
				drawButton.setText("Polyline");
				initialFlag = true;
				path =new Path();
				paintingPane.getChildren().add(path);
				pathList.add(path);
				hintMode =false;
			}
		});
		
		
		clearButton.setOnAction(e->{
			hintMode = false;
			for(Path p : pathList){
				p.setStroke(Color.TRANSPARENT);
				p.getElements().removeAll();
			}
			path =new Path();
			path.setStroke(Color.BLACK);
			paintingPane.getChildren().add(path);
			pathList.add(path);
			initialFlag = true;
		});
		
		
		paintingPane.setOnMouseClicked(e->{
			if(e.getButton() == MouseButton.PRIMARY){
				
				if(initialFlag == true)
				{
					initialFlag = false;
					initalX = e.getX();
					initalY = e.getY();
					MoveTo mt = new MoveTo(initalX,initalY);
					path.getElements().add(mt);
					
					LineTo initialHintLine = new LineTo(initalX,initalY);
					path.getElements().add(initialHintLine);
					hintMode= true;
					
				}
				else{
					currentX = e.getX();
					currentY = e.getY();
					LineTo lt = new LineTo(currentX,currentY);
					path.getElements().add(lt);
					
					LineTo initialHintLine = new LineTo(currentX, currentY);
					path.getElements().add(initialHintLine);

				}
			}
			else if(e.getButton() == MouseButton.SECONDARY){
				
				hintMode= false;
				if(paintingMode == Mode.Polygon)
				{
					LineTo lt = new LineTo(initalX ,initalY);
					int count = path.getElements().size();
					path.getElements().remove(count-1);
					path.getElements().add(lt);
					
				}
				else if(paintingMode == Mode.Polyline)
				{
			
				}
				initialFlag =true;
			}
		});
		
		paintingPane.setOnMouseMoved(e->{
			if(hintMode == true)
			{
				double x = e.getX();
				double y = e.getY();
				System.out.println(x+y);
				
				LineTo ltt = new LineTo(x,y);
				int count = path.getElements().size();
				if(count != 0)
				path.getElements().remove(count-1);
				path.getElements().add(ltt);
				
				
	
			}
		});

///////////////////////////////////////////////////////////////////////////////////////		
		mainPane.setCenter(paintingPane);
		mainPane.setBottom(buttonPane);
		paintingPane.getChildren().add(path);
		pathList.add(path);
		
		
		Scene main = new Scene(mainPane,600,600);

		primaryStage.setScene(main);
		primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
}

class Mode{
	static final int Polygon =0;
	static final int Polyline = 1;
};