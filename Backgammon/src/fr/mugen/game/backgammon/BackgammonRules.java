package fr.mugen.game.backgammon;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import fr.mugen.game.backgammon.player.BackgammonPlayer;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Move;
import fr.mugen.game.framework.Player;
import fr.mugen.game.framework.Rules;

public class BackgammonRules implements Rules {

  @Override
  public boolean check(final Board board, final Player player, final Move move) {
    final BackgammonBoard backgammonBoard = (BackgammonBoard) board;
    final Dice dice = backgammonBoard.getDice();
    final BackgammonMove backgammonMove = (BackgammonMove) move;
    final BackgammonColumn from = backgammonMove.getFrom();
    final BackgammonColumn to = backgammonMove.getTo();
    final boolean playerIsWhite = ((BackgammonPlayer) player).isWhite();

    if (from.getNumber() <= 0)
      return false;
    if (from.isWhite() != to.isWhite())
      return false;
    if (from.isWhite() && !playerIsWhite || !(from.isWhite() && playerIsWhite))
      return false;

    final int moveLength = from.getPosition() - to.getPosition();
    if (playerIsWhite && moveLength <= 0 || !playerIsWhite && moveLength >= 0)
      return false;

    final int moveLengthAbs = Math.abs(moveLength);
    if (dice.getDice1() != moveLengthAbs && dice.getDice2() != moveLengthAbs
        && moveLengthAbs % (dice.getDice1() != 0 ? dice.getDice1() : dice.getDice2()) != 0 || moveLengthAbs > dice.getRange())
      return false;

    return true;
  }

  public int getPlayersDefaultPosition(final Board board, final boolean white) {
    for (final BackgammonColumn column : ((BackgammonBoard) board).getColumns())
      if (column.getNumber() > 0 && column.isWhite() == white)
        return column.getPosition();
    return 0;
  }

  public int getNextPossiblePositionOnLeft(final Board board, final int position, final boolean white) {
    // for (int i = position - 1; i >= i / 12 * 12; i--) {
    // final int possiblePosition = i;
    // System.out.println("POSSIBLE POS : " + possiblePosition);
    // final Stream<BackgammonColumn> columns = ((BackgammonBoard)
    // board).getColumns().stream()
    // .filter(p -> p.isWhite() == white && p.getPosition() ==
    // possiblePosition);
    // if (columns.count() > 0)
    // return i;
    // }
    //
    // return position;

    final Stream<BackgammonColumn> columns = ((BackgammonBoard) board).getColumns().stream()
        .filter(p -> p.isWhite() == white && p.getPosition() < position);

    try {
      return columns.findFirst().get().getPosition();
    } catch (final NoSuchElementException e) {
      return position;
    }
  }

}
