import java.util.ArrayList;

public class BasicExpression extends Token {

	@Override
	public boolean parse(ArrayList<Key> cmd) {
		// TODO Auto-generated method stub

		Number number1 = new Number();

		//printALL(cmd);
		if (!number1.parse(cmd))
			return false;
		//System.out.print("Step1 seq:");
		//printALL(cmd);
		//System.out.println("Step1 res:" + number1.value);
		
		value = number1.value;
		
		while (cmd.size() > 0) {

			Operator op = new Operator("+-x¡Â");
			Number number2 = new Number();

			if (!op.parse(cmd))
				return false;

			//System.out.print("Step2 seq:");
			//printALL(cmd);
			// System.out.println(op.opcode);
			if (!number2.parse(cmd))
				return false;

			//System.out.print("Step3 seq:");
			//printALL(cmd);
			// System.out.println(number2.value);
			value = op.eval(value, number2.value);
			//System.out.println("final res:" + value);
		}
		return true;
	}

	/*
	void printALL(ArrayList<Key> cmd) {
		for (Key i : cmd)
			System.out.print(i.keyName);

		System.out.println("END!!");
	}
	*/
}
