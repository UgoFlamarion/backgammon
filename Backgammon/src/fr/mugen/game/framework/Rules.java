package fr.mugen.game.framework;

public interface Rules {

  public boolean check(Board board, Player player, Move move);
}
