package hchaoyidan.game;

import hchaoyidan.engine.Application;

/**
 * The Main that starts it all
 * @author yidanzeng
 *
 */
public class Main {

	public static void main(String[] args) {
		Application game = new Application("Moon", false);
		StartScreen screen = new StartScreen(game);
		game.setScreen(screen);
		game.startup();
	}

}
