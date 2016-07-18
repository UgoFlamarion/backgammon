package fr.mugen.game.backgammon;

import fr.mugen.game.framework.Move;

public class BackgammonMove implements Move {

  private final BackgammonColumn from;
  private final BackgammonColumn to;
  private boolean                eating;

  public BackgammonMove(final BackgammonColumn from, final BackgammonColumn to) {
    this.from = from;
    this.to = to;
  }

  public BackgammonMove(final BackgammonColumn from, final BackgammonColumn to, final boolean eating) {
    this(from, to);
    this.eating = eating;
  }

  public BackgammonColumn getFrom() {
    return this.from;
  }

  public BackgammonColumn getTo() {
    return this.to;
  }

  public boolean isEating() {
    return this.eating;
  }

  public void setEating(final boolean eating) {
    this.eating = eating;
  }

}
