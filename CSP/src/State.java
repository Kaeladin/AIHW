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
	

	public State(State master){
		this.variableList = master.variableList;
		this.numSet=master.numSet;
		this.count=master.count;
	}
	public String toString() {
		String out="";
		for (Variable var : variableList) {
			out=out+var.toString()+'\n';
		}
		return out;
		
	}
	
	public void set(Variable updated) {
		updated.valueSet=true;
		for(int i=0;i<this.variableList.size(); i++) {
			if(this.variableList.get(i).name.equals(updated.name))
				this.variableList.set(i, updated);
		}
			
		
	}
}
