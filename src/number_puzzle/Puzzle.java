package number_puzzle;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Puzzle extends Frame implements ActionListener {
	/**
		**/
	private static final long serialVersionUID = 1L;
	private String[] str = new String[1];
	private int n = 4;
	private static Dimension appSize;
	private ArrayList<Integer> currentButtonOrder;
	private ArrayList<Integer> sortedButtonNumbers;
	private ArrayList<Button> buttonList;
	private CompareY compareY;
	private Comparator<Button> compYThenX;
	private long startTime;
	private long endTime;
	public static long gameTime;

	private Font barFont;

	private EndgameDialog endDialog;
	private InfoDialog infoDialog;
	static int moveCounter;
	double h = appSize.getHeight();

	private MenuItem item1, item2, item3, item21, item22, item23;

	private Point actualEmptyFieldLocation;

	public Puzzle(int selectedSize) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width / 2) - (appSize.width / 2), (screenSize.height / 2) - (appSize.height / 2));
		this.n = selectedSize;
		setLayout(new GridLayout(n, n));
		setFont(new Font("SansSerif", Font.BOLD, 24));
		endDialog = new EndgameDialog(this, "Number Puzzle");
		endDialog.setLocationRelativeTo(this);
		infoDialog = new InfoDialog(this, "Number Puzzle");
		infoDialog.setLocationRelativeTo(this);

		moveCounter = 0;

		barFont = new Font("Dialog", Font.BOLD, 12);

		currentButtonOrder = new ArrayList<Integer>();
		sortedButtonNumbers = new ArrayList<Integer>();
		buttonList = new ArrayList<Button>();
		compareY = new CompareY();
		compYThenX = compareY.thenComparing(new CompareX());
		actualEmptyFieldLocation = new Point();

		MenuBar mbar = new MenuBar();
		setMenuBar(mbar);
		Menu options = new Menu("Menu");
		Menu size = new Menu("Size");
		mbar.setFont(new Font("Dialog", Font.ITALIC, 12));

		options.add(item1 = new MenuItem("Reset"));
		options.add(item2 = new MenuItem("How to play"));
		options.add(item3 = new MenuItem("Exit"));
		size.add(item21 = new MenuItem("4x4"));
		size.add(item22 = new MenuItem("5x5"));
		size.add(item23 = new MenuItem("6x6"));
		mbar.add(options);
		mbar.add(size);

		item1.addActionListener(this);
		item2.addActionListener(this);
		item3.addActionListener(this);
		item21.addActionListener(this);
		item22.addActionListener(this);
		item23.addActionListener(this);

		addButtons();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}

			public void windowOpened(WindowEvent we) {
				try {
					shuffleButtons();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			public void windowDeiconified(WindowEvent we) {
				dispose();
				str[0] = "" + n;
				main(str);
			}
		});
	}

	public static void main(String[] args) {
		int size;
		if (args.length > 0)
			size = Integer.valueOf(args[0]);
		else
			size = 4;
		appSize = new Dimension(450, 330);
		Puzzle appwin = new Puzzle(size);
		appwin.setSize(appSize);
		appwin.setTitle("Number Puzzle");
		appwin.setVisible(true);
		appwin.setResizable(false);
		// resetuje sie podczas resizable i minimalizacji
	}

	public Insets getInsets() {
		return new Insets(50, 10, 30, 10);
	}

	public void addButtons() {
		for (int i = 1; i <= (n * n) - 1; i++) {
			sortedButtonNumbers.add(i);
			Button b = new Button("" + i);
			add(b);
			b.addActionListener(this);
			if (Integer.valueOf(b.getLabel()) % 2 == 0)
				b.setBackground(new Color(255, 213, 0));
			else
				b.setBackground(new Color(0, 91, 187));
			buttonList.add(b);
		}
		buttonList.sort(compYThenX);
	}

	public void shuffleButtons() throws InterruptedException {

		Random random = new Random();
		int r = random.nextInt(200) + 100;

		for (int i = 0; i < r; i++) {
			for (Button button : buttonList) {
				if (isButtonAdjacentToEmptyField(button)) {
					Thread.sleep(4);
					button.setLocation(getEmptyFieldLocation());
				}
			}
			Collections.shuffle(buttonList);
		}
	}

	public void paint(Graphics g) {
		g.setFont(barFont);
		g.drawString("liczba ruchów: " + moveCounter, 12, (int) h - 15);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String arg = e.getActionCommand();

		switch (arg) {
		case "Exit":
			System.exit(0);
			break;
		case "Reset": {
			str[0] = "" + n;
			dispose();
			main(str);
			break;
		}
		case "How to play": {
			infoDialog.setVisible(true);
			infoDialog.repaint();
			break;
		}
		case "4x4": {
			str[0] = "4";
			dispose();
			main(str);
			break;
		}
		case "5x5": {
			str[0] = "5";
			dispose();
			main(str);
			break;
		}
		case "6x6": {
			str[0] = "6";
			dispose();
			main(str);
			break;
		}
		}

		currentButtonOrder.clear();
		for (Button button : buttonList) {
			if (e.getSource() == button && isButtonAdjacentToEmptyField(button)) {
				button.setLocation(getEmptyFieldLocation());
				moveCounter += 1;
				repaint();
			}
			currentButtonOrder.add(Integer.valueOf(button.getLabel()));
		}
		buttonList.sort(compYThenX);

		if (moveCounter == 1)
			startTime = new Date().getTime();
		if (gameFinished())
			endGame();
	}

	public boolean isButtonAdjacentToEmptyField(Button button) {

		actualEmptyFieldLocation = getEmptyFieldLocation();
		int emptyFieldX = (int) actualEmptyFieldLocation.getX();
		int emptyFieldY = (int) actualEmptyFieldLocation.getY();

		if (isButtonHorizontallyNextToEmpty(button, emptyFieldX, emptyFieldY)
				|| isButtonVerticallyNextToEmpty(button, emptyFieldX, emptyFieldY))
			return true;
		return false;
	}

	public boolean isButtonVerticallyNextToEmpty(Button button, int emptyFieldX, int emptyFieldY) {
		if ((button.getX() == emptyFieldX) && ((button.getY() - button.getHeight()) == emptyFieldY
				|| (button.getY() + button.getHeight()) == emptyFieldY))
			return true;
		return false;
	}

	public boolean isButtonHorizontallyNextToEmpty(Button button, int emptyFieldX, int emptyFieldY) {
		if ((button.getY() == emptyFieldY) && ((button.getX() - button.getWidth()) == emptyFieldX
				|| (button.getX() + button.getWidth()) == emptyFieldX))
			return true;
		return false;
	}

	public boolean gameFinished() {
		if (currentButtonOrder.equals(sortedButtonNumbers)
				&& (isEmptyFieldInEndPosition(getEmptyFieldX(), getEmptyFieldY())) && moveCounter > 0)
			return true;
		return false;
	}

	public void endGame() {
		endTime = new Date().getTime();
		gameTime = endTime - startTime;
		for (Button b : buttonList) {
			b.setEnabled(false);
		}
		endDialog.setVisible(true);
		endDialog.repaint();
	}

	public Point getEmptyFieldLocation() {
		Point emptyFieldLocation = new Point(getEmptyFieldX(), getEmptyFieldY());
		return emptyFieldLocation;
	}

	public int getEmptyFieldX() {
		ArrayList<Integer> allX = new ArrayList<Integer>();
		Set<Integer> allXValues = new HashSet<>();

		for (Button b : buttonList) {
			allX.add(b.getX());
			allXValues.add(b.getX());
		}
		return getEmptyFieldCoord(allX, allXValues);
	}

	public int getEmptyFieldY() {
		ArrayList<Integer> allY = new ArrayList<Integer>();
		Set<Integer> allYValues = new HashSet<>();

		for (Button b : buttonList) {
			allY.add(b.getY());
			allYValues.add(b.getY());
		}
		return getEmptyFieldCoord(allY, allYValues);
	}

	public int getEmptyFieldCoord(ArrayList<Integer> allCoords, Set<Integer> allCoordsValues) {

		int minCountValue = 0;
		int leastCoordValue = 0;

		for (Integer val : allCoordsValues) {
			int countValue = 0;
			for (Integer v : allCoords) {
				if (val.equals(v))
					countValue += 1;
			}
			if (countValue < minCountValue || minCountValue == 0) {
				minCountValue = countValue;
				leastCoordValue = val;
			}
		}
		return leastCoordValue;
	}

	public boolean isEmptyFieldInEndPosition(int x, int y) {

		int highestX = 0;
		int highestY = 0;
		for (Button b : buttonList) {
			if (b.getX() > highestX)
				highestX = b.getX();
			if (b.getY() > highestY)
				highestY = b.getY();
		}
		return ((getEmptyFieldX() == highestX) && (getEmptyFieldY() == highestY));
	}

}
