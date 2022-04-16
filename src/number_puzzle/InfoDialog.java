package number_puzzle;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class InfoDialog extends Dialog {

	private static final long serialVersionUID = 1L;

	InfoDialog(Frame parent, String title) {
		super(parent, title, false);
		setLayout(new FlowLayout());
		setSize(400, 170);

		add(new Label("How to play"));

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
			}
			public void windowDeactivated(WindowEvent we) {
				dispose();
			}
		});
	}

	public void paint(Graphics g) {
		g.drawString("Naciskaj¹c klocek znajduj¹cy siê w s¹siedztwie pustego pola,", 25, 80);
		g.drawString("przesunie sie on w jego miejsce. Gra jest skoñczona, kiedy klocki", 15, 92);
		g.drawString("w rzêdach s¹ poustawiane od lewego górnego rogu w rosn¹cym ", 15, 104);
		g.drawString("porz¹dku liczbowym, i dalej zaczynaj¹c od kolejnego rzêdu.", 15, 116);
		g.drawString("Autor: gerstel.tomasz@gmail.com", 25, 140);
	}

}

