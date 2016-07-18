package fr.mugen.game.backgammon.player;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Player;

public abstract class BackgammonPlayer extends Player {

  protected final Color color;
  protected int         deads;

  public BackgammonPlayer(final Controls controls, final Color color) {
    super(controls);
    this.color = color;
    this.deads = 0;
  }

  public Color getColor() {
    return this.color;
  }

  public int getDeads() {
    return this.deads;
  }

  public void increaseDeads() {
    this.deads++;
  }

  public void decreaseDeads() {
    this.deads--;
  }

}
