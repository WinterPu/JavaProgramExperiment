import java.util.ArrayList;

public class Operator extends Token {
	final int ADD = 1;
	final int SUB = 2;
	final int MUL = 3;
	final int DIV = 4;
	final int NEG = 0;
	
	String vaildSym;
	int opcode;
	
	public Operator (String ops){
		vaildSym =ops;
	}
	@Override
	public boolean parse(ArrayList<Key> cmd) {
		// TODO Auto-generated method stub
		String ops;
		if(cmd.size() ==0)
		  return false;
		
		ops = cmd.get(0).keyName;
		
		if(vaildSym.indexOf(ops)<0)
			return false;
		
		 if (ops.equals("+")) {
	            opcode = ADD;
	            cmd.remove(0); // remove the parsed key from the cmd for the terminal token
	        }
	        else if (ops.equals("-")) {
	            opcode = SUB;
	            cmd.remove(0); // remove the parsed key from the cmd for the terminal token
	        }
	        else if (ops.equals("x")) {
	            opcode = MUL;
	            cmd.remove(0); // remove the parsed key from the cmd for the terminal token
	        }
	        else if (ops.equals("¡Â")) {
	            opcode = DIV;
	            cmd.remove(0); // remove the parsed key from the cmd for the terminal token
	        }
	        else {
	            return false;
	        }
		 return true;
	}
	public double eval(double op1,double op2){
		double result  =0;
		switch(opcode)
		{
        case ADD:
            result = op1 + op2;
            break;
        case SUB:
            result = op1 - op2;
            break;
        case MUL:
            result = op1 * op2;
            break;
        case DIV:
            result = op1 / op2;
            break;
		
		}
		return result;
		
	}
}
