package fr.mugen.game.backgammon.display;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.BackgammonColumn;
import fr.mugen.game.backgammon.BackgammonGame;
import fr.mugen.game.backgammon.BackgammonMove;
import fr.mugen.game.backgammon.Dice;
import fr.mugen.game.backgammon.BackgammonColumn.Color;
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
	// Clear root Pane
	root.getChildren().clear();
	  
    this.game = (BackgammonGame) game;

    showCheckers(this.game);
  }

  private ImageView getCheckerImageView(final Color color) {
    final ImageView checker = new ImageView();
    checker.setImage(Color.WHITE.equals(color) ? this.whiteCheckerImage : this.blackCheckerImage);

    return checker;
  }

  private void showCheckers(final BackgammonGame game) {
    for (final BackgammonColumn column : ((BackgammonBoard) game.getBoard()).getColumns()) {
      final int position = column.getPosition();
      for (int i = 0; i < column.getNumber(); i++) {
        final ImageView checker = getCheckerImageView(column.getColor());

        checker.setX(JavaFXUtils.getCheckerX(position));
        checker.setY(JavaFXUtils.getCheckerY(position, i));

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
    this.cursorPosition = this.game.getPlayersDefaultPosition(((BackgammonPlayer) this.game.getCurrentPlayer()).getColor());
    this.cursorImageView.setX(JavaFXUtils.getCursorX(this.cursorPosition));
    this.cursorImageView.setY(JavaFXUtils.getCursorY(this.cursorPosition));
    this.cursorImageView.setScaleY(JavaFXUtils.getScaleY(this.cursorPosition));
    this.root.getChildren().add(this.cursorImageView);
  }

  public Pane getRoot() {
    return this.root;
  }

  public void left() {
      this.cursorPosition = this.game.getNextPossiblePositionOnLeft(this.cursorPosition,
          ((BackgammonPlayer) this.game.getCurrentPlayer()).getColor(), cursorSelectedPosition);
      this.cursorImageView.setX(JavaFXUtils.getCursorX(this.cursorPosition));
      this.cursorImageView.setY(JavaFXUtils.getCursorY(this.cursorPosition));
    System.out.println("POSITION = " + cursorPosition);
  }

  public void right() {
	  this.cursorPosition = this.game.getNextPossiblePositionOnRight(this.cursorPosition,
	       ((BackgammonPlayer) this.game.getCurrentPlayer()).getColor(), cursorSelectedPosition);
      this.cursorImageView.setX(JavaFXUtils.getCursorX(this.cursorPosition));
      this.cursorImageView.setY(JavaFXUtils.getCursorY(this.cursorPosition));
    System.out.println("POSITION = " + cursorPosition);
  }

  public void up() {
	  this.cursorPosition = this.game.getNextPossiblePositionUpward(this.cursorPosition,
    	   ((BackgammonPlayer) this.game.getCurrentPlayer()).getColor(), cursorSelectedPosition);
	  this.cursorImageView.setX(JavaFXUtils.getCursorX(this.cursorPosition));
      this.cursorImageView.setY(JavaFXUtils.getCursorY(this.cursorPosition));
      this.cursorImageView.setScaleY(JavaFXUtils.getScaleY(this.cursorPosition));
    System.out.println("POSITION = " + cursorPosition);
  }

  public void down() {
	  this.cursorPosition = this.game.getNextPossiblePositionDownward(this.cursorPosition,
    	   ((BackgammonPlayer) this.game.getCurrentPlayer()).getColor(), cursorSelectedPosition);
	  this.cursorImageView.setX(JavaFXUtils.getCursorX(this.cursorPosition));
      this.cursorImageView.setY(JavaFXUtils.getCursorY(this.cursorPosition));
      this.cursorImageView.setScaleY(JavaFXUtils.getScaleY(this.cursorPosition));
    System.out.println("POSITION = " + cursorPosition);
  }

  public void select() {
    if (this.cursorPosition == this.cursorSelectedPosition) {
      this.root.getChildren().remove(this.cursorSelectedImageView);
      this.cursorSelectedPosition = 0;
      return;
    }
    else if (cursorSelectedPosition != 0) {     // Move checker
      game.move(cursorSelectedPosition, cursorPosition);
      this.cursorSelectedPosition = 0;
    }
    else {
	    this.cursorSelectedImageView.setX(this.cursorImageView.getX());
	    this.cursorSelectedImageView.setY(this.cursorImageView.getY());
	    this.cursorSelectedImageView.setScaleY(this.cursorImageView.getScaleY());
	
	    if (!this.root.getChildren().contains(this.cursorSelectedImageView))
	      this.root.getChildren().add(this.cursorSelectedImageView);
	
	    this.cursorSelectedPosition = this.cursorPosition;
    }
  }

}
