package fr.mugen.game.backgammon;

public class Dice {

  private int dice1;
  private int dice2;
  private int range;

  public void roll() {
    this.dice1 = (int) (Math.random() * 10 % 6 + 1);
    this.dice2 = (int) (Math.random() * 10 % 6 + 1);
    this.range = this.dice1 + this.dice2 * (this.dice1 == this.dice2 ? 2 : 1);
  }

  public boolean keepPlaying() {
    return this.range > 0;
  }

  public void consume(final int moveLength) {
    // System.out.println("RANGE : " + range);
    // System.out.println("CONSUME : " + moveLength);
    if (this.dice1 != this.dice2) {
      this.dice1 = this.dice1 == moveLength ? 0 : this.dice1;
      this.dice2 = this.dice2 == moveLength ? 0 : this.dice2;
    }

    this.range -= moveLength;
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

}
