package fr.mugen.game.backgammon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Move;

public class BackgammonBoard implements Board {

  /*
   * Constants
   */

  public final static int WHITE_CEMETERY_POSITION = -1;
  public final static int BLACK_CEMETERY_POSITION = -2;
  
  private final Map<Integer, BackgammonColumn> columns;
  private final Dice                           dice;

  public BackgammonBoard() {
    this.columns = new HashMap<Integer, BackgammonColumn>();
    for (int i = 1; i <= 24; i++)
      this.columns.put(i, new BackgammonColumn(i));
    
    // Add cemeteries
    this.columns.put(WHITE_CEMETERY_POSITION, new BackgammonColumn(WHITE_CEMETERY_POSITION, Color.WHITE));
    this.columns.put(BLACK_CEMETERY_POSITION, new BackgammonColumn(BLACK_CEMETERY_POSITION, Color.BLACK));

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

  @Override
  public void move(final Move move) {
    final BackgammonColumn from = ((BackgammonMove) move).getFrom();
    final BackgammonColumn to = ((BackgammonMove) move).getTo();

    this.dice.consume(Math.abs(from.getPosition() - to.getPosition()));
    from.decreaseNumber();
	to.increaseNumber();

	// Send the dead checker to the cemetery
	if (((BackgammonMove) move).isEating()) {
		to.decreaseNumber();		
		getColumn(from.getColor() == Color.WHITE ? BLACK_CEMETERY_POSITION : WHITE_CEMETERY_POSITION).increaseNumber();
	}
	
    to.setColor(from.getColor());
    if (from.getNumber() == 0)
      from.setColor(Color.NONE);
  }

}
