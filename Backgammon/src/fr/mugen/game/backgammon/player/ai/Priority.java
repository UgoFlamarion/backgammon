package fr.mugen.game.backgammon.player.ai;

import fr.mugen.game.backgammon.BackgammonMove;

public class Priority {

	private static final int LEVEL_RANGE = 10;

	public static int priorityLevelToWeight(int level) { return level * Priority.LEVEL_RANGE; }
	public static int weightToLevel(int weight) { return weight / Priority.LEVEL_RANGE; }
	
	private int weight;
	private final BackgammonMove move;
	
	public Priority(final BackgammonMove move, final int weight) {
		super();
		this.move = move;
		this.weight = weight;
	}

	public void increaseWeight() {
		weight++;
	}
	
	public void decreaseWeight() {
		weight--;
	}

	public int getWeight() {
		return weight;
	}

	public BackgammonMove getMove() {
		return move;
	}
	
}
