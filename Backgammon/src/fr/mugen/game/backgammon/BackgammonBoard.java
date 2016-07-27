package fr.mugen.game.backgammon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Move;

public class BackgammonBoard implements Board, Cloneable {

  /*
   * Constants
   */

  private static final int                     BACKGAMMON_BOARD_SIZE   = 24;
  private static final int                     TOTAL_CHECKERS          = 15;
  public final static int                      WHITE_CEMETERY_POSITION = 25;
  public final static int                      BLACK_CEMETERY_POSITION = 0;
  public final static int                      WHITE_HEAVEN_POSITION   = -1;
  public final static int                      BLACK_HEAVEN_POSITION   = 26;

  /*
   * Properties
   */

  private final Map<Integer, BackgammonColumn> columns;
  private final Dice                           dice;

  /*
   * Static methods
   */

  public static boolean IS_CEMETERY(final int position) {
    return (position == BackgammonBoard.WHITE_CEMETERY_POSITION) || (position == BackgammonBoard.BLACK_CEMETERY_POSITION);
  }

  public static int COLOR_TO_CEMETERY_POSITION(final Color color) {
    return color == Color.WHITE ? BackgammonBoard.WHITE_CEMETERY_POSITION : BackgammonBoard.BLACK_CEMETERY_POSITION;
  }

  public static boolean IS_HEAVEN(final int position) {
    return (position == BackgammonBoard.WHITE_HEAVEN_POSITION) || (position == BackgammonBoard.BLACK_HEAVEN_POSITION);
  }

  public static int COLOR_TO_HEAVEN_POSITION(final Color color) {
    return color == Color.WHITE ? BackgammonBoard.WHITE_HEAVEN_POSITION : BackgammonBoard.BLACK_HEAVEN_POSITION;
  }

  public static Color POSITION_TO_SIDE_COLOR(final int position) {
    final int ratio = (position - 1) / 6;
    return ratio == 0 ? Color.WHITE : (ratio == 3 ? Color.BLACK : null);
  }

  public BackgammonBoard() {
    this.columns = new HashMap<Integer, BackgammonColumn>();
    for (int i = 1; i <= BackgammonBoard.BACKGAMMON_BOARD_SIZE; i++)
      this.columns.put(i, new BackgammonColumn(i));

    // Add cemeteries
    this.columns.put(BackgammonBoard.WHITE_CEMETERY_POSITION, new BackgammonColumn(BackgammonBoard.WHITE_CEMETERY_POSITION, Color.WHITE));
    this.columns.put(BackgammonBoard.BLACK_CEMETERY_POSITION, new BackgammonColumn(BackgammonBoard.BLACK_CEMETERY_POSITION, Color.BLACK));

    // Add heavens
    this.columns.put(BackgammonBoard.WHITE_HEAVEN_POSITION, new BackgammonColumn(BackgammonBoard.WHITE_HEAVEN_POSITION, Color.WHITE));
    this.columns.put(BackgammonBoard.BLACK_HEAVEN_POSITION, new BackgammonColumn(BackgammonBoard.BLACK_HEAVEN_POSITION, Color.BLACK));

    // Initialize positions
    this.columns.get(1).setNumber(2);
    this.columns.get(1).setColor(Color.BLACK);

    this.columns.get(6).setNumber(5);
    this.columns.get(6).setColor(Color.WHITE);

    this.columns.get(8).setNumber(3);
    this.columns.get(8).setColor(Color.WHITE);

    this.columns.get(12).setNumber(5);
    this.columns.get(12).setColor(Color.BLACK);

    this.columns.get(13).setNumber(5);
    this.columns.get(13).setColor(Color.WHITE);

    this.columns.get(17).setNumber(3);
    this.columns.get(17).setColor(Color.BLACK);

    this.columns.get(19).setNumber(5);
    this.columns.get(19).setColor(Color.BLACK);

    this.columns.get(24).setNumber(2);
    this.columns.get(24).setColor(Color.WHITE);

    // TEST

    // this.columns.get(0).setNumber(1);
    // this.columns.get(0).setColor(Color.BLACK);
    //
    // this.columns.get(1).setNumber(4);
    // this.columns.get(1).setColor(Color.WHITE);
    // this.columns.get(2).setNumber(4);
    // this.columns.get(2).setColor(Color.WHITE);
    // this.columns.get(3).setNumber(4);
    // this.columns.get(3).setColor(Color.WHITE);
    // this.columns.get(4).setNumber(4);
    // this.columns.get(4).setColor(Color.WHITE);
    // this.columns.get(5).setNumber(4);
    // this.columns.get(5).setColor(Color.WHITE);
    // this.columns.get(6).setNumber(4);
    // this.columns.get(6).setColor(Color.WHITE);
    // this.columns.get(16).setNumber(2);
    //
    // this.columns.get(16).setColor(Color.BLACK);
    // this.columns.get(20).setNumber(4);
    // this.columns.get(20).setColor(Color.BLACK);

    // orderColumns();

    this.dice = new Dice();
  }

  // private void orderColumns() {
  // this.columns = (Map<Integer, BackgammonColumn>)
  // this.columns.entrySet().stream()
  // .sorted((entry1, entry2) -> entry1.getKey() - entry2.getKey())
  // .collect(Collectors.toMap(entry -> ((Map.Entry<Integer, BackgammonColumn>)
  // entry).getKey(), entry -> ((Map.Entry<Integer, BackgammonColumn>)
  // entry).getValue()));
  // }

  public BackgammonColumn getColumn(final int position) {
    return this.columns.get(position);
  }

  public Collection<BackgammonColumn> getColumns() {
    return this.columns.values();
  }

  public Dice getDice() {
    return this.dice;
  }

  @Override
  public void move(final Move move) {
    final BackgammonMove backgammonMove = (BackgammonMove) move;
    final BackgammonColumn from = backgammonMove.getFrom();
    final BackgammonColumn to = backgammonMove.getTo();

    this.dice.consume(backgammonMove.getMoveLength(), BackgammonBoard.IS_HEAVEN(to.getPosition()));
    from.decreaseNumber();
    to.increaseNumber();

    // Send the dead checker to the cemetery
    if (backgammonMove.isEating()) {
      to.decreaseNumber();
      getColumn(BackgammonBoard.COLOR_TO_CEMETERY_POSITION(to.getColor())).increaseNumber();
    }

    to.setColor(from.getColor());
    if ((from.getNumber() == 0) && !BackgammonBoard.IS_CEMETERY(from.getPosition()))
      from.setColor(Color.NONE);
  }

  public boolean doesPlayerWin(final Color color) {
    return this.columns.get(BackgammonBoard.COLOR_TO_HEAVEN_POSITION(color)).getNumber() == BackgammonBoard.TOTAL_CHECKERS;
  }

  @Override
  public Object clone() {
    final BackgammonBoard backgammonBoard = new BackgammonBoard();

    this.columns.forEach((position, column) -> {
      backgammonBoard.getColumn(position).setColor(column.getColor());
      backgammonBoard.getColumn(position).setNumber(column.getNumber());
    });

    backgammonBoard.getDice().setDice(this.dice.getDice1(), this.dice.getDice2());

    return backgammonBoard;
  }

}
