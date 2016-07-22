package fr.mugen.game.backgammon;

public class Dice {

  private int     dice1;
  private int     dice2;
  private int     range;
  private boolean doubleDice;

  public void roll() {
//    this.dice1 = (int) (Math.random() * 10 % 6 + 1);
//    this.dice2 = (int) (Math.random() * 10 % 6 + 1);
	  
	  this.dice1 = 3;
	  this.dice2 = 6;
	  
    this.doubleDice = this.dice1 == this.dice2;
    this.range = (this.dice1 + this.dice2) * (this.doubleDice ? 2 : 1);
  }

  public boolean keepPlaying() {
    return this.range > 0;
  }

  public void consume(final int moveLength) {
    if (this.dice1 != this.dice2) {
      this.dice1 = this.dice1 == moveLength ? 0 : this.dice1;
      this.dice2 = this.dice2 == moveLength ? 0 : this.dice2;
    }

    this.range -= moveLength;
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
