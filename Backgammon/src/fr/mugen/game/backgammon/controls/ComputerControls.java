package fr.mugen.game.backgammon.controls;

import fr.mugen.game.backgammon.display.JavaFXDisplay;
import fr.mugen.game.framework.Controls;

public class ComputerControls implements Controls {

  private final JavaFXDisplay display;

  public ComputerControls(final JavaFXDisplay display) {
    this.display = display;
  }

  @Override
  public void enable() {
  }

  @Override
  public void disable() {
  }

}
