package number_puzzle;

import java.awt.Button;
import java.util.Comparator;

public class CompareX implements Comparator<Button>{
	
	@Override
	public int compare(Button b1, Button b2) {
		Integer b1X = b1.getX();
		Integer b2X = b2.getX();
		return b1X.compareTo(b2X);
	}

}