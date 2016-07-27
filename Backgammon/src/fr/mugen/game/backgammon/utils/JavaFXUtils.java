package fr.mugen.game.backgammon.utils;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.BackgammonRules;
import fr.mugen.game.backgammon.display.BackgammonBoardDisplay;
import fr.mugen.game.backgammon.display.JavaFXDisplay;

public class JavaFXUtils {

  public static int getCheckerX(final int position) {
    final int x = BackgammonBoardDisplay.BORDERX
        + (((position - 1) % 12) * BackgammonBoardDisplay.GAPX)
        + ((((position % 12) / 7) == 1) || ((position % 12) == 0) ? BackgammonBoardDisplay.MIDDLE_GAPX : 0);
    return (int) (position <= 12 ? x : JavaFXDisplay.WIDTH - x - BackgammonBoardDisplay.CHECKER_IMAGE_SIZE);
  }

  public static int getCheckerY(final int position, final int number) {
    return BackgammonBoardDisplay.BORDERY
        + ((position / 13) * BackgammonBoardDisplay.GAPY)
        + (number * BackgammonBoardDisplay.CHECKER_GAPY * (position > 12 ? -1 : 1));
  }
  
  public static int getNumberX(final int position) {
	return getCheckerX(position) + BackgammonBoardDisplay.NUMBER_ADDITIONNAL_GAPX;
  }
  
  public static int getNumberY(final int position) {
	    return BackgammonBoardDisplay.BORDERY
	        + ((position / 13) * BackgammonBoardDisplay.NUMBER_GAPY);
  }

  public static double getCursorX(final int position) {
    if (BackgammonBoard.IS_CEMETERY(position))
      return (JavaFXDisplay.WIDTH / 2) - (BackgammonBoardDisplay.CURSOR_IMAGE_SIZE / 2);
    else if (BackgammonBoard.IS_HEAVEN(position))
      return BackgammonBoardDisplay.BORDERX;

    final int x = BackgammonBoardDisplay.BORDERX
        + (((position - 1) % 12) * BackgammonBoardDisplay.GAPX // +
    )
        + ((((position % 12) / 7) == 1) || ((position % 12) == 0) ? BackgammonBoardDisplay.MIDDLE_GAPX : 0);
    return (int) (position <= 12 ? x + (BackgammonBoardDisplay.CURSOR_IMAGE_SIZE / 2)
        : JavaFXDisplay.WIDTH - x - (BackgammonBoardDisplay.GAPX / 2) - (BackgammonBoardDisplay.CURSOR_IMAGE_SIZE / 2));
  }

  public static int getCursorY(final int position) {
    if (BackgammonBoard.IS_CEMETERY(position))
      return (int) (position == BackgammonBoard.WHITE_CEMETERY_POSITION ? BackgammonBoardDisplay.CEMETERY_LINE1 : BackgammonBoardDisplay.CEMETERY_LINE2);
    else if (BackgammonBoard.IS_HEAVEN(position))
      return (int) (JavaFXDisplay.HEIGHT / 2) - (BackgammonBoardDisplay.CURSOR_IMAGE_SIZE / 2);

    return position <= 12 ? BackgammonBoardDisplay.CURSORY_LINE1 : BackgammonBoardDisplay.CURSORY_LINE2;
  }

  public static double getScaleY(final int position) {
    if (BackgammonBoard.IS_CEMETERY(position))
      return position == BackgammonBoard.WHITE_CEMETERY_POSITION ? -1 : 1;
    return position <= 12 ? 1 : -1;
  }

  public static double getRotate(final int position) {
    return BackgammonBoard.IS_HEAVEN(position) ? -BackgammonRules.getSideFactor(position) * 90 : 0;
  }

}
