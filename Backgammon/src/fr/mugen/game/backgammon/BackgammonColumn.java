package fr.mugen.game.backgammon;

public class BackgammonColumn {

	public enum Color {
		WHITE, BLACK, NONE
	}
	
	private Color color;
	private int number;
	private int position;

	public BackgammonColumn(int position) {
		this.position = position;
		this.color = Color.NONE;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public void increaseNumber() {
		number++;
	}
	
	public void decreaseNumber() {
		number--;
	}

	public int getPosition() {
		return position;
	}
	
	@Override
	public String toString() {
		return position + " - " + color.name() + " - " + number + " checkers";
	}

}
