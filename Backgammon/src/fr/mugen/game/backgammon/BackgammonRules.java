package fr.mugen.game.backgammon;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
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
    final Color playerColor = ((BackgammonPlayer) player).getColor();
    final int moveLength = Math.abs(from.getPosition() - to.getPosition());

    // System.out.println("FROM " + from+ " TO " + to);
    // Unselect case
    if (from.equals(to))
      return true;

    return from.getColor() == playerColor && from.getNumber() > 0
        && (Color.NONE == to.getColor() || from.getColor() == to.getColor() || to.getNumber() == 1) // Eat
                                                                                                    // it
                                                                                                    // !
        && ((Color.WHITE.equals(playerColor) && (from.getPosition() - to.getPosition()) > 0)
            || (Color.BLACK.equals(playerColor) && (from.getPosition() - to.getPosition()) < 0))
        && (dice.getDice1() == moveLength || dice.getDice2() == moveLength || moveLength == dice.getRange()
            || (dice.isDoubleDice() && moveLength % dice.getDice1() == 0 && moveLength <= dice.getRange()));

    // if (from.getNumber() == 0)
    // return false;
    // if (from.getColor() != to.getColor())
    // return false;
    // if (Color.WHITE.equals(from.getColor()) && !playerIsWhite ||
    // !(from.isWhite() && playerIsWhite))
    // return false;
    //
    // final int moveLength = from.getPosition() - to.getPosition();
    // if (Color.WHITE.equals(playerColor) && moveLength <= 0 || !playerIsWhite
    // && moveLength >= 0)
    // return false;
    //
    // final int moveLengthAbs = Math.abs(moveLength);
    // if (dice.getDice1() != moveLengthAbs && dice.getDice2() != moveLengthAbs
    // && moveLengthAbs % (dice.getDice1() != 0 ? dice.getDice1() :
    // dice.getDice2()) != 0 || moveLengthAbs > dice.getRange())
    // return false;
    //
    // return true;
  }

  public boolean isSelectable(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn column) {
    return column.getColor() == player.getColor() && column.getNumber() > 0;
  }

  public int getPlayersDefaultPosition(final Board board, final Color color) {
    return ((BackgammonBoard) board).getColumns().stream().filter(p -> p.getColor().equals(color)).findFirst().get().getPosition();
  }

  public int getNextPossiblePositionOnLeft(final Board board, final Player player, final BackgammonColumn currentColumn,
      final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();
    final int sideFactor = (currentPosition <= 12 ? 1 : -1);
    final Stream<BackgammonColumn> columns = ((BackgammonBoard) board).getColumns().stream().filter(column -> {
      return (column.getPosition() - currentPosition) * sideFactor < 0 && column.getPosition() / 13 == currentPosition / 13 // Avoid
                                                                                                                            // going
                                                                                                                            // to
                                                                                                                            // next
                                                                                                                            // line
          && (selectedColumn != null ? check(board, player, new BackgammonMove(selectedColumn, column))
              : isSelectable((BackgammonBoard) board, (BackgammonPlayer) player, column));
    });

    try {
      return columns.max((p1, p2) -> (p1.getPosition() - p2.getPosition()) * sideFactor).get().getPosition();
    } catch (final NoSuchElementException e) {
      return currentPosition;
    }
  }

  public int getNextPossiblePositionOnRight(final Board board, final Player player, final BackgammonColumn currentColumn,
      final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();
    final int sideFactor = (currentPosition <= 12 ? 1 : -1);
    final Stream<BackgammonColumn> columns = ((BackgammonBoard) board).getColumns().stream().filter(column -> {
      return (column.getPosition() - currentPosition) * sideFactor > 0 && column.getPosition() / 13 == currentPosition / 13 // Avoid
                                                                                                                            // going
                                                                                                                            // to
                                                                                                                            // next
                                                                                                                            // line
          && (selectedColumn != null ? check(board, player, new BackgammonMove(selectedColumn, column))
              : isSelectable((BackgammonBoard) board, (BackgammonPlayer) player, column));

    });

    try {
      return columns.min((p1, p2) -> (p1.getPosition() - p2.getPosition()) * sideFactor).get().getPosition();
    } catch (final NoSuchElementException e) {
      return currentPosition;
    }
  }

  public int getNextPossiblePositionUpward(final Board board, final Player player, final BackgammonColumn currentColumn,
      final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();

    if (currentPosition >= 1 && currentPosition <= 12)
      return currentPosition;

    final int sideFactor = (currentPosition <= 12 ? 1 : -1);
    final Stream<BackgammonColumn> columns = ((BackgammonBoard) board).getColumns().stream().filter(column -> {
      return column.getPosition() >= 1 && column.getPosition() <= 12
          && (selectedColumn != null ? check(board, player, new BackgammonMove(selectedColumn, column))
              : isSelectable((BackgammonBoard) board, (BackgammonPlayer) player, column));
    });

    try {
      return columns.min((p1, p2) -> (p1.getPosition() - p2.getPosition()) * sideFactor).get().getPosition();
    } catch (final NoSuchElementException e) {
      return currentPosition;
    }
  }

  public int getNextPossiblePositionDownward(final Board board, final Player player, final BackgammonColumn currentColumn,
      final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();

    if (currentPosition >= 13 && currentPosition <= 24)
      return currentPosition;

    final int sideFactor = (currentPosition <= 12 ? 1 : -1);
    final Stream<BackgammonColumn> columns = ((BackgammonBoard) board).getColumns().stream().filter(column -> {
      return column.getPosition() >= 13 && column.getPosition() <= 24
          && (selectedColumn != null ? check(board, player, new BackgammonMove(selectedColumn, column))
              : isSelectable((BackgammonBoard) board, (BackgammonPlayer) player, column));
    });

    try {
      return columns.min((p1, p2) -> (p1.getPosition() - p2.getPosition()) * sideFactor).get().getPosition();
    } catch (final NoSuchElementException e) {
      return currentPosition;
    }
  }

}
