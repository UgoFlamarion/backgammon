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

public class MediumComputerPlayer extends ComputerPlayer {

  private static final int LOW_PRIORITY_LEVEL = 1;
private static final int NOT_IN_BASE_CAMP_PRIORITY_LEVEL = 3;
private static final int CAN_BE_PROTECTED_PRIORITY_LEVEL = 8;
private static final int EAT_CHECKER_PRIORITY_LEVEL = 9;
private static final int PROTECT_CHECKER_PRIORITY_LEVEL = 10;

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
	  
	  possibilities.forEach((selectableColumn, columns) -> {
		  columns.forEach(column -> {
			  if (selectableColumn == column)
				  return;
			  
			  BackgammonMove move = new BackgammonMove(selectableColumn, column);
			  
			  BackgammonBoard boardCopy = (BackgammonBoard) board.clone();
			  boardCopy.move((BackgammonMove) move.clone());
			  
			  BackgammonRules rulesCopy = new BackgammonRules();
			  rulesCopy.calculatePossibilities(boardCopy, this);
			  HashMap<BackgammonColumn, List<BackgammonColumn>> nextPossibilities = rulesCopy.getPossibilities();
			  
			  if ((move.getTo().getNumber() == 1 && move.getTo().getColor() == color)
				|| move.getFrom().getNumber() == 1 && move.getTo().getNumber() > 0)
				  priorities.add(new Priority(move, Priority.priorityLevelToWeight(PROTECT_CHECKER_PRIORITY_LEVEL)));
			  else if (move.isEating())
				  priorities.add(new Priority(move, Priority.priorityLevelToWeight(EAT_CHECKER_PRIORITY_LEVEL)));
			  else if (nextPossibilities.keySet().stream().filter(nextSelectableColumn -> {
				return nextPossibilities.get(nextSelectableColumn).stream().filter(nextColumn -> {
					return nextColumn.equals(column);
				}).count() > 0;
			  }).count() > 0)
				  priorities.add(new Priority(move, Priority.priorityLevelToWeight(CAN_BE_PROTECTED_PRIORITY_LEVEL)));
			  else if (BackgammonBoard.POSITION_TO_SIDE_COLOR(column.getPosition()) != this.color)
				  priorities.add(new Priority(move, Priority.priorityLevelToWeight(NOT_IN_BASE_CAMP_PRIORITY_LEVEL)));
			  else
				  priorities.add(new Priority(move, Priority.priorityLevelToWeight(LOW_PRIORITY_LEVEL)));
				  
		  });
	  });
	  
	  for (Priority priority : priorities)
		  System.out.println("Priority (" + priority.getWeight() + ") : " + priority.getMove().getFrom().getPosition() + " -> " + priority.getMove().getTo().getPosition());
	  
	  final Priority topPriority = priorities.stream().max((p1, p2) -> {
		  return p1.getWeight() - p2.getWeight();
	  }).get();
	  
	  // If multiple priorities of the same level, take the farthest checker, or the nearest if it's to protect a checker.
	  List<Priority> sameLevelPriorities = priorities.stream().filter(priority -> priority != topPriority && Priority.weightToLevel(priority.getWeight()) == Priority.weightToLevel(topPriority.getWeight())).collect(Collectors.toList());
	  if (sameLevelPriorities.size() > 0) {
		  boolean isProtectCheckerPrioritylevel = Priority.weightToLevel(topPriority.getWeight()) == PROTECT_CHECKER_PRIORITY_LEVEL;
		Comparator<? super Priority> comparator = isProtectCheckerPrioritylevel ? (p1, p2) -> p2.getMove().getFrom().getPosition() - p1.getMove().getFrom().getPosition() : (p1, p2) -> p1.getMove().getFrom().getPosition() - p2.getMove().getFrom().getPosition();
		return color == Color.WHITE ? sameLevelPriorities.stream().max(comparator).get().getMove() : sameLevelPriorities.stream().min(comparator).get().getMove();
	  }
	  
	  return topPriority.getMove();
	}

//  public static void main(String[] args) {
//	  List<Integer> list = new ArrayList();
//	  list.add(17);
//	  list.add(19);
//	  Comparator<Integer> comparator = (p1, p2) -> p1 - p2;
//	  System.out.println(list.stream().min(comparator).get());
//  }
}
