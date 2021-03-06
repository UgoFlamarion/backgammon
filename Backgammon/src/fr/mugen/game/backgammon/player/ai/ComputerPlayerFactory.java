package fr.mugen.game.backgammon.player.ai;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.framework.Player;

public class ComputerPlayerFactory {

  public enum Difficulty {
    EASY,
    MEDIUM,
    HARD
  }

  public static Player createComputerPlayer(final Color color, final Difficulty difficulty) {
    switch (difficulty) {
      case EASY:
        return new EasyComputerPlayer(color);
      case MEDIUM:
        return new MediumComputerPlayer(color);
      case HARD:
        return new MediumComputerPlayer(color);
    }
    return null;
  }

}
