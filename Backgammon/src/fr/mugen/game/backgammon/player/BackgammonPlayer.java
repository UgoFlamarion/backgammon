package fr.mugen.game.backgammon.player;

import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Player;

public abstract class BackgammonPlayer extends Player {

  protected final boolean white;

  public BackgammonPlayer(final Controls controls, final boolean white) {
    super(controls);
    this.white = white;
  }

  public boolean isWhite() {
    return this.white;
  }

}
