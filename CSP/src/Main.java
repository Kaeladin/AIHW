package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Main {

	static Stack<State> fringe = new Stack<State>();
	static ArrayList<Variable> varList = new ArrayList<Variable>();

	public static void main(String[] args) throws FileNotFoundException{

		File varFile = new File("./src/ex1.var1.txt");
		File conFile = new File("./src/ex1.con.txt");

		Scanner varScan = new Scanner(varFile);
		Scanner conScan = new Scanner(conFile);

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

		State currState = new State(varList);
		fringe.add(currState);
		//end of file read-in


		//while...
		int count =0;
		while(!fringe.empty() && currState.count < varList.size()-1){
			currState = fringe.pop();
			//choose variable
			//most constrained variable heuristic (ties broken alphabetically)
			Variable chosen = chooseVariable(currState);
			if(chosen == null){
				System.out.println("failure");
				break;
			}
			System.out.print(chosen.name+ " ");

			//choose value
			//least constraining (ties broken with smaller val)
			int value = chooseValue(chosen, currState);
			System.out.println(value);

			chosen.value = value;
			chosen.valueSet = true;

			//UPDATE LEGAL VALUES 
			updateLegal(chosen, value);

			State nextState = new State(varList);
			nextState.count = currState.count + 1;
			fringe.add(nextState);

			if(!checkConstraints()){
				System.out.println("fail");
				break;
			}
			//add new state to fringe with updated variables (legal values)
			//add other new states with "non-optimal" values?



			//end while
		}	
		varScan.close();
		conScan.close();
	}
	
	public static boolean checkConstraints(){
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

 	public static void updateLegal(Variable var, int val){
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
			min = numConstraining(var, var.legalValues.get(i));
			if(min < val){
				index = i;
				val = min;
			}
			//least constraining value

		}
		return var.legalValues.get(index);
	}

	public static int numConstraining(Variable var, int val){
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

	public static Variable chooseVariable(State current){
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

			}
			if(current.variableList.get(i).valueSet != true && current.variableList.get(i).legalValues.size() == current.variableList.get(mostConstrained).legalValues.size()){
				//System.out.println(current.variableList.get(i).name);
				if(current.variableList.get(mostConstrained).valueSet == true || current.variableList.get(i).name.compareTo(current.variableList.get(mostConstrained).name) < 0 ){
					mostConstrained = i;

				}

			}

		}
		return current.variableList.get(mostConstrained);
	}

}
