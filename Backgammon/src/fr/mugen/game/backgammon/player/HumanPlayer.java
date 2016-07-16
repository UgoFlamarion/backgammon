package fr.mugen.game.backgammon.player;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.BackgammonMove;
import fr.mugen.game.backgammon.Dice;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Player;
import fr.mugen.game.framework.Rules;

public class HumanPlayer extends Player {

	public HumanPlayer(Controls controls) {
		super(controls);
	}

	public void play(Board board, Rules rules) {
		Dice dice = ((BackgammonBoard) board).getDice();
		while (dice.keepPlaying()) {
			BackgammonMove move = null;
			boolean rulesRespected = false;
			
			while (move == null || !rulesRespected) {
				move = new BackgammonMove(board, (String) controls.getInput(), dice);
				if (!(rulesRespected = rules.check(board, move)))
					System.out.println("Bad move.");
			}
			move.go();
		}
	}

}
