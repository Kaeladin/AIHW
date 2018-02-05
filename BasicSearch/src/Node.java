import java.util.List;

public class Node{
	public String state;
	public List<Node> children; 
	public Node parent;
	public int g;
	public int h;
	public int move;
	
	public Node(String st) {
		state = st;
		move = -1;
	}

	public void addChildren() {
		
	}

	
}
