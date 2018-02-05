import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GenericSearch {

	public static String state;
	public static void main(String[] args) throws FileNotFoundException {

		File testFile = new File("./src/tile1.txt");
		Scanner fileScan = new Scanner(testFile);
		state = fileScan.next();

		
		
	}
	
	
	public static void move(int x) {
		
	}

}
