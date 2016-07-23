package fr.mugen.game.backgammon;

import java.util.Iterator;
import java.util.List;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.display.JavaFXDisplay;
import fr.mugen.game.backgammon.player.BackgammonPlayer;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Game;
import fr.mugen.game.framework.Player;
import fr.mugen.game.framework.Rules;

public class BackgammonGame extends Game {

  /*
   * Constants
   */

  public final static int    DEFAULT_CURSOR_POSITION  = -2;
  public final static String NO_POSSIBILITIES_MESSAGE = "Aucun coup possible, passez votre tour.";

  protected Iterator<Player> playersIterator;
  protected BackgammonPlayer currentPlayer;

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
    System.out.println("\n###\n### Player " + ((BackgammonPlayer) player).getColor() + "'s turn. ###\n###\n");
    player.play(this.board, this.rules, this.display);
    this.turn++;

    final boolean existPossibilities = calculatePossibilities(BackgammonGame.DEFAULT_CURSOR_POSITION);

    initializeDisplay();
    this.display.update(this);

    if (!existPossibilities)
      ((JavaFXDisplay) this.display).showMessage(BackgammonGame.NO_POSSIBILITIES_MESSAGE);

    return true;
  }

  public void forceNextTurn() {
    ((BackgammonBoard) getBoard()).getDice().consumeAll();
    nextTurn();
  }

  protected Player nextPlayer() {
    if (((BackgammonBoard) this.board).getDice().keepPlaying())
      return this.currentPlayer;

    if (this.playersIterator == null || !this.playersIterator.hasNext())
      this.playersIterator = this.players.iterator();

    return this.currentPlayer = (BackgammonPlayer) this.playersIterator.next();
  }

  public void initializeDisplay() {
    ((JavaFXDisplay) this.display).initCursorSelectedPosition();

    final int cemeteryPosition = this.currentPlayer.getColor() == Color.WHITE ? BackgammonBoard.WHITE_CEMETERY_POSITION
        : BackgammonBoard.BLACK_CEMETERY_POSITION;
    if (((BackgammonBoard) this.board).getColumn(cemeteryPosition).getNumber() > 0)
      ((JavaFXDisplay) this.display).select(cemeteryPosition);
  }

  public void move(final int cursorSelectedPosition, final int cursorPosition) {
    final BackgammonColumn from = ((BackgammonBoard) this.board).getColumn(cursorSelectedPosition);
    final BackgammonColumn to = ((BackgammonBoard) this.board).getColumn(cursorPosition);
    final BackgammonMove move = ((BackgammonRules) this.rules).initializeMove(from, to);

    this.board.move(move);
    // this.display.update(this);

    nextTurn();
  }

  public boolean calculatePossibilities(final int selectedCheckerPosition) {
    return ((BackgammonRules) this.rules).calculatePossibilities((BackgammonBoard) this.board, this.currentPlayer);
  }

  public int getCursorDefaultPosition(final int selectedCheckerPosition) {
    return ((BackgammonRules) this.rules).getCursorDefaultPosition((BackgammonBoard) this.board, this.currentPlayer,
        ((BackgammonBoard) this.board).getColumn(selectedCheckerPosition));
  }

  public int getNextPossiblePositionOnLeft(final int currentPosition, final int selectedCheckerPosition) {
    return ((BackgammonRules) this.rules).getNextPossiblePositionOnLeft((BackgammonBoard) this.board, this.currentPlayer,
        ((BackgammonBoard) this.board).getColumn(currentPosition), ((BackgammonBoard) this.board).getColumn(selectedCheckerPosition));
  }

  public int getNextPossiblePositionOnRight(final int currentPosition, final int selectedCheckerPosition) {
    return ((BackgammonRules) this.rules).getNextPossiblePositionOnRight((BackgammonBoard) this.board, this.currentPlayer,
        ((BackgammonBoard) this.board).getColumn(currentPosition), ((BackgammonBoard) this.board).getColumn(selectedCheckerPosition));
  }

  public int getNextPossiblePositionUpward(final int currentPosition, final int selectedCheckerPosition) {
    return ((BackgammonRules) this.rules).getNextPossiblePositionUpward((BackgammonBoard) this.board, this.currentPlayer,
        ((BackgammonBoard) this.board).getColumn(currentPosition), ((BackgammonBoard) this.board).getColumn(selectedCheckerPosition));
  }

  public int getNextPossiblePositionDownward(final int currentPosition, final int selectedCheckerPosition) {
    return ((BackgammonRules) this.rules).getNextPossiblePositionDownward((BackgammonBoard) this.board, this.currentPlayer,
        ((BackgammonBoard) this.board).getColumn(currentPosition), ((BackgammonBoard) this.board).getColumn(selectedCheckerPosition));
  }

  /*
   * Getters
   */

  public Player getCurrentPlayer() {
    return this.currentPlayer;
  }

}
