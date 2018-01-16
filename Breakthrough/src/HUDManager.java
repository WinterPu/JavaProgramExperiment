import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class HUDManager {
	
	public static final int HUD_BASE =10;

	private GameManager gm;
	
	Text textBulletNum;
	
	 HUDManager(GameManager gameManager){
		 gm = gameManager;
	 }
	
	 
	 public void initHUD(){
		 flagSceneShow = false;
		 hearts.clear();
		 
		 textGameStatus = new Text("");
		 textBulletNum = new Text(" X ");
		 updateShowShipHp();
		 showShipBulletNum();
	 }
	
	ArrayList<BCHeart> hearts = new ArrayList<BCHeart>();
	
	
	//For Game End
	Text textGameStatus;

	//For Lose
	Button btnRestart;
	boolean flagSceneShow = false;
	
	public void updateShowShipHp() {
		CShip ship = gm.ship;
		
		if (ship == null)
			return;
		else {

			// Show Heart
			double base = HUD_BASE;
			for (int i = 1; i <= ship.getHP(); i++) {
				if (i > hearts.size()) {
					BCHeart heart = new BCHeart(gm);

					heart.setPosition((base + heart.getWidth()) * i - heart.getWidth(), base);
					gm.pane.getChildren().add(heart.getView());
					hearts.add(heart);
				}
			}

			// Remove extra hearts
			for (int i = hearts.size() - 1; i >= ship.getHP(); i--) {
				gm.pane.getChildren().remove(hearts.get(i).getView());
				hearts.remove(i);
			}
			if (hearts.size() == 0) {
				gm.gameStatus = GameStatus.LOSE;
				return;
			}
		}
	}
	
	public void showShipBulletNum(){
		double x = HUD_BASE;
		double y = HUD_BASE * 2.0;
		BCBullet bulletIcon = new BCBullet(gm);
		BCHeart heartIcon = new BCHeart(gm);
		bulletIcon.setPosition(x+heartIcon.getWidth()/4.0, y+heartIcon.getHeight());
		gm.pane.getChildren().add(bulletIcon.getView());


		textBulletNum.setText(" X "+ String.valueOf(gm.ship.getBulletNum()));
		textBulletNum.setX(bulletIcon.getCharacterX() + bulletIcon.getWidth() + HUD_BASE*2.5 + heartIcon.getWidth()/4.0);
		textBulletNum.setY(HUD_BASE/2.0 +heartIcon.getHeight()*2);
		
		textBulletNum.setFill(Color.WHITE);
		textBulletNum.setScaleX(2);
		textBulletNum.setScaleY(2);
		gm.pane.getChildren().add(textBulletNum);

	}
	public void updateShipBulletNumView(){
		if(gm.ship!=null)
			textBulletNum.setText(" X "+ String.valueOf(gm.ship.getBulletNum()));
	}
	
	
	public void showGameEndScene(){
		if(!flagSceneShow)
		{
			flagSceneShow = true;
			
			double baseX = gm.background.getWidth() / 2.0; 
			double baseY = gm.background.getHeight() / 2.0;
			

			btnRestart = new Button("Restart Game");

			//Notice:[btnRestart.getWidth()] return 0.0
			//Notice:[btnRestart.getHeight()] return 0.0
			//System.out.println(btnRestart.getTranslateY());
			btnRestart.setLayoutX(baseX - 20);
			btnRestart.setLayoutY(baseY);
			btnRestart.setScaleX(2);
			btnRestart.setScaleY(2);
			
			initWinAndLoseView();
			textGameStatus.setScaleX(10);
			textGameStatus.setScaleY(10);
			textGameStatus.setLayoutX(baseX - textGameStatus.getStrokeWidth()/2.0);
			textGameStatus.setLayoutY(HUD_BASE*10);
			
			gm.pane.getChildren().add(textGameStatus);
			gm.pane.getChildren().add(btnRestart);
			btnRestart.setOnMouseClicked((e) -> {
				gm.initGame();
				gm.pane.requestFocus();
			});
		}
	} 
	
	private void initWinAndLoseView(){
		
		
		if(gm.gameStatus == GameStatus.LOSE)
		{
			textGameStatus.setText("You Lose !");
			textGameStatus.setFill(Color.WHITE);
		}
		else {
			
			textGameStatus.setText("You Win !");
			textGameStatus.setFill(Color.RED);
		}
	}
	
	
	public void updateHUD(long elapsedTime){
		for (BCHeart heart :hearts) {
			heart.update(elapsedTime);
		}
		
		updateShowShipHp();
		updateShipBulletNumView();
		
	}

}
