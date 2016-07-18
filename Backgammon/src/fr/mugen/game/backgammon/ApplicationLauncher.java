package fr.mugen.game.backgammon;

import java.util.Arrays;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.controls.JavaFXControls;
import fr.mugen.game.backgammon.display.JavaFXDisplay;
import fr.mugen.game.backgammon.player.HumanPlayer;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ApplicationLauncher extends Application {

  private Pane root;

  @Override
  public void start(final Stage primaryStage) throws Exception {
    // Scene initialization
    primaryStage.setTitle("Backgammon");
    primaryStage.setResizable(false);

    this.root = new Pane();
    this.root.setId("root");

    final Scene scene = new Scene(this.root, 710, 630);
    scene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("css/main.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.show();

    // Game initialization
    final JavaFXDisplay display = new JavaFXDisplay(this.root);
    final Controls controls = new JavaFXControls(display);
    final BackgammonGame game = new BackgammonGame(new BackgammonBoard(),
        Arrays.asList((Player) new HumanPlayer(controls, Color.WHITE), (Player) new HumanPlayer(controls, Color.BLACK)), new BackgammonRules(), display);

    game.start();
  }

  public static void main(final String[] args) {
    Application.launch(args);
  }

}
