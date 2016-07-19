package fr.mugen.game.backgammon;

public class BackgammonColumn {

  public enum Color {
    WHITE,
    BLACK,
    NONE
  }

  private Color     color;
  private int       number;
  private final int position;

  public BackgammonColumn(final int position) {
    this(position, Color.NONE);
  }
  
  public BackgammonColumn(final int position, Color color) {
    this.position = position;
    this.color = color;
  }

  public Color getColor() {
    return this.color;
  }

  public void setColor(final Color color) {
    this.color = color;
  }

  public int getNumber() {
    return this.number;
  }

  public void setNumber(final int number) {
    this.number = number;
  }

  public void increaseNumber() {
    this.number++;
  }

  public void decreaseNumber() {
    this.number--;
  }

  public int getPosition() {
    return this.position;
  }

  @Override
  public String toString() {
    return this.position + " - " + this.color.name() + " - " + this.number + " checkers";
  }

}
