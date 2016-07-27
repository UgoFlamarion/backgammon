package fr.mugen.game.backgammon;

import java.util.Arrays;
import java.util.List;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.controls.JavaFXControls;
import fr.mugen.game.backgammon.display.JavaFXDisplay;
import fr.mugen.game.backgammon.player.HumanPlayer;
import fr.mugen.game.backgammon.player.ai.ComputerPlayerFactory;
import fr.mugen.game.backgammon.player.ai.ComputerPlayerFactory.Difficulty;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.InconsistentGameException;
import fr.mugen.game.framework.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ApplicationLauncher extends Application {

  private AnchorPane root;

  private interface MenuCallable {
    public void call(Pane root) throws Exception;
  }

  private enum MenuElement {
    EASY_AI("Easy AI", (root) -> {
      final JavaFXDisplay display = new JavaFXDisplay(root);
      final Controls controls = new JavaFXControls(display);

      ApplicationLauncher.initGame(display,
          Arrays.asList(new HumanPlayer(controls, Color.WHITE), ComputerPlayerFactory.createComputerPlayer(Color.BLACK, Difficulty.EASY)));
    } , 1, 0),
    MEDIUM_AI("Medium AI", (root) -> {
      final JavaFXDisplay display = new JavaFXDisplay(root);
      final Controls controls = new JavaFXControls(display);

      ApplicationLauncher.initGame(display, Arrays.asList(new HumanPlayer(controls, Color.WHITE),
          ComputerPlayerFactory.createComputerPlayer(Color.BLACK, Difficulty.MEDIUM)));
    } , 1, 1),
    HARD_AI("Hard AI", (root) -> {
      final JavaFXDisplay display = new JavaFXDisplay(root);
      final Controls controls = new JavaFXControls(display);

      ApplicationLauncher.initGame(display,
          Arrays.asList(new HumanPlayer(controls, Color.WHITE), ComputerPlayerFactory.createComputerPlayer(Color.BLACK, Difficulty.HARD)));
    } , 1, 2),
    HUMAN_VS_HUMAN("Human VS Human", (root) -> {
      final JavaFXDisplay display = new JavaFXDisplay(root);
      final Controls controls = new JavaFXControls(display);

      ApplicationLauncher.initGame(display, Arrays.asList(new HumanPlayer(controls, Color.WHITE), new HumanPlayer(controls, Color.BLACK)));
    } , 0, 0),
    HUMAN_VS_AI("Human VS AI", (root) -> {
      ApplicationLauncher.showMenu(root, 1);
    } , 0, 1),
    AI_VS_AI("AI VS AI", (root) -> {
      final JavaFXDisplay display = new JavaFXDisplay(root);

      ApplicationLauncher.initGame(display, Arrays.asList(ComputerPlayerFactory.createComputerPlayer(Color.BLACK, Difficulty.MEDIUM),
          ComputerPlayerFactory.createComputerPlayer(Color.WHITE, Difficulty.MEDIUM)));
    } , 0, 2);

    final MenuCallable gameInitializer;
    final int          menuLevel;
    final int          position;
    final Text         text;

    private MenuElement(final String menuLabel, final MenuCallable gameInitializer, final int menuLevel, final int position) {
      this.gameInitializer = gameInitializer;
      this.menuLevel = menuLevel;
      this.position = position;

      this.text = new Text(menuLabel);
      this.text.getStyleClass().add("menuElement");
    }

    public static MenuElement getNextMenuElement(final MenuElement menuElement) {
      for (final MenuElement _menuElement : MenuElement.values())
        if (_menuElement.menuLevel == menuElement.menuLevel && _menuElement.position == menuElement.position + 1)
          return _menuElement;
      return null;
    }

    public static MenuElement getPreviousMenuElement(final MenuElement menuElement) {
      for (final MenuElement _menuElement : MenuElement.values())
        if (_menuElement.menuLevel == menuElement.menuLevel && _menuElement.position == menuElement.position - 1)
          return _menuElement;
      return null;
    }
  }

  private MenuElement currentMenuElement;

  @Override
  public void start(final Stage primaryStage) throws Exception {
    // Scene initialization
    primaryStage.setTitle("Backgammon");
    primaryStage.setResizable(false);

    this.root = new AnchorPane();

    final Scene scene = new Scene(this.root, 710, 630);
    scene.getStylesheets().addAll(this.getClass().getClassLoader().getResource("css/main.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.show();

    ApplicationLauncher.showMenu(this.root, 0);
    selectMenu(MenuElement.HUMAN_VS_HUMAN);

    this.root.getScene().setOnKeyPressed(e -> {
      switch (e.getCode()) {
        case DOWN:
          selectMenu(MenuElement.getNextMenuElement(this.currentMenuElement));
        break;
        case UP:
          selectMenu(MenuElement.getPreviousMenuElement(this.currentMenuElement));
        break;
        case ENTER:
          try {
            this.currentMenuElement.gameInitializer.call(this.root);

            if (this.currentMenuElement == MenuElement.HUMAN_VS_AI)
              selectMenu(MenuElement.EASY_AI);
          } catch (final Exception e1) {
            e1.printStackTrace();
          }
        default:
        break;
      }
    });
  }

  public static void main(final String[] args) {
    Application.launch(args);
  }

  private void selectMenu(final MenuElement menuElement) {
    if (menuElement == null)
      return;

    this.currentMenuElement = menuElement;

    for (final MenuElement _menuElement : MenuElement.values())
      _menuElement.text.getStyleClass().remove("selected");

    menuElement.text.getStyleClass().add("selected");
  }

  public static void showMenu(final Pane root, final int menuLevel) {
    root.setId("menu");
    root.getChildren().clear();

    final VBox vbox = new VBox();
    vbox.getStyleClass().add("menuBox");
    AnchorPane.setBottomAnchor(vbox, (double) 0);
    AnchorPane.setTopAnchor(vbox, (double) 0);
    AnchorPane.setLeftAnchor(vbox, (double) 0);
    AnchorPane.setRightAnchor(vbox, (double) 0);

    final VBox menuPanel = new VBox();
    menuPanel.getStyleClass().add("menuPanel");
    menuPanel.setMaxWidth(10);
    menuPanel.setPadding(new Insets(35));

    vbox.getChildren().add(menuPanel);
    root.getChildren().add(vbox);

    for (final MenuElement menuElement : MenuElement.values())
      if (menuElement.menuLevel == menuLevel)
        menuPanel.getChildren().add(menuElement.text);
  }

  public static void initGame(final Display display, final List<Player> players) throws InconsistentGameException {
    final BackgammonGame game = new BackgammonGame(new BackgammonBoard(), players, new BackgammonRules(), display);
    game.start();
  }

}
