package fr.mugen.game.backgammon.display;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.BackgammonColumn;
import fr.mugen.game.backgammon.BackgammonGame;
import fr.mugen.game.backgammon.Dice;
import fr.mugen.game.backgammon.player.BackgammonPlayer;
import fr.mugen.game.backgammon.utils.JavaFXUtils;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Game;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class JavaFXDisplay implements Display {

  public static double     WIDTH;
  public static double     HEIGHT;

  private final Pane       root;

  /*
   * Game
   */

  private BackgammonGame   game;

  /*
   * Checkers
   */

  public final static int  BORDERX            = 32;
  public final static int  BORDERY            = 30;

  public final static int  MIDDLE_GAPX        = 59;
  public final static int  CHECKER_GAPY       = 20;

  public final static int  GAPX               = 50;
  public final static int  GAPY               = 530;

  public final static int  CHECKER_IMAGE_SIZE = 50;

  private final Image      blackCheckerImage;
  private final Image      whiteCheckerImage;

  /*
   * Dice
   */

  private final DiceSprite diceSprite;

  /*
   * Cursor
   */

  public final static int  CURSORY_LINE1      = 260;
  public final static int  CURSORY_LINE2      = 330;

  private final ImageView  cursorImageView;
  private final ImageView  cursorSelectedImageView;
  private int              cursorPosition;
  private int              cursorSelectedPosition;

  public JavaFXDisplay(final Pane root) {
    this.root = root;

    JavaFXDisplay.WIDTH = root.getWidth();
    JavaFXDisplay.HEIGHT = root.getHeight();

    this.blackCheckerImage = new Image("img/black_checker2.png");
    this.whiteCheckerImage = new Image("img/white_checker2.png");
    this.cursorImageView = new ImageView(new Image("img/cursor.png"));
    this.cursorSelectedImageView = new ImageView(new Image("img/cursor_blue.png"));

    this.diceSprite = new DiceSprite();
  }

  @Override
  public void update(final Game game) {
    if (this.game != game)
      this.game = (BackgammonGame) game;

    showCheckers(this.game);
  }

  private ImageView getCheckerImageView(final boolean white) {
    final ImageView checker = new ImageView();
    checker.setImage(white ? this.whiteCheckerImage : this.blackCheckerImage);

    return checker;
  }

  private void showCheckers(final BackgammonGame game) {
    for (final BackgammonColumn column : ((BackgammonBoard) game.getBoard()).getColumns()) {
      final int position = column.getPosition();
      for (int i = 0; i < column.getNumber(); i++) {
        final ImageView checker = getCheckerImageView(column.isWhite());

        final int x = JavaFXUtils.getCheckerX(position);
        final int y = JavaFXUtils.getCheckerY(position, i);

        checker.setX(position <= 12 ? x : this.root.getWidth() - x - JavaFXDisplay.CHECKER_IMAGE_SIZE);
        checker.setY(y);

        // System.out
        // .println((column.isWhite() ? "WHITE" : "BLACK") + " (" + position +
        // ") X = " + checker.getX() + " : Y = " + checker.getY());
        this.root.getChildren().add(checker);
      }
    }
  }

  public void showDice(final Dice dice) {
    final ImageView dice1ImageView = this.diceSprite.getImageView(dice.getDice1());
    dice1ImageView.setX(this.root.getWidth() / 2 - DiceSprite.SIZEX);
    dice1ImageView.setY(this.root.getHeight() / 2 - DiceSprite.SIZEY);
    this.root.getChildren().add(dice1ImageView);

    final ImageView dice2ImageView = this.diceSprite.getImageView(dice.getDice2());
    dice2ImageView.setX(this.root.getWidth() / 2);
    dice2ImageView.setY(this.root.getHeight() / 2 - DiceSprite.SIZEY);
    this.root.getChildren().add(dice2ImageView);
  }

  public void showCursor() {
    this.cursorPosition = this.game.getPlayersDefaultPosition(((BackgammonPlayer) this.game.getCurrentPlayer()).isWhite());
    this.cursorImageView.setX(JavaFXUtils.getCursorX(this.cursorPosition));
    this.cursorImageView.setY(JavaFXUtils.getCursorY(this.cursorPosition));
    this.root.getChildren().add(this.cursorImageView);
  }

  public Pane getRoot() {
    return this.root;
  }

  public void left() {
    if (this.cursorPosition != 1 && this.cursorPosition != 24) {
      // this.cursorPosition -= this.cursorPosition <= 12 ? 1 : -1;
      this.cursorPosition = this.game.getNextPossiblePositionOnLeft(this.cursorPosition,
          ((BackgammonPlayer) this.game.getCurrentPlayer()).isWhite());
      this.cursorImageView.setX(JavaFXUtils.getCursorX(this.cursorPosition));
    }
  }

  public void right() {
    if (this.cursorPosition != 12 && this.cursorPosition != 13) {
      this.cursorPosition += this.cursorPosition <= 12 ? 1 : -1;
      this.cursorImageView.setX(JavaFXUtils.getCursorX(this.cursorPosition));
    }
  }

  public void up() {
    if (this.cursorPosition > 12) {
      this.cursorPosition -= (this.cursorPosition - 12) * 2 - 1;
      this.cursorImageView.setY(JavaFXUtils.getCursorY(this.cursorPosition));
      this.cursorImageView.setScaleY(1);
    }
  }

  public void down() {
    if (this.cursorPosition <= 12) {
      this.cursorPosition += (12 - this.cursorPosition) * 2 + 1;
      this.cursorImageView.setY(JavaFXUtils.getCursorY(this.cursorPosition));
      this.cursorImageView.setScaleY(-1);
    }
  }

  public void select() {
    if (this.cursorPosition == this.cursorSelectedPosition) {
      this.root.getChildren().remove(this.cursorSelectedImageView);
      this.cursorSelectedPosition = 0;
      return;
    }

    this.cursorSelectedImageView.setX(this.cursorImageView.getX());
    this.cursorSelectedImageView.setY(this.cursorImageView.getY());
    this.cursorSelectedImageView.setScaleY(this.cursorImageView.getScaleY());

    if (!this.root.getChildren().contains(this.cursorSelectedImageView))
      this.root.getChildren().add(this.cursorSelectedImageView);

    this.cursorSelectedPosition = this.cursorPosition;
  }

}
