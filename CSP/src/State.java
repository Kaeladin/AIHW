package src;
import java.util.ArrayList;

public class State {
	ArrayList<Variable> variableList;
	int count;
	int numSet;
	String assignment;
	
	public State(ArrayList<Variable> variableArray){
		this.variableList = variableArray;
		this.numSet=0;
		assignment="";
	}
	

	
	public State(State currState) {
		this.variableList=currState.variableList;
		this.count=currState.count;
		this.numSet=currState.numSet;
		this.assignment=currState.assignment;
	}


	public String toString() {
		String out="";
		for (Variable var : variableList) {
			out=out+var.toString()+'\n';
		}
		return out;
		
	}
	
	public void unSet(Variable updated) {
		for(int i=0; i<this.variableList.size(); i++) {
			Variable var=this.variableList.get(i);
			if(var.name.equals(updated.name)) {
				updated.valueSet=false;
				this.variableList.set(i, updated);
				if(this.numSet!=1)
					this.assignment=this.assignment.substring(0, this.assignment.indexOf(var.name)-2);
				else
					this.assignment="";
			}
			
		}
	}
	
	public void set(Variable updated) {

		for(int i=0; i<this.variableList.size(); i++) {
			Variable var=this.variableList.get(i);
			if(var.name.equals(updated.name)) {
				if(!this.assignment.equals("")) {
					this.assignment=this.assignment+", ";
				}
				this.assignment=this.assignment+updated.name+"="+updated.value;
				this.variableList.set(i, updated);
		
			}
			
		}
	}
}
