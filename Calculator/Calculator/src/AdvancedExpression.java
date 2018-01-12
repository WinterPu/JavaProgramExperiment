import java.util.ArrayList;

public class AdvancedExpression extends Token {


	@Override
	public boolean parse(ArrayList<Key> cmd) {
		// TODO Auto-generated method stub
		ArrayList<Double> oprandList = new ArrayList<Double>();
		Number number1 = new Number();
		if (!number1.parse(cmd))
			return false;
		oprandList.add(number1.value);
		
		while (cmd.size() > 0) {

			Operator opADDSUB = new Operator("+-");
			Operator opMULDIV = new Operator("x¡Â");
			Number number2 = new Number();

			if(opADDSUB.parse(cmd))
			{
				if (!number2.parse(cmd))
					return false;
				oprandList.add(opADDSUB.eval(0, number2.value));
			}
			else {
				if(!opMULDIV.parse(cmd))
					return false;
				
				if (!number2.parse(cmd))
					return false;
				
				int index = oprandList.size()-1;
				double tmp =oprandList.get(index);
				oprandList.remove(index);
				oprandList.add(opMULDIV.eval(tmp,number2.value));				
			}
		}
		for(Double tmp:oprandList){
			value += tmp;
		}
		
		return true;
	
	}
}
