package fr.mugen.game.backgammon.player.ai;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import fr.mugen.game.backgammon.BackgammonColumn;
import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.BackgammonRules;
import fr.mugen.game.backgammon.display.JavaFXDisplay;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Rules;

public class EasyComputerPlayer extends ComputerPlayer {

  public EasyComputerPlayer(final Controls controls, final Color color) {
    super(controls, color);
  }

  @Override
  protected void _play(final Board board, final Rules rules, final Display display) {
    final HashMap<BackgammonColumn, List<BackgammonColumn>> possibilities = ((BackgammonRules) rules).getPossibilities();

    // No possibility
    if (possibilities.keySet().isEmpty())
      return;

    final BackgammonColumn selectableColumn = possibilities.keySet().iterator().next();

    final int from = selectableColumn.getPosition();
    int to = from;

    final Iterator<BackgammonColumn> possibilitiesIterator = possibilities.get(selectableColumn).iterator();
    while (to == from)
      to = possibilitiesIterator.next().getPosition();

    System.out.println("IA chose to move from " + from + " to " + to);
    ((JavaFXDisplay) display).selectLater(from, to);
  }

  @Override
  public void stop() {

  }

}
