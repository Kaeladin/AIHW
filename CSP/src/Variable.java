package src;
import java.util.ArrayList;

public class Variable {
	String name;
	ArrayList<String> constraints = new ArrayList<String>();
	ArrayList<Integer> possibleValues = new ArrayList<Integer>();
	ArrayList<Integer> legalValues = new ArrayList<Integer>();
	int value;
	boolean valueSet;

	@Override
	public String toString(){
		String descript = "";
		descript += name;
		for(int i =0; i< possibleValues.size(); i++){
			descript += " "+ possibleValues.get(i);
		}
		return descript;
	}
}