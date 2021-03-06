package fr.mugen.game.backgammon;

public class Dice {

  private int     dice1;
  private int     dice2;
  private int     range;
  private boolean doubleDice;

  public void roll() {
    this.dice1 = (int) (((Math.random() * 10) % 6) + 1);
    this.dice2 = (int) (((Math.random() * 10) % 6) + 1);
    // this.dice1 = 1;
    // this.dice2 = 6;

    this.doubleDice = this.dice1 == this.dice2;
    this.range = (this.dice1 + this.dice2) * (this.doubleDice ? 2 : 1);
    System.out.println("Rolling dice : " + this.dice1 + ":" + this.dice2);
  }

  public boolean keepPlaying() {
    return this.range > 0;
  }

  public void consume(final int moveLength) {
    consume(moveLength, false);
  }

  public void consume(int moveLength, final boolean goToHeaven) {
    if (goToHeaven && (moveLength != this.dice1) && (moveLength != this.dice2))
      moveLength = this.dice1 > this.dice2 ? this.dice1 : this.dice2;
    System.out.println("Consume dice : " + moveLength);

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

  /**
   * For cloning purpose.
   */
  protected void setDice(final int dice1, final int dice2) {
    this.dice1 = dice1;
    this.dice2 = dice2;
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
