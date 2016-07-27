package fr.mugen.game.backgammon.controls;

import fr.mugen.game.backgammon.ApplicationLauncher;
import fr.mugen.game.backgammon.display.JavaFXDisplay;
import fr.mugen.game.framework.Controls;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class JavaFXControls implements Controls {

  private final JavaFXDisplay display;

  public JavaFXControls(final JavaFXDisplay display) {
    this.display = display;
  }

  @Override
  public void enable() {
    this.display.getRoot().getScene().setOnKeyPressed(e -> {
      final String code = e.getCode().toString();
      switch (KeyCode.valueOf(code)) {
        case LEFT:
          this.display.left();
        break;
        case RIGHT:
          this.display.right();
        break;
        case SPACE:
          this.display.select();
          break;
        case TAB:
          this.display.showHideColumnNumbers();
          break;
        case ESCAPE:
          ApplicationLauncher.showMenu(this.display.getRoot(), 0);
        default:
      }
    });
  }

  @Override
  public void disable() {
    this.display.getRoot().getScene().setOnKeyPressed(null);
  }

}
