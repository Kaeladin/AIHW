import java.util.List;

public class Node implements Comparable<Node>{
	public String state;
	public List<Node> children; 
	public Node parent;
	public int depth;
	public int g;
	public int h;
	public int move;
	public int priority;
	public int cost;
	
	public Node(String st) {
		state = st;
		move=-1;
		g=0;
		depth=0;
		priority=0;
		cost=0;
		h=tilePlacementCost(state);
	}
	
	public int tilePlacementCost(String state) {
		int errors=0;
		int numB=state.length()-state.replace("B", "").length();
		char[] stateChars=state.toCharArray();
		for(int i=0; i<stateChars.length; i++) {
			
				if(i<numB && stateChars[i]!='B')
					errors++;
				if(i>numB && stateChars[i]!='W')
					errors++;
				if(i==numB && stateChars[i]!='x')
					errors++;
		}
		return errors;
	}

	public String toString() {
		String cstring="";
		if(cost!=0) {
			cstring = "(c="+cost+")";
		}
		if(move>=0) {
			return "Move "+move+" "+state+" "+cstring;
		}
		else return state+cstring;
	}

	@Override
	public int compareTo(Node b) {
		return 0;//return this.state.compareTo((Node) arg0.state);
	}
}
