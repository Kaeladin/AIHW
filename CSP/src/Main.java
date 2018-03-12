package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import org.omg.CORBA.Current;


public class Main {

	static Stack<State> fringe = new Stack<State>();
	//static ArrayList<Variable> varList = new ArrayList<Variable>();

	public static void main(String[] args) throws FileNotFoundException{
		
		boolean forwardChecking=false;

		File varFile = new File("./src/ex1.var1.txt");
		File conFile = new File("./src/ex1.con.txt");

		Scanner varScan = new Scanner(varFile);
		Scanner conScan = new Scanner(conFile);
		ArrayList<Variable> varList = initVars(varScan);
		initConsts(conScan,varList);

		State currState = new State(varList);
		backtrackingSearch(currState,forwardChecking);
	
		varScan.close();
		conScan.close();
	}
	
	private static State backtrackingSearch(State currState, boolean forwardChecking) {
		State solution;
		solution=recBackTracking(currState, forwardChecking);
		
		return solution;
		
	}

	private static State recBackTracking(State currState, boolean forwardChecking) {
		
		State result;
		if(currState.numSet==currState.variableList.size()) {
			return currState;
		}
		
		Variable chosen = chooseVariable(currState);
		
		if(chosen == null){
			System.out.println("failure");
			return null;
		}
		System.out.print(chosen.name+ " ");

		int value = chooseValue(chosen, currState);
		System.out.println(value);
		chosen.value = value;
		chosen.valueSet = true;
		currState.set(chosen);
		currState.numSet++;
		result=recBackTracking(currState, forwardChecking);
		
		if(result!=null)
			return result;
		if(!checkConstraints(currState)){
			System.out.println("failure");
			return null;
		}
		//UPDATE LEGAL VALUES 
		if(forwardChecking) {
			updateLegal(chosen, value, currState);
		}
		//State nextState = new State(currState);
	//	nextState.count = currState.count + 1;

		
		
		
		return null;
	}

	private static void initConsts(Scanner conScan, ArrayList<Variable> varList ) {
		while(conScan.hasNextLine()){
			String constraint = conScan.nextLine();
			String[] parts = constraint.split(" ");

			for(int i=0; i<varList.size(); i++){
				//System.out.println(varList.get(i).name);
				if (parts[0].contains(varList.get(i).name) || parts[2].contains(varList.get(i).name)){
					//System.out.println("hi");
					varList.get(i).constraints.add(constraint);
				}

			}
		}
	}

	private static ArrayList<Variable> initVars(Scanner varScan) {
		
		ArrayList<Variable> varList= new ArrayList<>();
		while(varScan.hasNextLine()){
			String varName = varScan.next();
			varName = varName.substring(0, varName.length() - 1);
			String line = varScan.nextLine();
			//System.out.println(line);
			char[] input = line.toCharArray();
			Variable tempVar = new Variable();
			tempVar.name = varName;
			for(int i=1; i<input.length; i+=2){
				tempVar.possibleValues.add(Integer.parseInt(Character.toString(input[i])));
			}
			tempVar.legalValues = tempVar.possibleValues;
			tempVar.valueSet = false;
			varList.add(tempVar);

		}
		return varList;
	}
	
	public static boolean checkConstraints(State state){
		ArrayList<Variable> varList = state.variableList;
		boolean legal = false;
		for(int i=0; i<varList.size(); i++){
			for (int j=0; j<varList.get(i).legalValues.size(); j++){
				
				if(varList.get(i).valueSet == true && varList.get(i).legalValues.get(j) == varList.get(i).value)
					legal = true;
				else if(varList.get(i).valueSet == false)
					legal = true;
				
			}
			if(legal == false)
				return legal;
		}
		return legal;
	}

 	public static void updateLegal(Variable var, int val, State state){
 		
 		ArrayList<Variable> varList=state.variableList;
		String constraint;
		String [] constrArr;

		for(int k=0; k<var.constraints.size(); k++){
			constraint = var.constraints.get(k);

			if(constraint.contains(">")){
				constrArr = constraint.split(">");

				if(constrArr[0].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[1].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j> val)
									varList.get(i).legalValues.remove(j);
							}
						}
					}
				}
				else if(constrArr[1].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[0].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j< val)
									varList.get(i).legalValues.remove(j);
							}
						}
					}

				}

			}
			else if(constraint.contains("<")){
				constrArr = constraint.split("<");
				if(constrArr[0].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[1].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j< val)
									varList.get(i).legalValues.remove(j);
							}
						}
					}
				}
				else if(constrArr[1].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[0].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j> val)
									varList.get(i).legalValues.remove(j);
							}
						}
					}

				}
			}
			else if(constraint.contains("!")){
				constrArr = constraint.split("!");
				if(constrArr[0].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[1].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j== val)
									varList.get(i).legalValues.remove(j);
							}
						}
					}
				}
				else if(constrArr[1].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[0].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j== val)
									varList.get(i).legalValues.remove(j);
							}
						}
					}

				}

			}
			else if(constraint.contains("=")){
				constrArr = constraint.split("=");
				if(constrArr[0].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[1].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j != val)
									varList.get(i).legalValues.remove(j);
							}
						}
					}
				}
				else if(constrArr[1].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[0].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j!= val)
									varList.get(i).legalValues.remove(j);
							}
						}
					}

				}

			}
		}



	}

	public static int chooseValue(Variable var, State curr){
		int val =Integer.MAX_VALUE ;
		int index = -1;
		int min;


		for(int i=0; i<var.legalValues.size(); i++){
			//System.out.println(var.legalValues.get(i));
			min = numConstraining(var, var.legalValues.get(i),curr);
			if(min < val){
				index = i;
				val = min;
			}
			//least constraining value

		}
		return var.legalValues.get(index);
	}

	public static int numConstraining(Variable var, int val, State state){
		
		ArrayList<Variable> varList= state.variableList;
		String constraint;
		String [] constrArr;
		int illegalCt =0;
		for(int k=0; k<var.constraints.size(); k++){
			constraint = var.constraints.get(k);

			if(constraint.contains(">")){
				constrArr = constraint.split(">");

				if(constrArr[0].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[1].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j> val)
									illegalCt++;
							}
						}
					}
				}
				else if(constrArr[1].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[0].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j< val)
									illegalCt++;
							}
						}
					}

				}

			}
			else if(constraint.contains("<")){
				constrArr = constraint.split("<");
				if(constrArr[0].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[1].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j< val)
									illegalCt++;
							}
						}
					}
				}
				else if(constrArr[1].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[0].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j> val)
									illegalCt++;
							}
						}
					}

				}
			}
			else if(constraint.contains("!")){
				constrArr = constraint.split("!");
				if(constrArr[0].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[1].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j== val)
									illegalCt++;
							}
						}
					}
				}
				else if(constrArr[1].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[0].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j== val)
									illegalCt++;
							}
						}
					}

				}

			}
			else if(constraint.contains("=")){
				constrArr = constraint.split("=");
				if(constrArr[0].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[1].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j != val)
									illegalCt++;
							}
						}
					}
				}
				else if(constrArr[1].contains(var.name)){
					for(int i=0; i<varList.size();i++){
						if(constrArr[0].contains(varList.get(i).name)){
							for(int j =0 ; j<varList.get(i).legalValues.size(); j++){
								if(j!= val)
									illegalCt++;
							}
						}
					}

				}

			}
		}


		return illegalCt;
	}

	public static int howConstrained(Variable var) {
		return var.constraints.size();
		 
	}
	
	
	public static Variable chooseVariable(State current) {
		if(current.numSet==current.variableList.size()) {
			System.out.println("All set.");
			return null;
		}
		Variable mostConstrained=null;
		
		for (Variable var: current.variableList) {
			if(!var.valueSet) {
				//System.out.println(var);
				if(mostConstrained==null) {
					mostConstrained=var;
					System.out.println("1st: "+mostConstrained);


				}
				if (var.compareTo(mostConstrained)==-1) {
				mostConstrained=var;
				System.out.println("1st: "+mostConstrained);
				}
			
			}
		}
		return mostConstrained;
	}
	
	/*
	public static Variable chooseVariable2(State current){
		
		//State current = fringe.peek();
		//check all variables to see which has fewest legal values
		int mostConstrained = -1;
		for(int i=0; i< current.variableList.size(); i++){
			
			if(current.variableList.get(i).valueSet == true){
				if(mostConstrained == -1 && i == current.variableList.size()-1)
					return null;
			}

			else if(mostConstrained == -1 || current.variableList.get(i).legalValues.size() < current.variableList.get(mostConstrained).legalValues.size()){
				mostConstrained = i;
				System.out.println("1st: "+current.variableList.get(mostConstrained));

			}
			if(current.variableList.get(i).valueSet != true && current.variableList.get(i).legalValues.size() == current.variableList.get(mostConstrained).legalValues.size()){
				//System.out.println(current.variableList.get(i).name);
				if(current.variableList.get(mostConstrained).valueSet == true || current.variableList.get(i).name.compareTo(current.variableList.get(mostConstrained).name) < 0 ){
					mostConstrained = i;
					System.out.println("2nd: "+mostConstrained+current.variableList.get(mostConstrained).legalValues.size());

				}

			}
			
		}
		return current.variableList.get(mostConstrained);
	}
	*/
	

}
