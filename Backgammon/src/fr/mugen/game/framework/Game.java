package fr.mugen.game.framework;

import java.util.List;

public abstract class Game {
	protected Board board;
	protected List<Player> players;
	protected Rules rules;
	protected Display display;
	protected boolean gameIsEnded;
	protected int turn;

	public Game(Board board, List<Player> players, Rules rules, Display display) {
		this.board = board;
		this.players = players;
		this.rules = rules;
		this.display = display;
	}

	public void start() {
		display.show(board);
		_loop();
	}

	private void _loop() {
		while (!gameIsEnded) {
			loop();
		}
	}
	
	protected abstract void loop();
	
}
