package fr.mugen.game.backgammon.factory;

import fr.mugen.game.backgammon.BackgammonColumn;
import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.BackgammonMove;

public class BackgammonMoveFactory {

  public static BackgammonMove createBackgammonMove(final BackgammonColumn from, final BackgammonColumn to) {
    return new BackgammonMove(from, to, (to.getColor() != Color.NONE) && (from.getColor() != to.getColor()));
  }

}
