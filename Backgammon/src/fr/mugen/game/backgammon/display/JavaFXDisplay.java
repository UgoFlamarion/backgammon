package fr.mugen.game.backgammon.display;

import java.net.URISyntaxException;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.BackgammonColumn;
import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.BackgammonGame;
import fr.mugen.game.backgammon.Dice;
import fr.mugen.game.backgammon.player.BackgammonPlayer;
import fr.mugen.game.backgammon.utils.JavaFXUtils;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Game;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
  
  public final static int  CEMETERY_GAPY = 50;

  // public final static int DEAD_CHECKERX = 32

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

  /*
   * Sounds
   */

  private final Media      diceRolling;

  public JavaFXDisplay(final Pane root) throws URISyntaxException {
    this.root = root;

    JavaFXDisplay.WIDTH = root.getWidth();
    JavaFXDisplay.HEIGHT = root.getHeight();

    this.blackCheckerImage = new Image("img/black_checker2.png");
    this.whiteCheckerImage = new Image("img/white_checker2.png");
    this.cursorImageView = new ImageView(new Image("img/cursor.png"));
    this.cursorSelectedImageView = new ImageView(new Image("img/cursor_blue.png"));

    this.diceSprite = new DiceSprite();

    this.diceRolling = new Media(getClass().getClassLoader().getResource("sound/roll.wav").toURI().toString());
  }

  @Override
  public void update(final Game game) {
    // Clear root Pane
    this.root.getChildren().clear();

    this.game = (BackgammonGame) game;

    showCheckers();
    showCemeteries();
  }

  private ImageView getCheckerImageView(final Color color) {
    final ImageView checker = new ImageView();
    checker.setImage(Color.WHITE.equals(color) ? this.whiteCheckerImage : this.blackCheckerImage);

    return checker;
  }

  private void showCheckers() {
    for (final BackgammonColumn column : ((BackgammonBoard) this.game.getBoard()).getColumns()) {
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
    updateCursorPosition();
    this.root.getChildren().add(this.cursorImageView);
  }

  public void showCemeteries() {
    this.game.getPlayers().stream().forEach(c -> {
      final BackgammonPlayer player = (BackgammonPlayer) c;
      if (player.getDeads() > 0) {
        final ImageView checker = getCheckerImageView(player.getColor());

        int sideFactor = player.getColor() == Color.WHITE ? -1 : 1;
        double x = (JavaFXDisplay.WIDTH / 2) - (CHECKER_IMAGE_SIZE / 2);
		double y = (HEIGHT / 2) - CHECKER_IMAGE_SIZE + CEMETERY_GAPY * sideFactor;
        
		checker.setX(x);
		checker.setY(y);

        Text text = new Text();
		text.getStyleClass().add("deadCount");
		System.out.println(player.getColor().name());
		text.getStyleClass().add(player.getColor().name());
        text.setText("X" + player.getDeads());
        text.setX(x + (CHECKER_IMAGE_SIZE / 2) - (text.getLayoutBounds().getWidth() / 2));
        text.setY(y + (CHECKER_IMAGE_SIZE / 2) + (text.getLayoutBounds().getHeight() / 2));
        
        this.root.getChildren().add(checker);
        this.root.getChildren().add(text);
      }

    });
  }

  public void playRollingDiceSound() {
    final MediaPlayer mediaPlayer = new MediaPlayer(this.diceRolling);
    mediaPlayer.play();
  }

  public Pane getRoot() {
    return this.root;
  }

  public void left() {
    this.cursorPosition = this.game.getNextPossiblePositionOnLeft(this.cursorPosition,
        ((BackgammonPlayer) this.game.getCurrentPlayer()).getColor(), this.cursorSelectedPosition);
    updateCursorPosition();
    System.out.println("POSITION = " + this.cursorPosition);
  }

  public void right() {
    this.cursorPosition = this.game.getNextPossiblePositionOnRight(this.cursorPosition,
        ((BackgammonPlayer) this.game.getCurrentPlayer()).getColor(), this.cursorSelectedPosition);
    updateCursorPosition();
    System.out.println("POSITION = " + this.cursorPosition);
  }

  public void up() {
    this.cursorPosition = this.game.getNextPossiblePositionUpward(this.cursorPosition,
        ((BackgammonPlayer) this.game.getCurrentPlayer()).getColor(), this.cursorSelectedPosition);
    updateCursorPosition();
    System.out.println("POSITION = " + this.cursorPosition);
  }

  public void down() {
    this.cursorPosition = this.game.getNextPossiblePositionDownward(this.cursorPosition,
        ((BackgammonPlayer) this.game.getCurrentPlayer()).getColor(), this.cursorSelectedPosition);
    updateCursorPosition();
    System.out.println("POSITION = " + this.cursorPosition);
  }

  private void updateCursorPosition() {
    this.cursorImageView.setX(JavaFXUtils.getCursorX(this.cursorPosition));
    this.cursorImageView.setY(JavaFXUtils.getCursorY(this.cursorPosition));
    this.cursorImageView.setScaleY(JavaFXUtils.getScaleY(this.cursorPosition));
  }

  public void select() {
    if (this.cursorPosition == this.cursorSelectedPosition) {
      this.root.getChildren().remove(this.cursorSelectedImageView);
      this.cursorSelectedPosition = 0;
      return;
    } else if (this.cursorSelectedPosition != 0) { // Move checker
      this.game.move(this.cursorSelectedPosition, this.cursorPosition);
      this.cursorSelectedPosition = 0;
    } else {
      this.cursorSelectedImageView.setX(this.cursorImageView.getX());
      this.cursorSelectedImageView.setY(this.cursorImageView.getY());
      this.cursorSelectedImageView.setScaleY(this.cursorImageView.getScaleY());

      if (!this.root.getChildren().contains(this.cursorSelectedImageView))
        this.root.getChildren().add(this.cursorSelectedImageView);

      this.cursorSelectedPosition = this.cursorPosition;
    }
  }

}
