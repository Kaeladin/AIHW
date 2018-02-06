import java.util.Comparator;

public class bfsComp implements Comparator<Node> {

	@Override
	public int compare(Node a, Node b) {
		if(a.priority<b.priority) {
			return -1;
		}
		if (a.priority>b.priority) {
			return 1;
		}
		else {
			return 0;
		}
	}

}
