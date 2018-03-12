package src;
import java.util.ArrayList;

public class State {
	ArrayList<Variable> variableList;
	int count;
	int numSet;
	
	public State(ArrayList<Variable> variableArray){
		this.variableList = variableArray;
		this.numSet=0;
	}
	
	public String toString() {
		String out="";
		for (Variable var : variableList) {
			out=out+var.toString()+'\n';
		}
		return out;
		
	}
}
