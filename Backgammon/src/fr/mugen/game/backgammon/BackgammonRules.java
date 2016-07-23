package fr.mugen.game.backgammon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.player.BackgammonPlayer;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Move;
import fr.mugen.game.framework.Player;
import fr.mugen.game.framework.Rules;

public class BackgammonRules implements Rules {

  private HashMap<BackgammonColumn, List<BackgammonColumn>> possibilities;

  public BackgammonRules() {
    this.possibilities = new HashMap<BackgammonColumn, List<BackgammonColumn>>();
  }

  @Override
  public boolean check(final Board board, final Player player, final Move move) {
    final Dice dice = ((BackgammonBoard) board).getDice();
    final BackgammonMove backgammonMove = (BackgammonMove) move;
    final BackgammonColumn from = backgammonMove.getFrom();
    final BackgammonColumn to = backgammonMove.getTo();
    final Color playerColor = ((BackgammonPlayer) player).getColor();
    final int moveLength = Math.abs(from.getPosition() - to.getPosition());
    // final int moveLength = Math.abs(from.getPosition() - to.getPosition()) +
    // (BackgammonBoard.IS_HEAVEN(to.getPosition()) ?
    // getSideFactor(to.getPosition()) : 0);

    // Unselect case
    if (from.equals(to) || !BackgammonBoard.IS_CEMETERY(to.getPosition()) && from.equals(to))
      return true;

    // System.out.println("Check (" + from.getPosition() + "--> " +
    // to.getPosition() + ") : " +
    // !BackgammonBoard.IS_CEMETERY(to.getPosition()) + " " + (from.getColor()
    // == playerColor) + " " + (from.getNumber() > 0)
    // + " " + (Color.NONE == to.getColor() || from.getColor() == to.getColor()
    // || to.getNumber() == 1)
    // + " " + (Color.WHITE.equals(playerColor) && from.getPosition() -
    // to.getPosition() > 0
    // || Color.BLACK.equals(playerColor) && from.getPosition() -
    // to.getPosition() < 0)
    // + " " + (dice.getDice1() == moveLength || dice.getDice2() ==
    // moveLength));

    return !BackgammonBoard.IS_CEMETERY(to.getPosition()) && from.getColor() == playerColor && from.getNumber() > 0
        && (Color.NONE == to.getColor() || from.getColor() == to.getColor() || to.getNumber() == 1)
        && (Color.WHITE.equals(playerColor) && from.getPosition() - to.getPosition() > 0
            || Color.BLACK.equals(playerColor) && from.getPosition() - to.getPosition() < 0)
        && (dice.getDice1() == moveLength || dice.getDice2() == moveLength || BackgammonBoard.IS_HEAVEN(to.getPosition())
            && moveLength + BackgammonRules.getSideFactor(to.getPosition()) <= dice.getRange());
    // || dice.isDoubleDice() && moveLength % dice.getDice1() == 0 && moveLength
    // <= dice.getRange());
  }

  public boolean isSelectable(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn column) {
    // System.out.println("isSelectable Column " + column.getPosition() + "--> "
    // + column.getColor() + " == " + player.getColor() + " && "
    // + column.getNumber() + " > 0 =====>" + (column.getColor() ==
    // player.getColor() && column.getNumber() > 0));

    final int cemeteryPosition = BackgammonBoard.COLOR_TO_CEMETERY_POSITION(player.getColor());
    return (board.getColumn(cemeteryPosition).getNumber() == 0 || column.getPosition() == cemeteryPosition)
        && column.getColor() == player.getColor() && column.getNumber() > 0;
  }

  public boolean calculatePossibilities(final BackgammonBoard board, final BackgammonPlayer player) {
    this.possibilities = new HashMap<BackgammonColumn, List<BackgammonColumn>>();

    System.out.println("Calculating possibilities (" + player.getColor() + ") ...");
    board.getColumns().stream().filter(column -> isSelectable(board, player, column)).forEach(selectableColumn -> {
      final List<BackgammonColumn> columns = board.getColumns().stream().filter(column -> {
        return check(board, player, new BackgammonMove(selectableColumn, column));
      }).collect(Collectors.toList());

      // // Avoid adding selectable columns for which the only possibility is
      // itself.
      if (columns.size() > 1)
        this.possibilities.put(selectableColumn, columns);
    });

    // this.possibilities.keySet().stream().filter(p ->
    // this.possibilities.get(p).isEmpty()).forEach(c ->
    // this.possibilities.remove(c));

    // Remove cemetery possibility from selectable cemeteries options.
    final BackgammonColumn whiteCemetery = board.getColumn(BackgammonBoard.WHITE_CEMETERY_POSITION);
    final BackgammonColumn blackCemetery = board.getColumn(BackgammonBoard.BLACK_CEMETERY_POSITION);
    if (this.possibilities.get(whiteCemetery) != null)
      this.possibilities.get(whiteCemetery).remove(whiteCemetery);
    if (this.possibilities.get(blackCemetery) != null)
      this.possibilities.get(blackCemetery).remove(blackCemetery);

    // Remove possibilities where checker can go to heaven, but another further
    // checker exists, or not all checkers are on player's side.
    this.possibilities.forEach((selectableColumn, columns) -> {
      final List<BackgammonColumn> toDelete = new ArrayList<>();
      columns.forEach(column -> {
        // final int moveLength = Math.abs(column.getPosition() -
        // selectableColumn.getPosition());
        if (BackgammonBoard.IS_HEAVEN(column.getPosition()) && (this.possibilities.keySet().stream()
            .filter(p -> this.possibilities.get(p).stream().filter(p2 -> BackgammonBoard.IS_HEAVEN(p2.getPosition())).count() > 0)
            .count() > 0
            || board.getColumns().stream()
                .filter(p -> p.getColor() == selectableColumn.getColor() && p.getNumber() > 0
                    && Math.abs(p.getPosition() - BackgammonBoard.COLOR_TO_HEAVEN_POSITION(selectableColumn.getColor())) > 8)
                .count() > 0)) {
          System.out.println("Delete Column " + column.getPosition() + " from selectable column " + selectableColumn.getPosition());
          toDelete.add(column);
        }
      });
      this.possibilities.get(selectableColumn).removeAll(toDelete);
    });

    // Remove selectable columns with less than 2 possibilities.
    final List<BackgammonColumn> toDelete = new ArrayList<>();
    this.possibilities.forEach((selectableColumn, columns) -> {
      if (columns.size() < 2)
        toDelete.add(selectableColumn);
    });
    toDelete.forEach(c -> this.possibilities.remove(c));

    for (final Entry<BackgammonColumn, List<BackgammonColumn>> e : this.possibilities.entrySet())
      e.getValue().forEach(c -> {
        System.out.println(e.getKey().getPosition() + " -> " + c.getPosition());
      });

    if (this.possibilities.isEmpty())
      return false;
    return true;
  }

  // public List<BackgammonColumn> getPossibilities() {
  // return possibilities;
  // }

  public BackgammonMove initializeMove(final BackgammonColumn from, final BackgammonColumn to) {
    return new BackgammonMove(from, to, to.getColor() != Color.NONE && from.getColor() != to.getColor());
  }

  public int getCursorDefaultPosition(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn selectedColumn) {
    final Collection<BackgammonColumn> columns = selectedColumn != null ? this.possibilities.get(selectedColumn)
        : this.possibilities.keySet();
    if (columns != null && columns.size() > 0)
      return columns.iterator().next().getPosition();
    return -1;
  }

  public int getNextPossiblePositionOnLeft(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn currentColumn,
      final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();
    final int sideFactor = BackgammonRules.getSideFactor(currentPosition);

    try {
      final Stream<BackgammonColumn> stream = selectedColumn != null ? this.possibilities.get(selectedColumn).stream()
          : this.possibilities.keySet().stream();
      return stream
          .filter(column -> (column.getPosition() - currentPosition) * sideFactor < 0
              && (column.getPosition() / 13 == currentPosition / 13 || BackgammonBoard.IS_HEAVEN(column.getPosition())))
          .max(BackgammonRules.newPositionComparator(sideFactor)).get().getPosition();
    } catch (final NoSuchElementException e) {
      return currentPosition;
    }
  }

  public int getNextPossiblePositionOnRight(final BackgammonBoard board, final BackgammonPlayer player,
      final BackgammonColumn currentColumn, final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();
    final int sideFactor = BackgammonRules.getSideFactor(currentPosition);

    try {
      final Stream<BackgammonColumn> stream = selectedColumn != null ? this.possibilities.get(selectedColumn).stream()
          : this.possibilities.keySet().stream();
      return stream
          .filter(column -> (column.getPosition() - currentPosition) * sideFactor > 0
              && (column.getPosition() / 13 == currentPosition / 13 || BackgammonBoard.IS_HEAVEN(currentPosition)))
          .min(BackgammonRules.newPositionComparator(sideFactor)).get().getPosition();
    } catch (final NoSuchElementException e) {
      return currentPosition;
    }
  }

  public int getNextPossiblePositionUpward(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn currentColumn,
      final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();

    if (currentPosition >= 1 && currentPosition <= 12)
      return currentPosition;

    final int sideFactor = BackgammonRules.getSideFactor(currentPosition);
    // final Stream<BackgammonColumn> columns = ((BackgammonBoard)
    // board).getColumns().stream().filter(column -> {
    // return column.getPosition() >= 1 && column.getPosition() <= 12
    // && (selectedColumn != null ? check(board, player, new
    // BackgammonMove(selectedColumn, column))
    // : isSelectable(board, player, column));
    // });

    try {
      final Stream<BackgammonColumn> stream = selectedColumn != null ? this.possibilities.get(selectedColumn).stream()
          : this.possibilities.keySet().stream();
      return stream.filter(column -> column.getPosition() >= 1 && column.getPosition() <= 12)
          .min(BackgammonRules.newPositionComparator(sideFactor)).get().getPosition();
    } catch (final NoSuchElementException e) {
      return currentPosition;
    }
  }

  public int getNextPossiblePositionDownward(final BackgammonBoard board, final BackgammonPlayer player,
      final BackgammonColumn currentColumn, final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();

    if (currentPosition >= 13 && currentPosition <= 24)
      return currentPosition;

    final int sideFactor = BackgammonRules.getSideFactor(currentPosition);
    // final Stream<BackgammonColumn> columns = ((BackgammonBoard)
    // board).getColumns().stream().filter(column -> {
    // return column.getPosition() >= 13 && column.getPosition() <= 24
    // && (selectedColumn != null ? check(board, player, new
    // BackgammonMove(selectedColumn, column))
    // : isSelectable(board, player, column));
    // });

    try {
      final Stream<BackgammonColumn> stream = selectedColumn != null ? this.possibilities.get(selectedColumn).stream()
          : this.possibilities.keySet().stream();
      return stream.filter(column -> column.getPosition() >= 13 && column.getPosition() <= 24)
          .min(BackgammonRules.newPositionComparator(sideFactor)).get().getPosition();
    } catch (final NoSuchElementException e) {
      return currentPosition;
    }
  }

  private static int getSideFactor(final int currentPosition) {
    return currentPosition <= 12 ? 1 : -1;
  }

  private static Comparator<? super BackgammonColumn> newPositionComparator(final int sideFactor) {
    return (p1, p2) -> (p1.getPosition() - p2.getPosition()) * sideFactor;
  }

}
