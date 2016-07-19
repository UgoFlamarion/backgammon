package fr.mugen.game.backgammon.player;

import fr.mugen.game.backgammon.BackgammonBoard;
import fr.mugen.game.backgammon.Dice;
import fr.mugen.game.backgammon.BackgammonColumn.Color;
import fr.mugen.game.backgammon.display.JavaFXDisplay;
import fr.mugen.game.framework.Board;
import fr.mugen.game.framework.Controls;
import fr.mugen.game.framework.Display;
import fr.mugen.game.framework.Player;
import fr.mugen.game.framework.Rules;

public abstract class BackgammonPlayer extends Player {

  protected final Color color;
//  protected int         deads;

  public BackgammonPlayer(final Controls controls, final Color color) {
    super(controls);
    this.color = color;
//    this.deads = 0;
  }
  
  @Override
  public void play(final Board board, final Rules rules, final Display display) {
	  System.out.println("WHITE DEADS = " + ((BackgammonBoard) board).getColumn(BackgammonBoard.WHITE_CEMETERY_POSITION).getNumber());
	  System.out.println("BLACK DEADS = " + ((BackgammonBoard) board).getColumn(BackgammonBoard.BLACK_CEMETERY_POSITION).getNumber());
	  
    final Dice dice = ((BackgammonBoard) board).getDice();

    if (!dice.keepPlaying()) {
      dice.roll();
      ((JavaFXDisplay) display).playRollingDiceSound();
    }

    ((JavaFXDisplay) display).showDice(dice);

    final int cemeteryPosition = this.color == Color.WHITE ? BackgammonBoard.WHITE_CEMETERY_POSITION : BackgammonBoard.BLACK_CEMETERY_POSITION;
	if (((BackgammonBoard) board).getColumn(cemeteryPosition).getNumber() > 0) {
		System.out.println("YES : "+ cemeteryPosition);
    	((JavaFXDisplay) display).select(cemeteryPosition);
	}
    
    _play(board, rules, display);
  }
  
  protected abstract void _play(final Board board, final Rules rules, final Display display);

  public Color getColor() {
    return this.color;
  }

//  public int getDeads() {
//    return this.deads;
//  }
//
//  public void increaseDeads() {
//    this.deads++;
//  }
//
//  public void decreaseDeads() {
//    this.deads--;
//  }

}
