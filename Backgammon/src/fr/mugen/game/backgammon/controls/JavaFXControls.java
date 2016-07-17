package fr.mugen.game.backgammon.controls;

import fr.mugen.game.backgammon.display.JavaFXDisplay;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Move;
import javafx.scene.input.KeyCode;

public class JavaFXControls implements Controls {

  private final JavaFXDisplay display;

  public JavaFXControls(final JavaFXDisplay display) {
    this.display = display;
  }

  @Override
  public void enable() {
    this.display.showCursor();
    this.display.getRoot().getScene().setOnKeyPressed(e -> {
      final String code = e.getCode().toString();
      switch (KeyCode.valueOf(code)) {
        case LEFT:
          this.display.left();
        break;
        case RIGHT:
          this.display.right();
        break;
        case UP:
          this.display.up();
        break;
        case DOWN:
          this.display.down();
        break;
        case SPACE:
          this.display.select();
        default:
        break;
      }
      // final BackgammonMove move = (BackgammonMove) this.controls.getMove();

    });
  }

  @Override
  public void disable() {
    this.display.getRoot().setOnKeyPressed(null);
  }

  @Override
  public Move getMove() {
    return null;
  }

}
