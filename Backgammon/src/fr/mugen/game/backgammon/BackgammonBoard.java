package fr.mugen.game.backgammon;

import fr.mugen.game.framework.Board;

public class BackgammonBoard implements Board {

	private BackgammonColumn[][] positions = new BackgammonColumn[4][6];
	private Dice dice;

	public BackgammonBoard() {
		for (int x = 0; x < positions.length; x++)
			for (int y = 0; y < positions[x].length; y++)
				positions[x][y] = new BackgammonColumn((x + 1) * ((y + 1) / 6) + (y + 1) % 6);

		// White pawns initialization
		// positions[0][0] = new BackgammonColumn(true, 2, 1);
		// positions[0][5] = new BackgammonColumn(true, 5, 6);
		// positions[1][1] = new BackgammonColumn(true, 3, 8);
		// positions[1][5] = new BackgammonColumn(true, 5, 12);

		positions[0][0].setNumber(2);
		positions[0][5].setNumber(5);
		positions[1][1].setNumber(3);
		positions[1][5].setNumber(5);

		// Black pawns initialization
		// positions[2][0] = new BackgammonColumn(false, 2, 14);
		// positions[2][5] = new BackgammonColumn(false, 5, 19);
		// positions[3][1] = new BackgammonColumn(false, 3, 21);
		// positions[3][5] = new BackgammonColumn(false, 5, 25);
		
		positions[2][0].setWhite(false);
		positions[2][0].setNumber(2);
		positions[2][5].setWhite(false);
		positions[2][5].setNumber(5);
		positions[3][1].setWhite(false);
		positions[3][1].setNumber(3);
		positions[3][5].setWhite(false);
		positions[3][5].setNumber(5);

		dice = new Dice();
	}

	public BackgammonColumn[][] getPositions() {
		return positions;
	}

	public void rollDice() {
		dice.roll();
	}

	public Dice getDice() {
		return dice;
	}

}
