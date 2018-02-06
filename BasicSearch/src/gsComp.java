import java.util.Comparator;

public class gsComp  implements Comparator<Node>{
	
	@Override
	public int compare(Node a, Node b) {
		if(a.h==b.h) {
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
		if(a.h<b.h)
			return -1;
		if(a.h>b.h)
			return 1;
		return 0;
	}

}
