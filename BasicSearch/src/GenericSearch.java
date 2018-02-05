import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GenericSearch {

	public static String state;
	public static void main(String[] args) throws FileNotFoundException {

		File testFile = new File("./src/tile1.txt");
		Scanner fileScan = new Scanner(testFile);
		state = fileScan.next();

		Node root = new Node(state);
		System.out.println(successors(root).size());
	}
	
	public static List<Node> successors(Node n){
		List<Node> succs = new ArrayList<Node>();
		String st=n.state;
		char[] stateChars = st.toCharArray();
		for(int i=0; i<stateChars.length; i++) {
			if(stateChars[i]!='x') {
				succs.add(new Node(move(i,st)));
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
