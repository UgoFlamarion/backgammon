package fr.mugen.game.backgammon.player;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.framework.Controls;

public abstract class ComputerPlayer extends BackgammonPlayer {

  public ComputerPlayer(final Controls controls, final Color color) {
    super(controls, color);
  }

}
