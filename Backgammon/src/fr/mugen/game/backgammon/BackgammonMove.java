package fr.mugen.game.backgammon;

import fr.mugen.game.framework.Move;

public class BackgammonMove implements Move {

  private final BackgammonColumn from;
  private final BackgammonColumn to;
  private final Dice             dice;

  public BackgammonMove(final BackgammonColumn from, final BackgammonColumn to, final Dice dice) {
    // final String[] move = input.split("-");
    //
    // this.from = ((BackgammonBoard)
    // board).getColumn(Integer.valueOf(move[0]));
    // this.to = ((BackgammonBoard) board).getColumn(Integer.valueOf(move[1]));
    //
    // System.out.println("NEW MOVE FROM " + this.from.getPosition() + " TO " +
    // this.to.getPosition());
    this.from = from;
    this.to = to;
    this.dice = dice;
  }

  @Override
  public void go() {
    this.from.decreaseNumber();
    this.to.increaseNumber();
    this.dice.consume(Math.abs(this.from.getPosition() - this.to.getPosition()));
  }

  public BackgammonColumn getFrom() {
    return this.from;
  }

  public BackgammonColumn getTo() {
    return this.to;
  }

}
