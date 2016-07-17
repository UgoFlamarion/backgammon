package fr.mugen.game.framework;

import java.util.List;

public abstract class Game {
  protected Board         board;
  protected List<Player>  players;
  protected Rules         rules;
  protected final Display display;
  protected boolean       gameIsOver;

  public Game(final Board board, final List<Player> players, final Rules rules, final Display display) {
    this.board = board;
    this.players = players;
    this.rules = rules;
    this.display = display;
  }

  public abstract void start();

  public abstract boolean nextTurn();

  public Board getBoard() {
    return this.board;
  }

  public List<Player> getPlayers() {
    return this.players;
  }

  public Rules getRules() {
    return this.rules;
  }

  public boolean isGameIsOver() {
    return this.gameIsOver;
  }

  // private void _loop() {
  // while (!gameIsEnded) {
  // loop();
  // }
  // }

  // protected abstract void loop();

}
