package fr.mugen.game.backgammon.player;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Rules;

public class HumanPlayer extends BackgammonPlayer {

  public HumanPlayer(final Controls controls, final Color color) {
    super(controls, color);
  }

  @Override
  public void _play(final Board board, final Rules rules, final Display display) {
    this.controls.enable();
  }

}
