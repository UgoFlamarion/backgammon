package fr.mugen.game.backgammon.display;

import java.net.URISyntaxException;
import java.util.Collection;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.BackgammonColumn;
import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.BackgammonGame;
import fr.mugen.game.backgammon.Dice;
import fr.mugen.game.backgammon.utils.JavaFXUtils;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Game;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class JavaFXDisplay implements Display {

  public static double        WIDTH;
  public static double        HEIGHT;

  private final Pane          root;

  /*
   * Game
   */

  private BackgammonGame      game;

  /*
   * Checkers
   */

  private static final String WHITE_CHECKER_IMG_PATH    = "img/white_checker.png";
  private static final String BLACK_CHECKER_IMG_PATH    = "img/black_checker.png";

  public final static int     BORDERX                   = 32;
  public final static int     BORDERY                   = 30;

  public final static int     MIDDLE_GAPX               = 59;
  public final static int     CHECKER_GAPY              = 20;

  public final static int     GAPX                      = 50;
  public final static int     GAPY                      = 530;

  public final static int     CHECKER_IMAGE_SIZE        = 50;

  public final static int     CEMETERY_GAPY             = 50;

  // public final static int DEAD_CHECKERX = 32

  private final Image         blackCheckerImage;
  private final Image         whiteCheckerImage;

  /*
   * Dice
   */

  private final DiceSprite    diceSprite;

  /*
   * Cursor
   */

  private static final String CURSOR_IMG_PATH           = "img/cursor.png";
  private static final String SELECTION_CURSOR_IMG_PATH = "img/cursor_blue.png";

  public final static int     CURSOR_IMAGE_SIZE         = 25;

  public final static int     CURSORY_LINE1             = 260;
  public final static int     CURSORY_LINE2             = 358;

  private final ImageView     cursorImageView;
  private final ImageView     cursorSelectedImageView;
  private int                 cursorPosition;
  private int                 cursorSelectedPosition;

  /*
   * Sounds
   */
  private final static String ROLLING_DICE_SND_PATH     = "sound/roll.wav";
  private final Media         diceRolling;

  public JavaFXDisplay(final Pane root) throws URISyntaxException {
    this.root = root;

    JavaFXDisplay.WIDTH = root.getWidth();
    JavaFXDisplay.HEIGHT = root.getHeight();

    this.cursorSelectedPosition = BackgammonGame.DEFAULT_CURSOR_POSITION;

    this.blackCheckerImage = new Image(JavaFXDisplay.BLACK_CHECKER_IMG_PATH);
    this.whiteCheckerImage = new Image(JavaFXDisplay.WHITE_CHECKER_IMG_PATH);
    this.cursorImageView = new ImageView(new Image(JavaFXDisplay.CURSOR_IMG_PATH));
    this.cursorSelectedImageView = new ImageView(new Image(JavaFXDisplay.SELECTION_CURSOR_IMG_PATH));

    this.diceSprite = new DiceSprite();

    this.diceRolling = new Media(getClass().getClassLoader().getResource(JavaFXDisplay.ROLLING_DICE_SND_PATH).toURI().toString());
  }

  @Override
  public void update(final Game game) {
    // Clear root Pane
    this.root.getChildren().clear();

    this.game = (BackgammonGame) game;

    showCheckers(((BackgammonBoard) this.game.getBoard()).getColumns());
    showDice(((BackgammonBoard) this.game.getBoard()).getDice());
    showCursor();
    showSelectionCursor();
  }

  private ImageView getCheckerImageView(final Color color) {
    final ImageView checker = new ImageView();
    checker.setImage(Color.WHITE.equals(color) ? this.whiteCheckerImage : this.blackCheckerImage);

    return checker;
  }

  /*
   * Display
   */

  private void showCheckers(final Collection<BackgammonColumn> columns) {
    for (final BackgammonColumn column : columns) {
      final int position = column.getPosition();

      if (BackgammonBoard.IS_CEMETERY(position) && column.getNumber() > 0)
        showCemetery(column);
      else
        for (int i = 0; i < column.getNumber(); i++) {
          final ImageView checker = getCheckerImageView(column.getColor());

          checker.setX(JavaFXUtils.getCheckerX(position));
          checker.setY(JavaFXUtils.getCheckerY(position, i));

          this.root.getChildren().add(checker);
        }
    }
  }

  private void showCemetery(final BackgammonColumn column) {
    System.out.println("Show cemetery (" + column.getColor() + ")");
    final Color color = column.getColor();
    final ImageView checker = getCheckerImageView(color);

    final double x = JavaFXDisplay.WIDTH / 2 - JavaFXDisplay.CHECKER_IMAGE_SIZE / 2;
    final double y = JavaFXDisplay.HEIGHT / 2 - JavaFXDisplay.CHECKER_IMAGE_SIZE
        + JavaFXDisplay.CEMETERY_GAPY * (color == Color.WHITE ? -1 : 1);

    checker.setX(x);
    checker.setY(y);

    final Text text = new Text();
    text.getStyleClass().addAll("deadCount", color.name());
    text.setText("X" + column.getNumber());
    text.setX(x + JavaFXDisplay.CHECKER_IMAGE_SIZE / 2 - text.getLayoutBounds().getWidth() / 2);
    text.setY(y + JavaFXDisplay.CHECKER_IMAGE_SIZE / 2 + text.getLayoutBounds().getHeight() / 2);

    this.root.getChildren().add(checker);
    this.root.getChildren().add(text);
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
    System.out.println("Get default position for selection cursor at " + this.cursorSelectedPosition);
    showCursor(this.game.getCursorDefaultPosition(this.cursorSelectedPosition));
  }

  public void showCursor(final int cursorPosition) {
    System.out.println("Show cursor at " + cursorPosition);
    if (cursorPosition < 0)
      return;

    this.cursorPosition = cursorPosition;

    updateCursorImageView(this.cursorPosition);
    if (!this.root.getChildren().contains(this.cursorImageView))
      this.root.getChildren().add(this.cursorImageView);
  }

  private void showSelectionCursor() {
    this.cursorSelectedImageView.setX(JavaFXUtils.getCursorX(this.cursorSelectedPosition));
    this.cursorSelectedImageView.setY(JavaFXUtils.getCursorY(this.cursorSelectedPosition));
    this.cursorSelectedImageView.setScaleY(JavaFXUtils.getScaleY(this.cursorSelectedPosition));

    if (!this.root.getChildren().contains(this.cursorSelectedImageView))
      this.root.getChildren().add(this.cursorSelectedImageView);
  }

  public void showMessage(final String message, final EventHandler<ActionEvent> onFinished) {
    System.out.println("Show message '" + message + "'.");
    final Text text = new Text();
    text.getStyleClass().add("message");
    text.setText(message);
    text.setX(JavaFXDisplay.WIDTH / 2 - text.getLayoutBounds().getWidth() / 2);
    text.setY(JavaFXDisplay.HEIGHT / 2 - text.getLayoutBounds().getHeight() / 2);

    final Timeline blinker = createBlinker(text);
    final SequentialTransition blinkThenFade = new SequentialTransition(text, blinker);

    blinkThenFade.setOnFinished(onFinished);
    this.root.getChildren().add(text);
    blinkThenFade.play();
  }

  private Timeline createBlinker(final Node node) {
    final Timeline blink = new Timeline(new KeyFrame(Duration.seconds(0), new KeyValue(node.opacityProperty(), 0, Interpolator.DISCRETE)),
        new KeyFrame(Duration.seconds(0.5), new KeyValue(node.opacityProperty(), 1, Interpolator.DISCRETE)),
        new KeyFrame(Duration.seconds(1), new KeyValue(node.opacityProperty(), 0, Interpolator.DISCRETE)));
    blink.setCycleCount(3);

    return blink;
  }

  /*
   * Sounds
   */

  public void playRollingDiceSound() {
    final MediaPlayer mediaPlayer = new MediaPlayer(this.diceRolling);
    mediaPlayer.play();
  }

  /*
   * Controls
   */

  public void left() {
    this.cursorPosition = this.game.getNextPossiblePositionOnLeft(this.cursorPosition, this.cursorSelectedPosition);
    updateCursorImageView(this.cursorPosition);
    System.out.println("POSITION = " + this.cursorPosition);
  }

  public void right() {
    this.cursorPosition = this.game.getNextPossiblePositionOnRight(this.cursorPosition, this.cursorSelectedPosition);
    updateCursorImageView(this.cursorPosition);
    System.out.println("POSITION = " + this.cursorPosition);
  }

  public void up() {
    this.cursorPosition = this.game.getNextPossiblePositionUpward(this.cursorPosition, this.cursorSelectedPosition);
    updateCursorImageView(this.cursorPosition);
    System.out.println("POSITION = " + this.cursorPosition);
  }

  public void down() {
    this.cursorPosition = this.game.getNextPossiblePositionDownward(this.cursorPosition, this.cursorSelectedPosition);
    updateCursorImageView(this.cursorPosition);
    System.out.println("POSITION = " + this.cursorPosition);
  }

  public void select() {
    select(this.cursorPosition);
  }

  public void select(final int position) {
    // Move checker
    if (this.cursorSelectedPosition != BackgammonGame.DEFAULT_CURSOR_POSITION) {
      this.game.move(this.cursorSelectedPosition, position);
      return;
    }

    // Clear selection
    if (position == this.cursorSelectedPosition) {
      hideSelectionCursor();
      initCursorSelectedPosition();
      return;
    } else { // Select checker
      this.cursorSelectedPosition = position;
      showSelectionCursor();
    }
  }

  public void initCursorSelectedPosition() {
    this.cursorSelectedPosition = BackgammonGame.DEFAULT_CURSOR_POSITION;
  }

  private void updateCursorImageView(final int cursorPosition) {
    this.cursorImageView.setX(JavaFXUtils.getCursorX(cursorPosition));
    this.cursorImageView.setY(JavaFXUtils.getCursorY(cursorPosition));
    this.cursorImageView.setRotate(JavaFXUtils.getRotate(cursorPosition));
    this.cursorImageView.setScaleY(JavaFXUtils.getScaleY(cursorPosition));
  }

  private void hideSelectionCursor() {
    this.root.getChildren().remove(this.cursorSelectedImageView);
  }

  /*
   * Getters
   */

  public Pane getRoot() {
    return this.root;
  }
}
