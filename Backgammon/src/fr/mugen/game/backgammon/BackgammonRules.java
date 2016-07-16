package fr.mugen.game.backgammon;

import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Move;
import fr.mugen.game.framework.Rules;

public class BackgammonRules implements Rules {

	public boolean check(Board board, Move move) {
		BackgammonBoard backgammonBoard = (BackgammonBoard) board;
		Dice dice = backgammonBoard.getDice();
		BackgammonMove backgammonMove = (BackgammonMove) move;
		BackgammonColumn from = backgammonMove.getFrom();
		BackgammonColumn to = backgammonMove.getTo();
		
		if (from.getNumber() <= 0)
			return false;
		if (from.isWhite() != to.isWhite())
			return false;
		
		int moveLength = Math.abs(from.getPosition() - to.getPosition());
		if ((dice.getDice1() != moveLength
			&& dice.getDice2() != moveLength
			&& moveLength % dice.getDice1() != 0)
			|| moveLength > dice.getRange())
			return false;
		
		return true;
	}

}
