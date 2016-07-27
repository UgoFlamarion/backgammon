package fr.mugen.game.backgammon.player.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.BackgammonColumn;
import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.BackgammonMove;
import fr.mugen.game.backgammon.BackgammonRules;
import fr.mugen.game.backgammon.display.JavaFXDisplay;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Rules;
import fr.mugen.game.framework.utils.DebugUtils;

public class MediumComputerPlayer extends ComputerPlayer {

  /*
   * Priority levels
   */

  private static final int NOT_IN_BASE_CAMP_PRIORITY_LEVEL                = 3;
  private static final int CAN_BE_PROTECTED_PRIORITY_LEVEL                = 5;
  private static final int EAT_CHECKER_PRIORITY_LEVEL                     = 6;
  private static final int PROTECT_CHECKER_PRIORITY_LEVEL                 = 9;
  private static final int PROTECT_CHECKER_AND_CREATE_WALL_PRIORITY_LEVEL = 10;

  public MediumComputerPlayer(final Color color) {
    super(color);
  }

  @Override
  protected void _play(final Board board, final Rules rules, final Display display) {
    final HashMap<BackgammonColumn, List<BackgammonColumn>> possibilities = ((BackgammonRules) rules).getPossibilities();

    // No possibility
    if (possibilities.keySet().isEmpty())
      return;

    final BackgammonMove move = findBestMove((BackgammonBoard) board, (BackgammonRules) rules, possibilities);

    System.out.println("IA chose to move from " + move.getFrom().getPosition() + " to " + move.getTo().getPosition());
    ((JavaFXDisplay) display).selectLater(move.getFrom().getPosition(), move.getTo().getPosition());
  }

  @Override
  public void stop() {

  }

  public BackgammonMove findBestMove(final BackgammonBoard board, final BackgammonRules rules,
      final HashMap<BackgammonColumn, List<BackgammonColumn>> possibilities) {
    final List<Priority> priorities = new ArrayList<>();

    DebugUtils.disableOutput();
    possibilities.forEach((selectableColumn, columns) -> {
      columns.forEach(column -> {
        if (selectableColumn == column)
          return;

        final BackgammonMove move = new BackgammonMove(selectableColumn, column);

        // Deep copy of the board, move and rules to calculate the next
        // possibilities
        // without affecting the current game.
        final BackgammonBoard boardCopy = (BackgammonBoard) board.clone();
        final BackgammonMove moveCopy = (BackgammonMove) move.clone();
        final BackgammonRules rulesCopy = new BackgammonRules();

        boardCopy.move(moveCopy);
        rulesCopy.calculatePossibilities(boardCopy, this);

        final HashMap<BackgammonColumn, List<BackgammonColumn>> nextPossibilities = rulesCopy.getPossibilities();

        final Priority priority = new Priority(move, 0);
        final boolean existEnemyCheckerBefore = board.getColumns().stream().filter(c -> {
          return (c.getColor() != this.color)
              && (c.getNumber() > 0)
              && (((c.getPosition() - column.getPosition()) * (this.color == Color.WHITE ? -1 : 1)) > 0);
        }).count() > 0;

        if (!existEnemyCheckerBefore && (move.getTo().getNumber() == 1) && (move.getTo().getColor() == this.color))
          priority.increaseWeight(Priority.priorityLevelToWeight(MediumComputerPlayer.PROTECT_CHECKER_AND_CREATE_WALL_PRIORITY_LEVEL));

        if (!existEnemyCheckerBefore && (move.getFrom().getNumber() == 1) && (move.getTo().getNumber() > 0))
          priority.increaseWeight(Priority.priorityLevelToWeight(MediumComputerPlayer.PROTECT_CHECKER_PRIORITY_LEVEL));

        if (move.isEating())
          priority.increaseWeight(Priority.priorityLevelToWeight(MediumComputerPlayer.EAT_CHECKER_PRIORITY_LEVEL));

        if ((((move.getTo().getNumber() == 0) && (nextPossibilities.keySet().stream().filter(nextSelectableColumn -> {
          return nextPossibilities.get(nextSelectableColumn).stream().filter(nextColumn -> {
            return nextColumn.getPosition() == column.getPosition();
          }).count() > 0;
        }).count() > 0)) || ((moveCopy.getFrom().getNumber() == 1) && (nextPossibilities.keySet().stream().filter(nextSelectableColumn -> {
          return nextPossibilities.get(nextSelectableColumn).stream().filter(nextColumn -> {
            return nextColumn.getPosition() == move.getTo().getPosition();
          }).count() > 0;
        }).count() > 0))) && (boardCopy.getDice().getRange() == 0))
          priority.increaseWeight(Priority.priorityLevelToWeight(MediumComputerPlayer.CAN_BE_PROTECTED_PRIORITY_LEVEL));

        if (BackgammonBoard.POSITION_TO_SIDE_COLOR(column.getPosition()) != this.color)
          priority.increaseWeight(Priority.priorityLevelToWeight(MediumComputerPlayer.NOT_IN_BASE_CAMP_PRIORITY_LEVEL));

        if ((moveCopy.getFrom().getNumber() == 1) || (moveCopy.getTo().getNumber() == 1))
          priority.decreaseWeight(5);

        priorities.add(priority);

      });
    });
    DebugUtils.enableOutput();

    for (final Priority priority : priorities)
      System.out.println("Priority ("
          + priority.getWeight()
          + ") : "
          + priority.getMove().getFrom().getPosition()
          + " -> "
          + priority.getMove().getTo().getPosition());

    final Priority topPriority = priorities.stream().max((p1, p2) -> {
      return p1.getWeight() - p2.getWeight();
    }).get();

    // If multiple priorities of the same weight, take the farthest checker, or
    // the nearest if it's to protect a checker.
    final List<Priority> sameWeightPriorities = priorities.stream()
        .filter(priority -> (priority != topPriority) && (priority.getWeight() == topPriority.getWeight())).collect(Collectors.toList());
    if (sameWeightPriorities.size() > 0) {
      System.out.println("Multiple priorities with same weight found.");
      final boolean isProtectCheckerPrioritylevel = Priority
          .weightToLevel(topPriority.getWeight()) >= MediumComputerPlayer.PROTECT_CHECKER_PRIORITY_LEVEL;
      System.out.println("isProtectCheckerPrioritylevel : " + isProtectCheckerPrioritylevel);
      System.out.println("color : " + this.color);

      // Comparator<? super Priority> comparator = isProtectCheckerPrioritylevel
      // ? (p1, p2) -> p2.getMove().getFrom().getPosition() -
      // p1.getMove().getFrom().getPosition() : (p1, p2) ->
      // p1.getMove().getFrom().getPosition() -
      // p2.getMove().getFrom().getPosition();
      // return color != Color.WHITE ?
      // sameWeightPriorities.stream().max(comparator).get().getMove() :
      // sameWeightPriorities.stream().min(comparator).get().getMove();
      final Comparator<? super Priority> comparator = (p1, p2) -> p2.getMove().getFrom().getPosition()
          - p1.getMove().getFrom().getPosition();
      return ((this.color == Color.WHITE) && isProtectCheckerPrioritylevel)
          || ((this.color == Color.BLACK) && !isProtectCheckerPrioritylevel) ? sameWeightPriorities.stream().max(comparator).get().getMove()
              : sameWeightPriorities.stream().min(comparator).get().getMove();
    }

    return topPriority.getMove();
  }

  public static void main(final String[] args) {
    final Color color = Color.WHITE;
    // final Priority topPriority = new Priority(new BackgammonMove(new
    // BackgammonColumn(17), new BackgammonColumn(19)), 100);
    //
    // List<Priority> priorities = new ArrayList<>();
    // priorities.add(new Priority(new BackgammonMove(new BackgammonColumn(17),
    // new BackgammonColumn(19)), 100));
    // priorities.add(new Priority(new BackgammonMove(new BackgammonColumn(15),
    // new BackgammonColumn(19)), 100));
    //
    // List<Priority> sameWeightPriorities = priorities.stream().filter(priority
    // -> priority != topPriority && priority.getWeight() ==
    // topPriority.getWeight()).collect(Collectors.toList());
    // if (sameWeightPriorities.size() > 0) {
    // System.out.println("Multiple priorities with same weight found.");
    // boolean isProtectCheckerPrioritylevel =
    // Priority.weightToLevel(topPriority.getWeight()) >=
    // PROTECT_CHECKER_PRIORITY_LEVEL;
    // System.out.println("isProtectCheckerPrioritylevel : " +
    // isProtectCheckerPrioritylevel);
    // System.out.println("color : " + color);
    // Comparator<? super Priority> comparator = (p1, p2) ->
    // p2.getMove().getFrom().getPosition() -
    // p1.getMove().getFrom().getPosition();
    // BackgammonMove move = (color == Color.WHITE &&
    // isProtectCheckerPrioritylevel) || (color == Color.BLACK &&
    // !isProtectCheckerPrioritylevel) ?
    // sameWeightPriorities.stream().max(comparator).get().getMove() :
    // sameWeightPriorities.stream().min(comparator).get().getMove();
    //
    // System.out.println(move.getFrom().getPosition());
    // }
    final BackgammonBoard board = new BackgammonBoard();
    final BackgammonColumn column = new BackgammonColumn(17);
    final boolean existEnemyCheckerBefore = board.getColumns().stream().filter(c -> {
      final boolean colorTest = c.getColor() != color;
      final boolean posTest = (c.getNumber() > 0) && (((c.getPosition() - column.getPosition()) * (color == Color.WHITE ? -1 : 1)) > 0);

      System.out.println("Column " + c + " :: colorTest=" + colorTest + " -- posTest=" + posTest);

      return colorTest && posTest;
    }).count() > 0;
    System.out.println(existEnemyCheckerBefore);
  }
}
