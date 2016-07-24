package fr.mugen.game.backgammon.player;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Player;

public class ComputerPlayerFactory {

  public static Player createEasyComputerPlayer(final Controls controls, final Color color) {
    return new EasyComputerPlayer(controls, color);
  }

}
