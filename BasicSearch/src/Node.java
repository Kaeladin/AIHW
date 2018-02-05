import java.util.List;

public class Node {
	public String state;
	public List<Node> children; 
	public Node parent;
	public int g;
	public int h;
	
	public Node(String st) {
		state = st;
	}

	public void addChildren() {
		
	}

}
