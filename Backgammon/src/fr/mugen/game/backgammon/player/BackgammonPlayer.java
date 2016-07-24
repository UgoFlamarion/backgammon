package fr.mugen.game.backgammon.player;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Player;
import fr.mugen.game.framework.Rules;

public abstract class BackgammonPlayer extends Player {

  protected final Color color;

  public BackgammonPlayer(final Controls controls, final Color color) {
    super(controls);
    this.color = color;
  }

  @Override
  public void play(final Board board, final Rules rules, final Display display) {

    _play(board, rules, display);
  }

  protected abstract void _play(final Board board, final Rules rules, final Display display);

  public Color getColor() {
    return this.color;
  }

}
