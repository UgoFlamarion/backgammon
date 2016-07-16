package fr.mugen.game.backgammon;

import java.util.Arrays;
import java.util.List;

import fr.mugen.game.backgammon.display.ConsoleDisplay;
import fr.mugen.game.backgammon.player.HumanPlayer;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Player;

public class BackgammonStarter {

	public static void main(String[] args) {
		Controls controls = new KeyboardControls();
		List<Player> players = Arrays.asList((Player) new HumanPlayer(controls), (Player) new HumanPlayer(controls));
		BackgammonGame game = new BackgammonGame(new BackgammonBoard(), 
												players, 
												new BackgammonRules(), 
												new ConsoleDisplay());
		game.start();
	}
}
