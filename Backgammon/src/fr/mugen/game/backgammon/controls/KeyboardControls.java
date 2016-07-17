package fr.mugen.game.backgammon.controls;

import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Move;

public class KeyboardControls implements Controls {

  @Override
  public Move getMove() {
    // Scanner s = new Scanner(System.in);
    // String input = null;
    //
    // while (!checkInput(input))
    // input = s.nextLine();
    //
    // return input;
    return null;
  }

  private boolean checkInput(final String input) {
    return input != null && input.matches("^\\d\\d-\\d\\d$");
  }

  @Override
  public void enable() {
    // TODO Auto-generated method stub

  }

  @Override
  public void disable() {
    // TODO Auto-generated method stub

  }

}
