package fr.mugen.game.backgammon;

public class Dice {

	public int dice1;
	public int dice2;
	public int range;

	public Dice() {
		roll();
	}
	
	public void roll() {
		dice1 = (int) ((Math.random() * 10) % 6 + 1);
		dice2 = (int) ((Math.random() * 10) % 6 + 1);
		range = dice1 + dice2 * (dice1 == dice2 ? 2 : 1);
	}

	// public boolean isDouble() {
	// return dice1 == dice2;
	// }

	public boolean keepPlaying() {
		return range > 0;
	}

	public void consume(int moveLength) {
		System.out.println("RANGE : " + range);
		System.out.println("CONSUME : " + moveLength);
		range -= moveLength;
	}

	public int getRange() {
		return range;
	}

	public int getDice1() {
		return dice1;
	}

	public int getDice2() {
		return dice2;
	}

}
