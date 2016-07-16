package fr.mugen.game.backgammon;

import java.util.Scanner;

import fr.mugen.game.framework.Controls;

public class KeyboardControls implements Controls {

	public Object getInput() {
		Scanner s = new Scanner(System.in);
		String input = null;
		
		while (!checkInput(input))
			input = s.nextLine();
		
		return input;
	}

	private boolean checkInput(String input) {
		return input != null && input.matches("^\\d\\d-\\d\\d$");
	}

}
