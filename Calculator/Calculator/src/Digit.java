import java.util.ArrayList;
public class Digit extends Token {
    @Override
    public boolean parse(ArrayList<Key> cmd) {
        if (cmd.size() == 0)
            return false;
        
        char ch = (cmd.get(0).keyName.charAt(0));
        if (!Character.isDigit(ch))
            return false;
        value = ch-'0';
        cmd.remove(0); // remove the parsed key from the cmd for the terminal token
        return true;
    }
}