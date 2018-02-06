import java.util.Comparator;

public class dfsComp implements Comparator<Node> {

	@Override
	public int compare(Node a, Node b) {
		if(a.depth==b.depth) {
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
		if(a.depth>b.depth)
			return -1;
		if(a.depth<b.depth)
			return 1;
		return 0;
	}
}
