import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class GenericSearch {

	public static String state;
	private static Scanner fileScan;
	public static void main(String[] args) throws FileNotFoundException {
		

		readFile();
		String strategy= "DFS";
		
		search(state,strategy);
		
	}

	private static void search( String state, String strategy) {
		int priority = 0;
			
		
		Comparator<Node> comp = null;
		switch (strategy) {
		case "BFS":	
			comp = new bfsComp();
			break;
		case "DFS":
			comp = new dfsComp();
			break;
		case "UCS":
			comp = new ucsComp();
			break;
		case "GS":
			comp = new gsComp();
			break;
		case "A-star":
			comp = new astarComp();
			break;
		}
		PriorityQueue<Node> fringe = new PriorityQueue<>(50,comp);
		
		
		
		ArrayList<String> visited= new ArrayList<String>();
		
		Node root = new Node(state);
		fringe.add(root);
		priority++;
		root.priority=priority;
		
		while(!fringe.isEmpty()) {
			Node n = fringe.poll();
		
			if(isGoal(n)) {
				System.out.println(n.toString());
				System.out.println(path(n,strategy));
				break;
			}
			else if(!visited.contains(n.state)){			
				System.out.println(n.toString());

				List<Node> s = successors(n);
				visited.add(n.state);
				for(Node succ : s) {
					if(!visited.contains(succ.state)) {
						priority++;
						succ.priority=priority;
						fringe.add(succ);
					}
				}//for all successor states			
			}//if haven't expanded node
		}//while there are unexpanded nodes
	}//search algorithm

	private static void readFile() throws FileNotFoundException {
		File testFile = new File("./src/tile1.txt");
		fileScan = new Scanner(testFile);
		state = fileScan.next();
	}
	
	public static String path(Node n, String strategy) {
		String path="";
		if(n.parent!=null) {
			path=path.concat(path(n.parent, strategy)+"Step "+n.g+": "+n.toString()+"\n");
		}
		
		else {
			return "\nFinal Result for "+strategy+":\nStep 0: "+n.toString()+'\n';
		}
		return path;
	}
	
	
	public static List<Node> successors(Node n){
		List<Node> succs = new ArrayList<Node>();
		String st=n.state;
		int blankIndex = 0;
		char[] stateChars = st.toCharArray();
		for(int i=0; i<stateChars.length; i++) {
			if(stateChars[i]=='x') {
				blankIndex=i;
			}
		}
		for(int i=0; i<stateChars.length; i++) {
			if(i!=blankIndex){		
					Node m = new Node(move(i,st));
					m.move=i;
					m.parent=n;
					m.g=n.g+1;
					succs.add(m);	
			}
		}
		n.children=succs;
		return succs;
	}
	
	public static boolean isGoal(Node n)
	{		
		boolean expectWhite = false;
		char[] stateChars =  n.state.toCharArray();
		for(int i=0; i<stateChars.length; i++) {
			if(expectWhite && stateChars[i]=='B')
				return false;
			if(!expectWhite && stateChars[i]=='W')
				return false;
			if(stateChars[i]=='x')
				expectWhite=true;
		}		
		return true;
	}//Goal test
	
	public static String move(int x, String state) {
		int blankIndex = 0;
		
		char[] stateChars = state.toCharArray();
		for(int i=0; i<stateChars.length; i++) {
			if(stateChars[i]=='x') {
				blankIndex=i;
			}
		}
		
		stateChars[blankIndex]=stateChars[x];
		stateChars[x]='x';
		return new String(stateChars);
	}

	
	
}


