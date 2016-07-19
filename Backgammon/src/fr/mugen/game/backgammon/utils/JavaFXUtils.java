package fr.mugen.game.backgammon.utils;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.display.JavaFXDisplay;

public class JavaFXUtils {

  public static int getCheckerX(final int position) {
    final int x = JavaFXDisplay.BORDERX + (position - 1) % 12 * JavaFXDisplay.GAPX
        + (position % 12 / 7 == 1 || position % 12 == 0 ? JavaFXDisplay.MIDDLE_GAPX : 0);
    return (int) (position <= 12 ? x : JavaFXDisplay.WIDTH - x - JavaFXDisplay.CHECKER_IMAGE_SIZE);
  }

  public static int getCheckerY(final int position, final int number) {
    return JavaFXDisplay.BORDERY + position / 13 * JavaFXDisplay.GAPY + number * JavaFXDisplay.CHECKER_GAPY * (position > 12 ? -1 : 1);
  }

  public static double getCursorX(final int position) {
	if (position < 0)
	  return (JavaFXDisplay.WIDTH / 2) - (JavaFXDisplay.CHECKER_IMAGE_SIZE / 2);
    return JavaFXUtils.getCheckerX(position);
  }

  public static int getCursorY(final int position) {
    if (position < 0)
	  return (int) ((JavaFXDisplay.HEIGHT / 2) - (position == BackgammonBoard.WHITE_CEMETERY_POSITION ? JavaFXDisplay.CEMETERY_GAPY : -JavaFXDisplay.CEMETERY_GAPY) * 3);
    return position <= 12 ? JavaFXDisplay.CURSORY_LINE1 : JavaFXDisplay.CURSORY_LINE2;
  }

  public static double getScaleY(final int position) {
	if (position < 0)
	  return (position == BackgammonBoard.WHITE_CEMETERY_POSITION ? -1 : 1);
    return position <= 12 ? 1 : -1;
  }

}
