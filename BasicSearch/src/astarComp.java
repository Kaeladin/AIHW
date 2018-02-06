import java.util.Comparator;

public class astarComp  implements Comparator<Node>{
	
	@Override
	public int compare(Node a, Node b) {
		int af=a.h+a.g;
		int bf=b.h+b.g;
		
		if(af==bf) {
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
		if(af<bf)
			return -1;
		if(af>bf)
			return 1;
		return 0;
	}

}
