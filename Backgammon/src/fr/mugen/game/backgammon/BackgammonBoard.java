package fr.mugen.game.backgammon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.mugen.game.framework.Board;

public class BackgammonBoard implements Board {

  private final Map<Integer, BackgammonColumn> columns;
  private final Dice                           dice;

  public BackgammonBoard() {
    // for (int x = 0; x < this.columns.length; x++)
    // for (int y = 0; y < this.columns[x].length; y++)
    // this.columns[x][y] = new BackgammonColumn((x == 2 ? 3 : x == 3 ? 2 : x) *
    // 6 + y + 1);
    //
    // // Pawns initialization
    // this.columns[0][0].setNumber(2);
    // this.columns[0][5].setNumber(5);
    // this.columns[1][1].setNumber(3);
    // this.columns[1][5].setNumber(5);
    // this.columns[2][0].setNumber(2);
    // this.columns[2][5].setNumber(5);
    // this.columns[3][1].setNumber(3);
    // this.columns[3][5].setNumber(5);
    //
    // this.columns[0][0].setWhite(false);
    // this.columns[1][5].setWhite(false);
    // this.columns[2][5].setWhite(false);
    // this.columns[3][1].setWhite(false);
    this.columns = new HashMap<Integer, BackgammonColumn>();
    for (int i = 1; i <= 24; i++)
      this.columns.put(i, new BackgammonColumn(i));

    this.columns.get(1).setNumber(2);
    this.columns.get(1).setWhite(false);

    this.columns.get(6).setNumber(5);

    this.columns.get(8).setNumber(3);

    this.columns.get(12).setNumber(5);
    this.columns.get(12).setWhite(false);

    this.columns.get(13).setNumber(5);

    this.columns.get(17).setNumber(3);
    this.columns.get(17).setWhite(false);

    this.columns.get(19).setNumber(5);
    this.columns.get(19).setWhite(false);

    this.columns.get(24).setNumber(2);

    this.dice = new Dice();
  }

  public BackgammonColumn getColumn(final int position) {
    return this.columns.get(position);
  }

  public Collection<BackgammonColumn> getColumns() {
    return this.columns.values();
  }

  public void rollDice() {
    this.dice.roll();
  }

  public Dice getDice() {
    return this.dice;
  }

}
