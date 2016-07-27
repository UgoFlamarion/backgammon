package fr.mugen.game.backgammon.player.ai;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.player.BackgammonPlayer;

public abstract class ComputerPlayer extends BackgammonPlayer {

  public ComputerPlayer(final Color color) {
    super(null, color);
  }

}
