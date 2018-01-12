import java.util.ArrayList;

public abstract class Token {
 protected String name;
 protected double value;
 
 Token(){}
 
 Token(String name){
	 this.name  = name;
 }
 public abstract boolean parse(ArrayList<Key> cmd);
}
