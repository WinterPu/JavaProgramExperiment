import java.util.ArrayList;

import javafx.scene.control.Button;

public class HUDManager {

	private GameManager gm;
	
	 HUDManager(GameManager gameManager){
		 gm = gameManager;
		 initHUD();
	 }
	
	 
	 public void initHUD(){
		 flagLoseShow = false;
		 hearts.clear();
	 }
	
	ArrayList<BCHeart> hearts = new ArrayList<BCHeart>();
	
	Button btnRestart;
	boolean flagLoseShow = false;
	
	public void showShipHp() {
		CShip ship = gm.ship;
		
		if (ship == null)
			return;
		else {

			// Show Heart
			double base = 10;
			for (int i = 1; i <= ship.getHP(); i++) {
				if (i > hearts.size()) {
					BCHeart heart = new BCHeart(gm);

					heart.setPosition((base + heart.getWidth()) * i, base);
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
	
	
	
	public void showLoseScene(){
		if(!flagLoseShow)
		{
			flagLoseShow = true;
			btnRestart = new Button("Restart Game");
			btnRestart.setScaleX(2);
			btnRestart.setScaleY(2);
			btnRestart.setLayoutX(gm.background.getWidth() / 2.0 - btnRestart.getWidth() / 2.0);
			btnRestart.setLayoutY(gm.background.getHeight() / 2.0 - btnRestart.getHeight() / 2.0);
			gm.pane.getChildren().add(btnRestart);
			btnRestart.setOnMouseClicked((e) -> {
				gm.initGame();
				gm.pane.requestFocus();
			});
		}
	} 
	
	
	
	public void updateHUD(long elapsedTime){
		for (BCHeart heart :hearts) {
			heart.update(elapsedTime);
		}
	}

}
