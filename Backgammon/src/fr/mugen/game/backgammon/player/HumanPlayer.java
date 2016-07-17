package fr.mugen.game.backgammon.player;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.Dice;
import fr.mugen.game.backgammon.display.JavaFXDisplay;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Rules;

public class HumanPlayer extends BackgammonPlayer {

  public HumanPlayer(final Controls controls, final boolean white) {
    super(controls, white);
  }

  @Override
  public void play(final Board board, final Rules rules, final Display display) {
    final Dice dice = ((BackgammonBoard) board).getDice();
    dice.roll();
    ((JavaFXDisplay) display).showDice(dice);

    this.controls.enable();

    // BackgammonMove move = null;
    // move = (BackgammonMove) this.controls.getMove();

    // while (dice.keepPlaying()) {
    // BackgammonMove move = null;
    // boolean rulesRespected = false;
    //
    // while (move == null || !rulesRespected) {
    // move = (BackgammonMove) this.controls.getInput();
    // if (!(rulesRespected = rules.check(board, this, move)))
    // System.out.println("Bad move.");
    // }
    //
    // move.go();
    // }
  }

}
