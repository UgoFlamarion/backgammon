package fr.mugen.game.backgammon;

import java.util.Iterator;
import java.util.List;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
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
    if (((BackgammonBoard) this.board).getDice().keepPlaying())
      return this.currentPlayer;

    if (this.playersIterator == null || !this.playersIterator.hasNext())
      this.playersIterator = this.players.iterator();

    return this.currentPlayer = this.playersIterator.next();
  }

  public Player getCurrentPlayer() {
    return this.currentPlayer;
  }

  public int getPlayersDefaultPosition(final Color color) {
    return ((BackgammonRules) this.rules).getPlayersDefaultPosition(this.board, color);
  }

  public int getNextPossiblePositionOnLeft(final int currentPosition, final Color color, final int selectedCheckerPosition) {
    return ((BackgammonRules) this.rules).getNextPossiblePositionOnLeft(this.board, this.currentPlayer,
        ((BackgammonBoard) this.board).getColumn(currentPosition), ((BackgammonBoard) this.board).getColumn(selectedCheckerPosition));
  }

  public int getNextPossiblePositionOnRight(final int currentPosition, final Color color, final int selectedCheckerPosition) {
    return ((BackgammonRules) this.rules).getNextPossiblePositionOnRight(this.board, this.currentPlayer,
        ((BackgammonBoard) this.board).getColumn(currentPosition), ((BackgammonBoard) this.board).getColumn(selectedCheckerPosition));
  }

  public int getNextPossiblePositionUpward(final int currentPosition, final Color color, final int selectedCheckerPosition) {
    return ((BackgammonRules) this.rules).getNextPossiblePositionUpward(this.board, this.currentPlayer,
        ((BackgammonBoard) this.board).getColumn(currentPosition), ((BackgammonBoard) this.board).getColumn(selectedCheckerPosition));
  }

  public int getNextPossiblePositionDownward(final int currentPosition, final Color color, final int selectedCheckerPosition) {
    return ((BackgammonRules) this.rules).getNextPossiblePositionDownward(this.board, this.currentPlayer,
        ((BackgammonBoard) this.board).getColumn(currentPosition), ((BackgammonBoard) this.board).getColumn(selectedCheckerPosition));
  }

  public void move(final int cursorSelectedPosition, final int cursorPosition) {
    this.board.move(new BackgammonMove(((BackgammonBoard) this.board).getColumn(cursorSelectedPosition),
        ((BackgammonBoard) this.board).getColumn(cursorPosition)));
    this.display.update(this);
    nextTurn();
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
