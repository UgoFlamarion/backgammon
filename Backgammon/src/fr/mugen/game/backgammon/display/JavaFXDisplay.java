package fr.mugen.game.backgammon.display;

import java.net.URISyntaxException;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.BackgammonGame;
import fr.mugen.game.backgammon.BackgammonRules;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Game;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class JavaFXDisplay implements Display {

  /*
   * Constants
   */

  public static double                 WIDTH;
  public static double                 HEIGHT;

  /*
   *
   */

  private final Pane                   root;
  private BackgammonGame               game;
  private final BackgammonBoardDisplay backgammonBoardDisplay;

  public JavaFXDisplay(final Pane root) throws URISyntaxException {
    this.root = root;

    JavaFXDisplay.WIDTH = root.getWidth();
    JavaFXDisplay.HEIGHT = root.getHeight();

    this.backgammonBoardDisplay = new BackgammonBoardDisplay(root);
  }

  @Override
  public void update(final Game game) {
    this.game = (BackgammonGame) game;

    this.backgammonBoardDisplay.update(this.game);
  }

  /*
   * Display
   */

  public void showMessage(final String message, final EventHandler<ActionEvent> onFinished) {
    System.out.println("Show message '" + message + "'.");

    final Text text = new Text();
    text.getStyleClass().add("message");
    text.setText(message);
    text.autosize();

    final BorderPane stackPane = new BorderPane(text);
    stackPane.setPrefWidth(JavaFXDisplay.WIDTH);
    stackPane.setPrefHeight(JavaFXDisplay.HEIGHT);
    this.root.getChildren().add(stackPane);

    final SequentialTransition sequentialTransition = new SequentialTransition(stackPane, createBlinker(stackPane));
    sequentialTransition.setOnFinished(onFinished);
    sequentialTransition.play();
  }

  private Timeline createBlinker(final Node node) {
    final Timeline blink = new Timeline(new KeyFrame(Duration.seconds(0), new KeyValue(node.opacityProperty(), 0, Interpolator.DISCRETE)),
        new KeyFrame(Duration.seconds(0.5), new KeyValue(node.opacityProperty(), 1, Interpolator.DISCRETE)),
        new KeyFrame(Duration.seconds(1), new KeyValue(node.opacityProperty(), 0, Interpolator.DISCRETE)));
    blink.setCycleCount(3);

    return blink;
  }

  public void showGameOverScreen(final Color color, final int turn) {
    System.out.println("Showing Game Over screen ...");

    this.root.setId("gameOverRoot");
    this.root.getChildren().clear();

    final VBox vbox = new VBox();
    final Text text = new Text();
    text.getStyleClass().add("gameOver");
    text.setText(color.name().substring(0, 1)
        + color.name().substring(1, color.name().length()).toLowerCase()
        + " player wins in "
        + turn
        + " turns.");
    vbox.getChildren().add(text);

    final SequentialTransition sequentialTransition = new SequentialTransition(vbox, createDropDownAnimation(vbox, 4000));

    this.root.getChildren().add(vbox);
    sequentialTransition.play();

    this.root.getScene().setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.ENTER) {
        // Restart the game.
        this.root.setId("root");

        BackgammonGame game;
        try {
          game = new BackgammonGame(new BackgammonBoard(), this.game.getPlayers(), new BackgammonRules(), this);
          game.start();
        } catch (final Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  private PathTransition createDropDownAnimation(final Node node, final int millis) {
    System.out.println(node.getLayoutBounds().getWidth());
    final Path path = new Path();
    path.getElements().add(new MoveTo(JavaFXDisplay.WIDTH / 2, 0));
    path.getElements().add(new LineTo(JavaFXDisplay.WIDTH / 2, JavaFXDisplay.HEIGHT / 2));

    final PathTransition pathTransition = new PathTransition();
    pathTransition.setDuration(Duration.millis(millis));
    pathTransition.setPath(path);
    pathTransition.setNode(node);

    return pathTransition;
  }

  /**
   * Show column numbers in order to debug easily.
   */
  public void showHideColumnNumbers() {
    this.backgammonBoardDisplay.showHideColumnNumbers();
  }

  /*
   * Sounds
   */

  public void playRollingDiceSound() {
    this.backgammonBoardDisplay.playRollingDiceSound();
  }

  /*
   * Controls
   */

  public void left() {
    this.backgammonBoardDisplay.left();
  }

  public void right() {
    this.backgammonBoardDisplay.right();
  }

  public void select() {
    this.backgammonBoardDisplay.select();
  }

  public void select(final int position) {
    this.backgammonBoardDisplay.select(position);
  }

  /*
   * For IA purpose.
   */
  public void selectLater(final int from, final int to) {
    this.backgammonBoardDisplay.selectLater(from, to);
  }

  public void initCursorSelectedPosition() {
    this.backgammonBoardDisplay.initCursorSelectedPosition();
  }

  /*
   * Getters
   */

  public Pane getRoot() {
    return this.root;
  }

}
