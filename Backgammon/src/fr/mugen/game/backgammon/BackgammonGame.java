package fr.mugen.game.backgammon;

import java.util.Iterator;
import java.util.List;

import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Game;
import fr.mugen.game.framework.Player;
import fr.mugen.game.framework.Rules;

public class BackgammonGame extends Game {

  protected Iterator<Player> playersIterator;
  protected Player           currentPlayer;

  protected int              turn;

  public BackgammonGame(final Board board, final List<Player> players, final Rules rules, final Display display) {
    super(board, players, rules, display);
  }

  @Override
  public void start() {
    this.display.update(this);
    nextTurn();
  }

  @Override
  public boolean nextTurn() {
    final Player player = nextPlayer();
    player.play(this.board, this.rules, this.display);
    this.turn++;

    return false;
  }

  protected Player nextPlayer() {
    if (this.playersIterator == null || !this.playersIterator.hasNext())
      this.playersIterator = this.players.iterator();

    return this.currentPlayer = this.playersIterator.next();
  }

  public Player getCurrentPlayer() {
    return this.currentPlayer;
  }

  public int getPlayersDefaultPosition(final boolean white) {
    return ((BackgammonRules) this.rules).getPlayersDefaultPosition(this.board, white);
  }

  public int getNextPossiblePositionOnLeft(final int position, final boolean white) {
    return ((BackgammonRules) this.rules).getNextPossiblePositionOnLeft(this.board, position, white);
  }

  // @Override
  // protected void loop() {
  // for (final Player player : this.players) {
  // System.out.println((((BackgammonPlayer) player).isWhite() ? "White" :
  // "Black") + " player's turn.");
  //
  // player.play(this.board, this.rules);
  // ((BackgammonBoard) this.board).rollDice();
  // this.display.show(this.board);
  // }
  // this.turn++;
  // }

}
