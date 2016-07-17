package fr.mugen.game.backgammon.display;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DiceSprite {

  private final Image        diceImage;

  /*
   * Constants
   */

  public final static double SIZEX = 37.5;
  public final static double SIZEY = 37.5;

  /*
   * Faces
   */

  enum Face {
    ONE(1, 0, 4 * DiceSprite.SIZEY),
    TWO(2, 4 * DiceSprite.SIZEX, 4 * DiceSprite.SIZEY),
    THREE(3, 0, 8 * DiceSprite.SIZEY),
    FOUR(4, 0, 0),
    FIVE(5, 12 * DiceSprite.SIZEX, 4 * DiceSprite.SIZEY),
    SIX(6, 8 * DiceSprite.SIZEX, 4 * DiceSprite.SIZEY);

    int    number;
    double posX;
    double posY;

    Face(final int number, final double posX, final double posY) {
      this.number = number;
      this.posX = posX;
      this.posY = posY;
    }

    public static Face getFace(final int number) {
      for (final Face face : Face.values())
        if (face.number == number)
          return face;
      return null;
    }
  }

  public DiceSprite() {
    this.diceImage = new Image("img/dice_sprite.png");
  }

  public ImageView getImageView(final int number) {
    final Face face = Face.getFace(number);
    if (face == null)
      return null;

    final ImageView imageView = new ImageView(this.diceImage);
    imageView.setViewport(new Rectangle2D(face.posX, face.posY, DiceSprite.SIZEX, DiceSprite.SIZEY));
    return imageView;
  }

}
