package fr.mugen.game.backgammon;

public class Dice {

  private int     dice1;
  private int     dice2;
  private int     range;
  private boolean doubleDice;

  public void roll() {
    // this.dice1 = (int) (Math.random() * 10 % 6 + 1);
    // this.dice2 = (int) (Math.random() * 10 % 6 + 1);
    this.dice1 = 1;
    this.dice2 = 3;

    this.doubleDice = this.dice1 == this.dice2;
    this.range = (this.dice1 + this.dice2) * (this.doubleDice ? 2 : 1);
  }

  public boolean keepPlaying() {
    return this.range > 0;
  }

  public void consume(final int moveLength) {
    consume(moveLength, false);
  }

  public void consume(int moveLength, final boolean goToHeaven) {
    System.out.println("Consume dice : " + moveLength);
    if (goToHeaven && moveLength != this.dice1 && moveLength != this.dice2)
      moveLength = this.dice1 > this.dice2 ? this.dice1 : this.dice2;

    if (this.dice1 != this.dice2) {
      this.dice1 = this.dice1 == moveLength ? 0 : this.dice1;
      this.dice2 = this.dice2 == moveLength ? 0 : this.dice2;
    }

    this.range -= moveLength;
    System.out.println("Dice 1 : " + this.dice1 + " - Dice 2 : " + this.dice2 + " - Range : " + this.range);
  }

  public void consumeAll() {
    consume(this.range);
  }

  public int getRange() {
    return this.range;
  }

  public int getDice1() {
    return this.dice1;
  }

  public int getDice2() {
    return this.dice2;
  }

  public boolean isDoubleDice() {
    return this.doubleDice;
  }

}
