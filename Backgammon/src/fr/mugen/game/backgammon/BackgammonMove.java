package fr.mugen.game.backgammon;

import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Move;

public class BackgammonMove implements Move {

	private BackgammonColumn from;
	private BackgammonColumn to;
	private Dice dice;

	public BackgammonMove(Board board, String input, Dice dice) {
		BackgammonColumn[][] columns = ((BackgammonBoard) board).getPositions();
		String[] move = input.split("-");

		int _from = Integer.valueOf(move[0]) - 1;
		int _to = Integer.valueOf(move[1]) - 1;
		from = columns[Math.min(_from / 6, 4)][Math.min(_from % 6, 6)];
		to = columns[Math.min(_to / 6, 4)][Math.min(_to % 6, 6)];
		
		System.out.println("NEW MOVE FROM " + from.getPosition() + " TO " + to.getPosition());
		
		this.dice = dice;
	}

	public void go() {
		from.decreaseNumber();
		to.increaseNumber();
		dice.consume(Math.abs(from.getPosition() - to.getPosition()));
	}

	public BackgammonColumn getFrom() {
		return from;
	}

	public BackgammonColumn getTo() {
		return to;
	}

}
