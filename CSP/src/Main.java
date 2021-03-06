package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import org.omg.CORBA.Current;

public class Main {
	
	public static int branchnum=0;
	public static void main(String[] args) throws FileNotFoundException{
		
		boolean forwardChecking;
		File varFile = new File(args[0]) ;
		  File conFile = new File(args[1]);
		 if(args[2].contains("none"))
		  	forwardChecking = false;
		 else
		  	forwardChecking = true;
		 
		
		/*
		
		boolean forwardChecking=true;

		File varFile = new File("./src/ex3.var1.txt");
		File conFile = new File("./src/ex3.con.txt");
*/
		
		
		Scanner varScan = new Scanner(varFile);
		Scanner conScan = new Scanner(conFile);
		ArrayList<Variable> varList = initVars(varScan);
		initConsts(conScan,varList);

		State currState = new State(varList);
		recBackTracking(currState,forwardChecking);
	
		varScan.close();
		conScan.close();
	}
	
	
	private static State recBackTracking(State currState, boolean forwardChecking) {
		if(!checkConstraints(currState)){
				return null;
		}
		State result;
		if(currState.numSet==currState.variableList.size()) {
			branchnum++;
			System.out.print(branchnum+". "+currState.assignment);
			System.out.println("  solution");
			System.exit(0);
		}
		
		if(forwardChecking) {
			currState=updateLegal(currState);
		}
		Variable chosen = chooseVariable(currState);
		
		
		if(chosen == null){
			return null;
		}
		
		
	//	System.out.println(currState==nextState);
		//System.out.println(currState.variableList==nextState.variableList);
		while(chosen.legalValues.size()>0) {
			State nextState = new State(currState);
			//System.out.println(chosen.name+chosen.legalValues);
		
			int value = chooseValue(chosen, nextState);
			//System.out.println(chosen.name+ "="+value);
			
			
			chosen.value = value;
			chosen.valueSet = true;
			nextState.updateAssgn(chosen);
			nextState.set(chosen);
			nextState.numSet++;
			
			if (forwardChecking) {
				nextState=updateLegal(nextState);
			}
			
			result=recBackTracking(nextState, forwardChecking);

			if(forwardChecking) {
				

				for (Variable var: nextState.variableList) {
					if(var.legalValues.size()==0) {
						branchnum++;
						System.out.print(branchnum+". "+nextState.assignment);
						System.out.println("  failure");
						chosen.valueSet=false;
						nextState.cleanAssgn(chosen);
						nextState.set(chosen);
						nextState.numSet--;
					}
				}
			}

		
			
			if(result==null && !forwardChecking) {
				//System.out.println(chosen.name+"="+value+" is not legal assignment.");
				branchnum++;
				System.out.print(branchnum+". "+nextState.assignment);
				if(chosen.legalValues.contains(value)) {
					chosen.legalValues.remove(chosen.legalValues.indexOf(value));
				}
				chosen.valueSet=false;
				nextState.cleanAssgn(chosen);
				nextState.set(chosen);
				nextState.numSet--;
				System.out.println("  failure");

			}
			else if (chosen.legalValues.contains(value)){
				chosen.legalValues.remove(chosen.legalValues.indexOf(value));
				
			}	
		}
		
	//	System.out.println("curr"+currState);
		//System.out.println("next"+nextState);
		//chosen.legalValues=chosen.possibleValues;
		//currState.unSet(chosen);

		return currState;
	}

	private static void initConsts(Scanner conScan, ArrayList<Variable> varList ) {
		while(conScan.hasNextLine()){
			String constraint = conScan.nextLine();
			String[] parts = constraint.split(" ");

			for(int i=0; i<varList.size(); i++){
				if (parts[0].contains(varList.get(i).name) || parts[2].contains(varList.get(i).name)){
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
		boolean legal=true;
		String [] cArr;
		for (Variable var : state.variableList) {
			if(var.valueSet) {
				for(String constr : var.constraints) {
					
					if (constr.contains(">")) {
						cArr=constr.split(">");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name) && other.valueSet) {
									legal=var.value>other.value;
									if(!legal) return legal;
								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name) && other.valueSet) {
									legal=var.value<other.value;
									if(!legal) return legal;

								}
							}
						}
					}
					
					if (constr.contains("<")) {
						cArr=constr.split("<");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name) && other.valueSet) {
									legal=var.value<other.value;
									if(!legal) return legal;

								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name) && other.valueSet) {
									legal=var.value>other.value;
									if(!legal) return legal;

								}
							}
						}
					}
					
					if (constr.contains("=")) {
						cArr=constr.split("=");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name) && other.valueSet) {
									legal=var.value==other.value;
									if(!legal) return legal;

								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name) && other.valueSet) {
									legal=var.value==other.value;
									if(!legal) return legal;

								}
							}
						}
					}
					
					if (constr.contains("!")) {
						cArr=constr.split("!");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name) && other.valueSet) {
									legal=var.value!=other.value;
									if(!legal) return legal;

								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name) && other.valueSet) {
									legal=var.value!=other.value;
									if(!legal) return legal;

								}
							}
						}
					}
				}	
			}
		}
		
		return legal;
	}

 	public static State updateLegal(State state){
 		
 		boolean legal=true;
		String [] cArr;
		for (Variable var : state.variableList) {
			if(var.valueSet) {
				for(String constr : var.constraints) {
					
					if (constr.contains(">")) {
						cArr=constr.split(">");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name)) {
									for (int i=0; i<other.legalValues.size(); i++) {
										legal=var.value>other.legalValues.get(i);
										if(!legal) {
											other.legalValues.remove(i);
											state.set(other);
											//return state;
										}
									}
								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name)) {
									for (int i=0; i<other.legalValues.size(); i++) {
										legal=var.value<other.legalValues.get(i);
										if(!legal) {
											other.legalValues.remove(i);
											state.set(other);
											//return state;
										}
									}

								}
							}
						}
					}
					
					if (constr.contains("<")) {
						cArr=constr.split("<");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name)) {
									for (int i=0; i<other.legalValues.size(); i++) {
										legal=var.value<other.legalValues.get(i);
										if(!legal) {
											other.legalValues.remove(i);
											state.set(other);
											//return state;
										}
									}

								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name)) {
									for (int i=0; i<other.legalValues.size(); i++) {
										legal=var.value>other.legalValues.get(i);
										if(!legal) {
											other.legalValues.remove(i);
											state.set(other);
											//return state;
										}
									}
								}
							}
						}
					}
					
					if (constr.contains("=")) {
						cArr=constr.split("=");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name)) {
									for (int i=0; i<other.legalValues.size(); i++) {
										legal=var.value==other.legalValues.get(i);
										if(!legal) {
											other.legalValues.remove(i);
											state.set(other);
											//return state;
										}
									}

								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name)) {
									for (int i=0; i<other.legalValues.size(); i++) {
										legal=var.value==other.legalValues.get(i);
										if(!legal) {
											other.legalValues.remove(i);
											state.set(other);
											//return state;
										}
									}

								}
							}
						}
					}
					
					if (constr.contains("!")) {
						cArr=constr.split("!");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name)) {
									for (int i=0; i<other.legalValues.size(); i++) {
										legal=var.value!=other.legalValues.get(i);
										if(!legal) {
											other.legalValues.remove(i);
											state.set(other);
											//return state;
										}
									}

								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name)) {
									for (int i=0; i<other.legalValues.size(); i++) {
										legal=var.value!=other.legalValues.get(i);
										if(!legal) {
											other.legalValues.remove(i);
											state.set(other);
											//return state;
										}
									}

								}
							}
						}
					}
				}	
			}
		}
		
		return state;
 		


	}

	public static int chooseValue(Variable var, State curr){
		int val =Integer.MAX_VALUE ;
		int index = -1;
		int min;


		for(int i=0; i<var.legalValues.size(); i++){
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
		
		boolean legal;
		String [] cArr;
		int illegalCt =0;
				for(String constr : var.constraints) {
					
					if (constr.contains(">")) {
						cArr=constr.split(">");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name) && !other.valueSet) {
									for (int option : other.possibleValues) {
										legal=val>option;
										if(!legal) illegalCt++;
									}
								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name) && !other.valueSet) {
									for (int option : other.possibleValues) {
										legal=val<option;
										if(!legal) illegalCt++;
									}

								}
							}
						}
					}
					
					if (constr.contains("<")) {
						cArr=constr.split("<");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name) && !other.valueSet) {
									for (int option : other.possibleValues) {
										legal=val<option;
										if(!legal) illegalCt++;
									}			
								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name) && !other.valueSet) {
									for (int option : other.possibleValues) {
										legal=val>option;
										if(!legal) illegalCt++;
									}
								}
							}
						}
					}
					
					if (constr.contains("=")) {
						cArr=constr.split("=");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name) && !other.valueSet) {
									for (int option : other.possibleValues) {
										legal=val==option;
										if(!legal) illegalCt++;
									}
								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name) && !other.valueSet ) {
									for (int option : other.possibleValues) {
										legal=val==option;
										if(!legal) illegalCt++;
									}
								}
							}
						}
					}
					
					if (constr.contains("!")) {
						cArr=constr.split("!");
						
						if(cArr[0].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[1].contains(other.name) && !other.valueSet) {
									for (int option : other.possibleValues) {
										legal=val!=option;
										if(!legal) illegalCt++;
									}
								}
							}
						}
						
						else if(cArr[1].contains(var.name)) {
							for (Variable other: state.variableList) {
								if(cArr[0].contains(other.name) && !other.valueSet ) {
									for (int option : other.possibleValues) {
										legal=val!=option;
										if(!legal) illegalCt++;
									}
								}
							}
						}
					}
				}	
			
		

		//System.out.println(val+": "+illegalCt);
		return illegalCt;
	}
	
	public static Variable chooseVariable(State current) {
		
		Variable mostConstrained=null;
		
		for (Variable var: current.variableList) {
			if(!var.valueSet) {
				//System.out.println(var);
				if(mostConstrained==null) {
					mostConstrained=var;
					//System.out.println("1st: "+mostConstrained);


				}
				
				mostConstrained.numConstraining=numConstraining(mostConstrained,current);
				var.numConstraining=numConstraining(var,current);
				if (var.compareTo(mostConstrained)==-1) {
				mostConstrained=var;
			//	System.out.println("1st: "+mostConstrained);
				}
			
			}
		}
		return mostConstrained;
	}

	private static int numConstraining(Variable var, State current) {
		int numConst=0;
		for (String constraint : var.constraints) {
			for (Variable other: current.variableList) {
				if (!other.valueSet && constraint.contains(other.name)) {
					numConst++;
				}
			}
		}
		return numConst;
	}

	

}
