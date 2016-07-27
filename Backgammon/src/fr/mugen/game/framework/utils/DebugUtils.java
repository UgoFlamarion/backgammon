package fr.mugen.game.framework.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class DebugUtils {

  private static PrintStream out;

  public static void disableOutput() {
    DebugUtils.out = System.out;
    System.setOut(new PrintStream(new OutputStream() {
      @Override
      public void write(final int arg0) throws IOException {
      }
    }));
  }

  public static void enableOutput() {
    System.setOut(DebugUtils.out);
  }

}
