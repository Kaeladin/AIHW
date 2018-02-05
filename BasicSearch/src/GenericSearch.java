import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class GenericSearch {

	public static String state;
	private static Scanner fileScan;
	public static void main(String[] args) throws FileNotFoundException {
		

		File testFile = new File("./src/tile1.txt");
		fileScan = new Scanner(testFile);
		state = fileScan.next();
		Queue<Node> fringe = null;
		String strategy= "BFS";
		if(strategy.equals("BFS")){
			 fringe= new LinkedList<Node>();
		}
		ArrayList<String> visited= new ArrayList<String>();
		
		Node root = new Node(state);
		fringe.add(root);
		
		while(!fringe.isEmpty()) {
			Node n = fringe.poll();
			System.out.println(n.toString());
		
			//System.out.println(visited);
			if(isGoal(n)) {
				System.out.println(path(n,0));
				break;
			}
			else if(!visited.contains(n.state)){			
		 
				List<Node> s = successors(n);
				fringe.addAll(s);	
				visited.add(n.state);
			}
			
		}
		
	}
	
	public static String path(Node n, int step) {
		
		String path="";
		if(n.parent!=null) {
			step++;
			path=path.concat(path(n.parent,step)+"Step "+step+": "+n.toString()+"\n");

		}
		
		else {
			return "Step 0: "+n.toString()+'\n';
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
					succs.add(m);	
			}
		}
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
