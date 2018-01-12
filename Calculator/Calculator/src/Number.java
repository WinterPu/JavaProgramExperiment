import java.util.ArrayList;

public class Number extends Token {

	@Override
	public boolean parse(ArrayList<Key> cmd) {
		// TODO Auto-generated method stub
		Digit digitParser = new Digit();
		DecPoint decpointParser = new DecPoint();

		value = 0;
		boolean negative = false;
		boolean parseFlag = false;
		boolean changeBase =false;
		
		//check negative
		if (cmd.size() >= 2) {
			if (cmd.get(0).keyName.equals("-") && Character.isDigit(cmd.get(1).keyName.charAt(0)))
			{
				negative = true;
				cmd.remove(0);
			}
			else if (cmd.get(0).keyName.equals(".") && Character.isDigit(cmd.get(1).keyName.charAt(0)))
				parseFlag = true;
		}

		//check Whether the Key is "00"
		if(cmd.size()!= 0 && cmd.get(0).keyName.equals("00"))
			changeBase = true;
		while (digitParser.parse(cmd)) {
			if(changeBase)
				{
					value = 100 * value + digitParser.value;
					changeBase = false;
				}
			else 
				value = 10 * value + digitParser.value;
			
			if(cmd.size()!= 0 && cmd.get(0).keyName.equals("00"))
				changeBase = true;
			
			parseFlag = true;
		}
		
		//check Dec Point
		if (decpointParser.parse(cmd)) {
			int count = 1;
			
			if(cmd.size()!= 0 && cmd.get(0).keyName.equals("00"))
				changeBase = true;
			while (digitParser.parse(cmd)) {
				if(changeBase)
				{
					count++;
					changeBase = false;
				}
				value += Math.pow(0.1, count) * digitParser.value;
				count++;
				
				if(cmd.size()!= 0 && cmd.get(0).keyName.equals("00"))
					changeBase = true;
			}
		}
		
		if (negative)
			value = -value;
		return parseFlag;
	}

}
