package fr.mugen.game.backgammon.display;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.BackgammonColumn;
import fr.mugen.game.backgammon.Dice;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Display;

public class ConsoleDisplay implements Display {

	public void init() {

	}

	public void show(Board board) {
		BackgammonColumn[][] positions = ((BackgammonBoard) board).getPositions();
		
		System.out.println("|01|02|03|04|05|06|07|08|09|10|12|13|");
		System.out.println("|__|__|__|__|__|__|__|__|__|__|__|__|");
		for (int x = 0; x < positions.length; x++)
			for (int y = 0; y < positions[x].length; y++) {
				System.out.print("|");				
				if (x == 2 && y == 0)
					System.out.print("\n|__|__|__|__|__|__|__|__|__|__|__|__|\n|");
				
				BackgammonColumn column = positions[x][y];
				if (column.getNumber() > 0)
					System.out.print((column.isWhite() ? "W" : "B") + column.getNumber());
				else
					System.out.print("NN");
			}
		System.out.println("|\n|__|__|__|__|__|__|__|__|__|__|__|__|");
		System.out.println("|14|15|16|17|18|19|20|21|22|23|24|25|");
		
		Dice dice = ((BackgammonBoard) board).getDice();
		System.out.println("\nDice : " + dice.getDice1() + "-" + dice.getDice2());
	}
	
//	public void showDice(int[] dice) {
//		System.out.println("\nDice : " + dice[0] + "-" + dice[1]);
//	}

}
