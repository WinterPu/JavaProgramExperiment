import java.util.ArrayList;

public class DecPoint extends Token {

	@Override
	public boolean parse(ArrayList<Key> cmd) {
		// TODO Auto-generated method stub
        if (cmd.size() == 0)
            return false;
        String symbol = (cmd.get(0).keyName);
        if (!symbol.equals("."))
            return false;
        cmd.remove(0); // remove the parsed key from the cmd for the terminal token
        return true;
	}

}
