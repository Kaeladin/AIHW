package src;
import java.util.ArrayList;

public class Variable implements Comparable <Variable>{
	String name;
	ArrayList<String> constraints = new ArrayList<String>();
	ArrayList<Integer> possibleValues = new ArrayList<Integer>();
	ArrayList<Integer> legalValues = new ArrayList<Integer>();
	int numConstraining;
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
	
	@Override
	public int compareTo(Variable var) {
		if(this.possibleValues.size()<var.possibleValues.size()) {
			return -1;
		}
		
		else if(this.possibleValues.size()>var.possibleValues.size()) {

			return 1;

		}
		
		else {
			if(this.numConstraining>var.numConstraining) {
				return -1;
			}
			
			else if(this.numConstraining<var.numConstraining) {
				return 1;
			}
			
			else {
			
				return this.name.compareTo(var.name);
			}
		}
		
	}

	
	

}