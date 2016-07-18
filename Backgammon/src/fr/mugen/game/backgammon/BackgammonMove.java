package fr.mugen.game.backgammon;

import fr.mugen.game.framework.Move;

public class BackgammonMove implements Move {

  private final BackgammonColumn from;
  private final BackgammonColumn to;

  public BackgammonMove(final BackgammonColumn from, final BackgammonColumn to) {
    this.from = from;
    this.to = to;
  }

  public BackgammonColumn getFrom() {
    return this.from;
  }

  public BackgammonColumn getTo() {
    return this.to;
  }

}
