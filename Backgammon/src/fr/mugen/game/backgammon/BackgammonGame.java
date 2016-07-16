package fr.mugen.game.backgammon;

import java.util.List;

import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Game;
import fr.mugen.game.framework.Player;
import fr.mugen.game.framework.Rules;

public class BackgammonGame extends Game {

	public BackgammonGame(Board board, List<Player> players, Rules rules, Display display) {
		super(board, players, rules, display);
	}

	protected void loop() {
		for (Player player : players) {
			player.play(board, rules);
			((BackgammonBoard) board).rollDice();
			display.show(board);
		}
		turn++;
	}

}
