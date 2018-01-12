import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class CalculatorController {

	static final int BASIC_MODE = 0;
	static final int ADVANCED_MODE = 1;

	@FXML
	private Label labelExpression;
	private int mode = ADVANCED_MODE;

	@FXML
	private Label labelResult;

	@FXML
	private Button button1;

	@FXML
	private Button button2;

	@FXML
	private Button button3;

	@FXML
	private Button button4;

	@FXML
	private Button button5;

	@FXML
	private Button button6;

	@FXML
	private Button button7;

	@FXML
	private Button button8;

	@FXML
	private Button button9;

	@FXML
	private Button button0;

	@FXML
	private Button button00;

	@FXML
	private Button buttonMUL;

	@FXML
	private Button buttonDIV;
	@FXML
	private Button buttonSUB;

	@FXML
	private Button buttonDOT;

	@FXML
	private Button buttonADD;

	@FXML
	private Button buttonCLEAR;

	@FXML
	private Button buttonDEL;

	@FXML
	private Button buttonMODE;

	private ArrayList<Key> expression = new ArrayList<Key>();

	public void initialize() {
		labelResult.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		labelResult.setTextFill(Color.WHITE);
	}

	String getCurrentExpression(ArrayList<Key> expression) {
		String expressionString = "";
		if (expression.size() == 0)
			expressionString = "0";
		else {
			for (Key tmp : expression)
				expressionString += tmp.keyName;
		}
		return expressionString;
	}

	String getCurrentResult() {
		String result = "";

		Token handler = new BasicExpression();
		if (mode == BASIC_MODE)
			handler = new BasicExpression();
		else if (mode == ADVANCED_MODE)
			handler = new AdvancedExpression();
		ArrayList<Key> parseSequence = new ArrayList<Key>(expression);

		if (parseSequence.size() == 0)
			result = "0";
		else {
			if (handler.parse(parseSequence))
				result = String.valueOf(handler.value);
			else
				result = "Error";
		}
		return result;
	}

	void updateLabel(Label labelExpression, Label labelResult) {
		labelExpression.setText(getCurrentExpression(expression));
		labelResult.setText(getCurrentResult());
	}

	@FXML
	private void ButtonAction(Event event) {

		String name = ((Button) (event.getSource())).getText();

		// System.out.println(name);
		if (name.equals("Advanced") || name.equals("Basic")) {
			mode = (1 ^ mode);
			// System.out.println(mode);
			if (mode == ADVANCED_MODE)
				((Button) (event.getSource())).setText("Advanced");
			else if (mode == BASIC_MODE)
				((Button) (event.getSource())).setText("Basic");
			// set mode

		} else if (name.equals("⌫")) {
			if (expression.size() == 0)
				return;
			else {
				expression.remove(expression.size() - 1);
			}
		} else if (name.equals("C")) {
			// System.out.println("OK Clear");
			expression.removeAll(expression);
		} else {
			// System.out.println(name);
			Key addedItem = new Key(name);
			expression.add(addedItem);
		}

		updateLabel(labelExpression, labelResult);
	}

}
