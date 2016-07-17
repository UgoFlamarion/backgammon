package fr.mugen.game.framework;

public abstract class Player {

  protected Controls controls;

  public Player(final Controls controls) {
    this.controls = controls;
  }

  public abstract void play(Board board, Rules rules, Display display);

}
