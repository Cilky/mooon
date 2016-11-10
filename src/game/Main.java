package game;

import engine.Application;

/**
 * The Main that starts it all
 * @author yidanzeng
 *
 */
public class Main {

	public static void main(String[] args) {
		Application game = new Application("M2Retry", false);
		StartScreen screen = new StartScreen(game);
		game.setScreen(screen);
		game.startup();
	}

}
