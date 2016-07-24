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
    final int moveLength = backgammonMove.getMoveLength();
    // final int moveLength = Math.abs(from.getPosition() - to.getPosition()) +
    // (BackgammonBoard.IS_HEAVEN(to.getPosition()) ?
    // getSideFactor(to.getPosition()) : 0);

    // Unselect case
    if (from.equals(to) || (!BackgammonBoard.IS_CEMETERY(to.getPosition()) && from.equals(to)))
      return true;

    // System.out.println("Check (" + from.getPosition() + "--> " +
    // to.getPosition() + ") : " +
    // !BackgammonBoard.IS_CEMETERY(to.getPosition())
    // + " " + (from.getColor() == playerColor) + " " + (from.getNumber() > 0) +
    // " "
    // + (Color.NONE == to.getColor() || from.getColor() == to.getColor() ||
    // to.getNumber() == 1) + " "
    // + (Color.WHITE.equals(playerColor) && from.getPosition() -
    // to.getPosition() > 0
    // || Color.BLACK.equals(playerColor) && from.getPosition() -
    // to.getPosition() < 0)
    // + " " + (dice.getDice1() == moveLength || dice.getDice2() ==
    // moveLength));

    return !BackgammonBoard.IS_CEMETERY(to.getPosition())
        && (from.getColor() == playerColor)
        && (from.getNumber() > 0)
        && ((Color.NONE == to.getColor()) || (from.getColor() == to.getColor()) || (to.getNumber() == 1))
        && ((Color.WHITE.equals(playerColor) && ((from.getPosition() - to.getPosition()) > 0))
            || (Color.BLACK.equals(playerColor) && ((from.getPosition() - to.getPosition()) < 0)))
        && ((dice.getDice1() == moveLength)
            || (dice.getDice2() == moveLength)
            || (BackgammonBoard.IS_HEAVEN(to.getPosition())
                && ((moveLength + BackgammonRules.getSideFactor(to.getPosition())) <= dice.getRange())));
  }

  public boolean isSelectable(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn column) {
    // System.out.println("isSelectable Column " + column.getPosition() + "--> "
    // + column.getColor() + " == " + player.getColor() + " && "
    // + column.getNumber() + " > 0 =====>" + (column.getColor() ==
    // player.getColor() && column.getNumber() > 0));

    final int cemeteryPosition = BackgammonBoard.COLOR_TO_CEMETERY_POSITION(player.getColor());
    return ((board.getColumn(cemeteryPosition).getNumber() == 0) || (column.getPosition() == cemeteryPosition))
        && (column.getColor() == player.getColor())
        && (column.getNumber() > 0);
  }

  public boolean calculatePossibilities(final BackgammonBoard board, final BackgammonPlayer player) {
    this.possibilities = new HashMap<BackgammonColumn, List<BackgammonColumn>>();

    System.out.println("Calculating possibilities (" + player.getColor() + ") ...");
    board.getColumns().stream().filter(column -> isSelectable(board, player, column)).forEach(selectableColumn -> {
      final List<BackgammonColumn> columns = board.getColumns().stream().filter(column -> {
        return check(board, player, new BackgammonMove(selectableColumn, column));
      }).collect(Collectors.toList());

      // Avoid adding selectable columns for which the only possibility is
      // itself.
      if (columns.size() > 1)
        this.possibilities.put(selectableColumn, columns);
      else
        System.out.println("Didn't add " + selectableColumn.getPosition() + " -> " + columns.get(0).getPosition());
    });

    cleanUpPossibilities(board, player);

    // Debug output.
    for (final Entry<BackgammonColumn, List<BackgammonColumn>> e : this.possibilities.entrySet())
      e.getValue().forEach(c -> {
        System.out.println(e.getKey().getPosition() + " -> " + c.getPosition());
      });

    if (this.possibilities.isEmpty())
      return false;
    return true;
  }

  private void cleanUpPossibilities(final BackgammonBoard board, final BackgammonPlayer player) {
    final BackgammonColumn cemetery = board.getColumn(BackgammonBoard.COLOR_TO_CEMETERY_POSITION(player.getColor()));
    final List<BackgammonColumn> cemeteryPossibilities = this.possibilities.get(cemetery);

    if (cemeteryPossibilities != null) {
      // Remove cemetery possibility from selectable cemetery.
      cemeteryPossibilities.remove(cemetery);

      // Remove all other selectable columns.
      final List<BackgammonColumn> toDelete = new ArrayList<>();
      this.possibilities.forEach((selectableColumn, columns) -> {
        if (selectableColumn != cemetery)
          toDelete.add(selectableColumn);
      });
      toDelete.forEach(c -> this.possibilities.remove(c));
      return; // We can go out at this point.
    }

    // Remove possibilities where checker can go to heaven, but another further
    // checker exists, or not all checkers are on player's side.
    this.possibilities.forEach((selectableColumn, columns) -> {
      final List<BackgammonColumn> toDelete = new ArrayList<>();
      columns.forEach(toColumn -> {
        final BackgammonMove move = new BackgammonMove(selectableColumn, toColumn);
        if (BackgammonBoard.IS_HEAVEN(toColumn.getPosition())
            && ((existFurtherCheckerForHeaven(selectableColumn)
                && (move.getMoveLength() != board.getDice().getDice1())
                && (move.getMoveLength() != board.getDice().getDice2())) || existCheckerOutsideCamp(board, selectableColumn))) {
          System.out.println("Delete Column " + toColumn.getPosition() + " from selectable column " + selectableColumn.getPosition());
          toDelete.add(toColumn);
        }
      });
      this.possibilities.get(selectableColumn).removeAll(toDelete);
    });

    // Remove selectable columns with less than 2 possibilities, except for
    // cemeteries.
    final List<BackgammonColumn> toDelete = new ArrayList<>();
    this.possibilities.forEach((selectableColumn, columns) -> {
      if ((columns.size() < 2) && !BackgammonBoard.IS_CEMETERY(selectableColumn.getPosition()))
        toDelete.add(selectableColumn);
    });
    toDelete.forEach(c -> this.possibilities.remove(c));
  }

  private boolean existFurtherCheckerForHeaven(final BackgammonColumn selectableColumn) {
    return this.possibilities.keySet().stream().filter(p -> {
      final int possibilityPosition = selectableColumn.getPosition();
      return (this.possibilities.get(p).stream().filter(p2 -> BackgammonBoard.IS_HEAVEN(p2.getPosition())).count() > 0)
          && (((p.getPosition() - possibilityPosition) * BackgammonRules.getSideFactor(possibilityPosition)) > 0);
    }).count() > 0;
  }

  private boolean existCheckerOutsideCamp(final BackgammonBoard board, final BackgammonColumn selectableColumn) {
    return board.getColumns().stream()
        .filter(p -> (p.getColor() == selectableColumn.getColor())
            && (p.getNumber() > 0)
            && (Math.abs(p.getPosition() - BackgammonBoard.COLOR_TO_HEAVEN_POSITION(selectableColumn.getColor())) > 7))
        .count() > 0;
  }

  public BackgammonMove initializeMove(final BackgammonColumn from, final BackgammonColumn to) {
    return new BackgammonMove(from, to, (to.getColor() != Color.NONE) && (from.getColor() != to.getColor()));
  }

  public int getCursorDefaultPosition(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn selectedColumn) {
    final Collection<BackgammonColumn> columns = selectedColumn != null ? this.possibilities.get(selectedColumn)
        : this.possibilities.keySet();
    if ((columns != null) && (columns.size() > 0))
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
          .filter(column -> (((column.getPosition() - currentPosition) * sideFactor) < 0)
              && (((column.getPosition() / 13) == (currentPosition / 13)) || BackgammonBoard.IS_HEAVEN(column.getPosition())))
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
          .filter(column -> (((column.getPosition() - currentPosition) * sideFactor) > 0)
              && (((column.getPosition() / 13) == (currentPosition / 13)) || BackgammonBoard.IS_HEAVEN(currentPosition)))
          .min(BackgammonRules.newPositionComparator(sideFactor)).get().getPosition();
    } catch (final NoSuchElementException e) {
      return currentPosition;
    }
  }

  public int getNextPossiblePositionUpward(final BackgammonBoard board, final BackgammonPlayer player, final BackgammonColumn currentColumn,
      final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();

    if ((currentPosition >= 1) && (currentPosition <= 12))
      return currentPosition;

    final int sideFactor = BackgammonRules.getSideFactor(currentPosition);
    try {
      final Stream<BackgammonColumn> stream = selectedColumn != null ? this.possibilities.get(selectedColumn).stream()
          : this.possibilities.keySet().stream();
      return stream.filter(column -> (column.getPosition() >= 1) && (column.getPosition() <= 12))
          .min(BackgammonRules.newPositionComparator(sideFactor)).get().getPosition();
    } catch (final NoSuchElementException e) {
      return currentPosition;
    }
  }

  public int getNextPossiblePositionDownward(final BackgammonBoard board, final BackgammonPlayer player,
      final BackgammonColumn currentColumn, final BackgammonColumn selectedColumn) {
    final int currentPosition = currentColumn.getPosition();

    if ((currentPosition >= 13) && (currentPosition <= 24))
      return currentPosition;

    final int sideFactor = BackgammonRules.getSideFactor(currentPosition);
    try {
      final Stream<BackgammonColumn> stream = selectedColumn != null ? this.possibilities.get(selectedColumn).stream()
          : this.possibilities.keySet().stream();
      return stream.filter(column -> (column.getPosition() >= 13) && (column.getPosition() <= 24))
          .min(BackgammonRules.newPositionComparator(sideFactor)).get().getPosition();
    } catch (final NoSuchElementException e) {
      return currentPosition;
    }
  }

  /**
   * Returns 1 for up side and -1 for down side of the board.
   */
  public static int getSideFactor(final int currentPosition) {
    return currentPosition <= 12 ? 1 : -1;
  }

  private static Comparator<? super BackgammonColumn> newPositionComparator(final int sideFactor) {
    return (p1, p2) -> (p1.getPosition() - p2.getPosition()) * sideFactor;
  }

  /*
   * Getters
   */

  public HashMap<BackgammonColumn, List<BackgammonColumn>> getPossibilities() {
    return this.possibilities;
  }

}
