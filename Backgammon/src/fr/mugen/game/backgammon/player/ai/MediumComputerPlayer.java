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
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Rules;
import fr.mugen.game.framework.utils.DebugUtils;

public class MediumComputerPlayer extends ComputerPlayer {

  /*
   * Priority levels
   */
	
  private static final int LOW_PRIORITY_LEVEL = 1;
  private static final int NOT_IN_BASE_CAMP_PRIORITY_LEVEL = 3;
  private static final int CAN_BE_PROTECTED_PRIORITY_LEVEL = 5;
  private static final int EAT_CHECKER_PRIORITY_LEVEL = 6;
  private static final int PROTECT_CHECKER_PRIORITY_LEVEL = 9;
  private static final int PROTECT_CHECKER_AND_CREATE_WALL_PRIORITY_LEVEL = 10;

  public MediumComputerPlayer(final Controls controls, final Color color) {
    super(controls, color);
  }

  @Override
  protected void _play(final Board board, final Rules rules, final Display display) {
    final HashMap<BackgammonColumn, List<BackgammonColumn>> possibilities = ((BackgammonRules) rules).getPossibilities();

    // No possibility
    if (possibilities.keySet().isEmpty())
      return;

    BackgammonMove move = findBestMove((BackgammonBoard) board, (BackgammonRules) rules, possibilities);

    System.out.println("IA chose to move from " + move.getFrom().getPosition() + " to " + move.getTo().getPosition());
    ((JavaFXDisplay) display).selectLater(move.getFrom().getPosition(), move.getTo().getPosition());
  }

  @Override
  public void stop() {

  }

  public BackgammonMove findBestMove(BackgammonBoard board, BackgammonRules rules, HashMap<BackgammonColumn, List<BackgammonColumn>> possibilities) {
	  List<Priority> priorities = new ArrayList<>();
	  
	  DebugUtils.disableOutput();
	  possibilities.forEach((selectableColumn, columns) -> {
		  columns.forEach(column -> {
			  if (selectableColumn == column)
				  return;
			  
			  BackgammonMove move = new BackgammonMove(selectableColumn, column);
			  
			  // Deep copy of the board, move and rules to calculate the next possibilities 
			  // without affecting the current game.
			  BackgammonBoard boardCopy = (BackgammonBoard) board.clone();
			  BackgammonMove moveCopy = (BackgammonMove) move.clone();
			  BackgammonRules rulesCopy = new BackgammonRules();

			  boardCopy.move(moveCopy);
			  rulesCopy.calculatePossibilities(boardCopy, this);
			  
			  HashMap<BackgammonColumn, List<BackgammonColumn>> nextPossibilities = rulesCopy.getPossibilities();
			  
			  
			  Priority priority = null;
			  boolean existEnemyCheckerBefore = board.getColumns().stream().filter(c -> {
				  return c.getColor() != color && c.getNumber() > 0 && (c.getPosition() - column.getPosition()) * (color == Color.WHITE ? 1 : -1) > 0;
			  	}).count() == 0;
			  
			  if (existEnemyCheckerBefore && move.getTo().getNumber() == 1 && move.getTo().getColor() == color)
					  priority = new Priority(move, Priority.priorityLevelToWeight(PROTECT_CHECKER_AND_CREATE_WALL_PRIORITY_LEVEL));
			  else if (existEnemyCheckerBefore && move.getFrom().getNumber() == 1 && move.getTo().getNumber() > 0)
					  priority = new Priority(move, Priority.priorityLevelToWeight(PROTECT_CHECKER_PRIORITY_LEVEL));
			  else if (move.isEating())
				  priority = new Priority(move, Priority.priorityLevelToWeight(EAT_CHECKER_PRIORITY_LEVEL));
			  else if (move.getTo().getNumber() == 0 && nextPossibilities.keySet().stream().filter(nextSelectableColumn -> {
				return nextPossibilities.get(nextSelectableColumn).stream().filter(nextColumn -> {
					return nextColumn.getPosition() == column.getPosition();
				}).count() > 0;
			  }).count() > 0)
				  priority = new Priority(move, Priority.priorityLevelToWeight(CAN_BE_PROTECTED_PRIORITY_LEVEL));
			  else if (BackgammonBoard.POSITION_TO_SIDE_COLOR(column.getPosition()) != this.color)
				  priority = new Priority(move, Priority.priorityLevelToWeight(NOT_IN_BASE_CAMP_PRIORITY_LEVEL));
			  else
				  priority = new Priority(move, Priority.priorityLevelToWeight(LOW_PRIORITY_LEVEL));
			  
			  if (moveCopy.getFrom().getNumber() == 1 || moveCopy.getTo().getNumber() == 1)
				  priority.decreaseWeight(5);
			  
			  priorities.add(priority);
				  
		  });
	  });
	  DebugUtils.enableOutput();
	  
	  for (Priority priority : priorities)
		  System.out.println("Priority (" + priority.getWeight() + ") : " + priority.getMove().getFrom().getPosition() + " -> " + priority.getMove().getTo().getPosition());
	  
	  final Priority topPriority = priorities.stream().max((p1, p2) -> {
		  return p1.getWeight() - p2.getWeight();
	  }).get();
	  
	  // If multiple priorities of the same weight, take the farthest checker, or the nearest if it's to protect a checker.
	  List<Priority> sameWeightPriorities = priorities.stream().filter(priority -> priority != topPriority && priority.getWeight() == topPriority.getWeight()).collect(Collectors.toList());
	  if (sameWeightPriorities.size() > 0) {
		System.out.println("Multiple priorities with same weight found.");
        boolean isProtectCheckerPrioritylevel = Priority.weightToLevel(topPriority.getWeight()) >= PROTECT_CHECKER_PRIORITY_LEVEL;
        System.out.println("isProtectCheckerPrioritylevel : " + isProtectCheckerPrioritylevel);
        System.out.println("color : " + color);
		Comparator<? super Priority> comparator = isProtectCheckerPrioritylevel ? (p1, p2) -> p2.getMove().getFrom().getPosition() - p1.getMove().getFrom().getPosition() : (p1, p2) -> p1.getMove().getFrom().getPosition() - p2.getMove().getFrom().getPosition();
		return color == Color.WHITE ? sameWeightPriorities.stream().max(comparator).get().getMove() : sameWeightPriorities.stream().min(comparator).get().getMove();
	  }
	  
	  return topPriority.getMove();
	}

  public static void main(String[] args) {
	  List<Integer> list = new ArrayList<Integer>();
	  list.add(12);
	  list.add(21);
	  Comparator<Integer> comparator = (p1, p2) -> p1 - p2;
	  System.out.println(list.stream().min(comparator).get());
  }
}
