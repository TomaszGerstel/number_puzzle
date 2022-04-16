package number_puzzle;

import java.awt.Button;
import java.util.Comparator;

public class CompareY implements Comparator<Button>{
	
	@Override
	public int compare(Button b1, Button b2) {
		Integer b1Y = b1.getY();
		Integer b2Y = b2.getY();
		return b1Y.compareTo(b2Y);
	}

}
