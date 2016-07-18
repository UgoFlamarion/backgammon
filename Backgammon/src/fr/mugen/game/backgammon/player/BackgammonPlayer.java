package fr.mugen.game.backgammon.player;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Player;

public abstract class BackgammonPlayer extends Player {

  protected final Color color;

  public BackgammonPlayer(final Controls controls, final Color color) {
    super(controls);
    this.color = color;
  }

  public Color getColor() {
    return this.color;
  }

}
