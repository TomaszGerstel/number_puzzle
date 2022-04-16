package number_puzzle;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class EndgameDialog extends Dialog {

	private static final long serialVersionUID = 1L;

	EndgameDialog(Frame parent, String title) {
		super(parent, title, false);
		setLayout(new FlowLayout());
		setSize(200, 120);

		add(new Label("Wygrana!!!"));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		});

	}
	public void paint(Graphics g) {
		g.drawString("wykonano: " + Puzzle.moveCounter + " ruchów", 20, 80);
		g.drawString("w czasie: " + getTime(Puzzle.gameTime), 20, 100);
	}
	
	public String getTime(long millis) {
		if(millis/1000 <= 60) return String.valueOf(millis/1000).concat(" sekund"); 
		return timeInMinutes(millis/1000);		
	
	}
	public String timeInMinutes(long millis) {
		int minutes = (int) (millis/60);
		int seconds = (int) (millis - (minutes*60));
		return minutes + " minut i " + seconds + " sekund";
	}
	
}
