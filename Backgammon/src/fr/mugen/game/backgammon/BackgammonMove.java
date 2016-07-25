package fr.mugen.game.backgammon;

import fr.mugen.game.framework.Move;

public class BackgammonMove implements Move, Cloneable {

  private final BackgammonColumn from;
  private final BackgammonColumn to;
  private boolean                eating;
  private final int              moveLength;

  public BackgammonMove(final BackgammonColumn from, final BackgammonColumn to) {
    this.from = from;
    this.to = to;

    final int toPosition = to.getPosition();
    this.moveLength = Math
        .abs(from.getPosition() - (toPosition + (BackgammonBoard.IS_HEAVEN(toPosition) ? BackgammonRules.getSideFactor(toPosition) : 0)));
    // System.out.println("(" + from.getPosition() + ", " + toPosition + ") Move
    // Length = " + this.moveLength);
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

  public int getMoveLength() {
    return this.moveLength;
  }

  @Override
  public Object clone() {
	  return new BackgammonMove((BackgammonColumn) from.clone(), (BackgammonColumn) to.clone());
  }
  
}
