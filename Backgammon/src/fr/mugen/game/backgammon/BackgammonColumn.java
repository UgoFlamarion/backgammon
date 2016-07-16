package fr.mugen.game.backgammon;

public class BackgammonColumn {

	private boolean white;
	private int number;
	private int position;

	public BackgammonColumn(int position) {
		this.position = position;
		this.white = true;
	}
	
	public boolean isWhite() {
		return white;
	}

	public void setWhite(boolean white) {
		this.white = white;
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

}
