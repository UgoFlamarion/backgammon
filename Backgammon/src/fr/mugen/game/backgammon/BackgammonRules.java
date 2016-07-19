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
    final Dice dice = ((BackgammonBoard) board).getDice();
    final BackgammonMove backgammonMove = (BackgammonMove) move;
    final BackgammonColumn from = backgammonMove.getFrom();
    final BackgammonColumn to = backgammonMove.getTo();
    final Color playerColor = ((BackgammonPlayer) player).getColor();
    final int moveLength = Math.abs((from.getPosition() > 0 ? from.getPosition() : 0) - to.getPosition());

    // Unselect case
    if (to.getPosition() > 0 && from.equals(to))
      return true;

    return from.getColor() == playerColor && from.getNumber() > 0
        && (Color.NONE == to.getColor() || from.getColor() == to.getColor() || to.getNumber() == 1)
        && (Color.WHITE.equals(playerColor) && from.getPosition() - to.getPosition() > 0
            || Color.BLACK.equals(playerColor) && from.getPosition() - to.getPosition() < 0)
        && (dice.getDice1() == moveLength || dice.getDice2() == moveLength || moveLength == dice.getRange()
            || dice.isDoubleDice() && moveLength % dice.getDice1() == 0 && moveLength <= dice.getRange())
        && to.getPosition() > 0;
  }

  public BackgammonMove getMove(final BackgammonColumn from, final BackgammonColumn to) {
    return new BackgammonMove(from, to, to.getColor() != Color.NONE && from.getColor() != to.getColor());
  }

  public boolean isSelectable(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn column) {
    return column.getColor() == player.getColor() && column.getNumber() > 0;
  }

  public int getCursorDefaultPosition(final Board board, final Color color) {
    return ((BackgammonBoard) board).getColumns().stream().filter(p -> p.getColor().equals(color) && p.getNumber() > 0 && p.getPosition() > 0).findFirst().get().getPosition();
  }

  public int getNextPossiblePositionOnLeft(final Board board, final Player player, final BackgammonColumn currentColumn,
      final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();
    final int sideFactor = currentPosition <= 12 ? 1 : -1;
    final Stream<BackgammonColumn> columns = ((BackgammonBoard) board).getColumns().stream().filter(column -> {
      return (column.getPosition() - currentPosition) * sideFactor < 0
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
    final int sideFactor = currentPosition <= 12 ? 1 : -1;
    final Stream<BackgammonColumn> columns = ((BackgammonBoard) board).getColumns().stream().filter(column -> {
      return (column.getPosition() - currentPosition) * sideFactor > 0
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

    final int sideFactor = currentPosition <= 12 ? 1 : -1;
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

    final int sideFactor = currentPosition <= 12 ? 1 : -1;
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
