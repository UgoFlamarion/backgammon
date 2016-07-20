package fr.mugen.game.backgammon;

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

    // Unselect case
    if (!BackgammonBoard.IS_CEMETERY(to.getPosition()) && from.equals(to))
      return true;

    return !BackgammonBoard.IS_CEMETERY(to.getPosition()) && from.getColor() == playerColor && from.getNumber() > 0
        && (Color.NONE == to.getColor() || from.getColor() == to.getColor() || to.getNumber() == 1)
        && (Color.WHITE.equals(playerColor) && from.getPosition() - to.getPosition() > 0
            || Color.BLACK.equals(playerColor) && from.getPosition() - to.getPosition() < 0)
        && (dice.getDice1() == moveLength || dice.getDice2() == moveLength || moveLength == dice.getRange());
    // || dice.isDoubleDice() && moveLength % dice.getDice1() == 0 && moveLength
    // <= dice.getRange());
  }

  public boolean isSelectable(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn column) {
    System.out.println("isSelectable Column " + column.getPosition() + "--> " + column.getColor() + " == " + player.getColor() + " && "
        + column.getNumber() + " > 0 =====>" + (column.getColor() == player.getColor() && column.getNumber() > 0));
    return column.getColor() == player.getColor() && column.getNumber() > 0;
  }

  public void calculatePossibilities(final BackgammonBoard board, final BackgammonPlayer player) {
    this.possibilities = new HashMap<BackgammonColumn, List<BackgammonColumn>>();

    System.out.println("Calculating possibilities (" + player.getColor() + ") ...");
    board.getColumns().stream().filter(column -> isSelectable(board, player, column)).forEach(selectableColumn -> {
      final List<BackgammonColumn> columns = board.getColumns().stream().filter(column -> {
        return column != selectableColumn && check(board, player, new BackgammonMove(selectableColumn, column));
      }).collect(Collectors.toList());
      // if (!columns.isEmpty())
      this.possibilities.put(selectableColumn, columns);
    });

    // this.possibilities.keySet().stream().filter(p ->
    // this.possibilities.get(p).isEmpty()).forEach(c ->
    // this.possibilities.remove(c));

    for (final Entry<BackgammonColumn, List<BackgammonColumn>> e : this.possibilities.entrySet())
      e.getValue().forEach(c -> {
        System.out.println(e.getKey().getPosition() + " -> " + c.getPosition());
      });

    // possibilities = ((BackgammonBoard)
    // board).getColumns().stream().filter(column -> {
    // return selectedColumn != null ? check(board, player, new
    // BackgammonMove(selectedColumn, column))
    // : isSelectable(board, player, column);
    // }).collect(Collectors.toList());
  }

  // public List<BackgammonColumn> getPossibilities() {
  // return possibilities;
  // }

  public BackgammonMove initializeMove(final BackgammonColumn from, final BackgammonColumn to) {
    return new BackgammonMove(from, to, to.getColor() != Color.NONE && from.getColor() != to.getColor());
  }

  public int getCursorDefaultPosition(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn selectedColumn) {
    return board.getColumns().stream().filter(column -> (selectedColumn != null
        ? check(board, player, new BackgammonMove(selectedColumn, column)) : isSelectable(board, player, column))).findFirst().get()
        .getPosition();
  }

  public int getNextPossiblePositionOnLeft(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn currentColumn,
      final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();
    final int sideFactor = BackgammonRules.getSideFactor(currentPosition);

    try {
      final Stream<BackgammonColumn> stream = selectedColumn != null ? this.possibilities.get(selectedColumn).stream()
          : this.possibilities.keySet().stream();
      return stream
          .filter(column -> (column.getPosition() - currentPosition) * sideFactor < 0 && column.getPosition() / 13 == currentPosition / 13) // Avoid
                                                                                                                                            // next
                                                                                                                                            // line
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
          .filter(column -> (column.getPosition() - currentPosition) * sideFactor > 0 && column.getPosition() / 13 == currentPosition / 13) // Avoid
                                                                                                                                            // next
                                                                                                                                            // line
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
