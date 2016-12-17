package hchaoyidan.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import hchaoyidan.engine.Application;
import hchaoyidan.engine.Screen;
import hchaoyidan.engine.persistence.Persistence;
import hchaoyidan.engine.sound.SoundPlayer;
import hchaoyidan.engine.ui.Text;
import hchaoyidan.engine.ui.UIRectangle;
import hchaoyidan.engine.ui.UIShape;
import hchaoyidan.engine.ui.Viewport;
import starter.Vec2f;
import starter.Vec2i;

/**
 * Default launch screen for Tou
 * 
 * @author yidanzeng
 *
 */
public class StartScreen extends Screen {

	Text start;
	UIShape background;
	Persistence p;
	private int startAlpha = 255;
	Screen screenToSet = null;

	/**
	 * Instantiates the StartScreen
	 * 
	 * @param game
	 */
	public StartScreen(Application game) {
		super(game);
		setup();
	}

	/**
	 * Initiates all the screen elements
	 */
	@Override
	public void setup() {
		
		p = new Persistence();
		
		// background
		background = new UIRectangle(new Color(181, 214, 255, startAlpha), new Vec2f(0, 0), null,
				new Vec2i(windowSize.x, windowSize.y));
		content.add(background);

		// text TODO: fix the height thing for text
//		Text debug = new Text("Debug", new Color(215, 229, 245),
//				new Vec2f(windowSize.x / 2 - 100, windowSize.y / 4 * 2 - 100), background, new Vec2i(200, 100));
//		debug.setBackground(new Color(86, 142, 210));
//		debug.type = "debugger";
//		debug.setClickable();
//		debug.setFontSize(10);
//		debug.setFamily("Andale Mono");
//
//		content.add((UIShape) debug);

		start = new Text("Start Game", new Color(215, 229, 245, startAlpha),
				new Vec2f(windowSize.x / 2 - 100, windowSize.y / 4 * 1 - 100), background, new Vec2i(200, 100));
		start.setBackground(new Color(86, 142, 210, startAlpha));
		start.type = "game";
		start.setClickable();
		start.setFontSize(10);
		start.setFamily("Andale Mono");

		content.add((UIShape) start);

		Text scores = new Text("Scores", new Color(215, 229, 245, startAlpha),
				new Vec2f(windowSize.x / 2 - 100, windowSize.y / 4 * 2 - 100), background, new Vec2i(200, 100));
		scores.setBackground(new Color(86, 142, 210, startAlpha));
		scores.type = "scores";
		scores.setClickable();
		scores.setFamily("Andale Mono");
		
		content.add((UIShape) scores);
		
		Text saved = new Text("Continue Last Game", new Color(215, 229, 245, startAlpha),
				new Vec2f(windowSize.x / 2 - 100, windowSize.y / 4 * 3 - 100), background, new Vec2i(200, 100));
		saved.setBackground(new Color(86, 142, 210, startAlpha));
		saved.type = "continue";
		saved.setClickable();
		saved.setFamily("Andale Mono");
		
		content.add((UIShape) saved);

		background.setStartAlpha(startAlpha);
		start.setStartAlpha(startAlpha);
		scores.setStartAlpha(startAlpha);
		saved.setStartAlpha(startAlpha);
	}

	/**
	 * If the start button is pressed, a new GameScreen is created and
	 * initialized
	 */

	@Override
	public void onMouseClicked(MouseEvent e) {

		for (UIShape s : content) {
			if (s.clickable) {
				
				Vec2i mouse = new Vec2i(e.getX(), e.getY());
				if (s.isWithin(mouse)) {
					setFade(true);
					setLerpVelocity(new Vec2f(0, 20));
					setLerp(true);
				}
				if (s.isWithin(mouse) && s.type.equals("debugger")) {
//					DebugScreen debug = new DebugScreen(game);
//					game.setScreen(debug);
				} else if (s.isWithin(mouse) && s.type.equals("game")) {
					screenToSet = new MScreen(game);
				} else if (s.isWithin(mouse) && s.type.equals("scores")) {
					screenToSet = new HighScoresScreen(game);
				} else if (s.isWithin(mouse) && s.type.equals("continue")) {
					
					Screen screen = (Screen) p.loadScreen(Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + "resources"
							+ File.separator + "screen");
					
					SoundPlayer sound = null;
					if (((MWorld)screen.getWorld()).getLm().getLevel() == 1) {
						sound = new SoundPlayer(new File("sounds/ambient.wav"), true);
					} else if (((MWorld)screen.getWorld()).getLm().getLevel() == 2){
						sound = new SoundPlayer(new File("sounds/wind.wav"), true);
					} else if (((MWorld)screen.getWorld()).getLm().getLevel() == 3) {
						sound = new SoundPlayer(new File("sounds/space.wav"), true);
					}
					//((MWorld)screen.getWorld()).getPlayer().setSound(new SoundPlayer(new File("fishHit/hit.wav"), false));
					((MWorld)screen.getWorld()).setGameSound(sound);
					((MWorld)screen.getWorld()).getGameSound().run();
					((MWorld)screen.getWorld()).debrisSound = new SoundPlayer(new File("sounds/debris.wav"), false);
					((MWorld)screen.getWorld()).player.setBirdHit(new SoundPlayer(new File("sounds/hit.wav"), false));
					((MWorld)screen.getWorld()).player.setFishHit(new SoundPlayer(new File("sounds/smallbell.wav"), false));
					((MWorld)screen.getWorld()).player.setStarHit(new SoundPlayer(new File("sounds/starhit.wav"), false));
					screenToSet = screen;
					
				}
			}
		}
	}

	@Override
	public void onTick(long nanosSincePreviousTick) {
		super.onTick(nanosSincePreviousTick);
		if (finishFade) {
			game.setScreen(screenToSet);
		}
	}

	public void onDraw(Graphics2D g) {
		g.setColor(new Color(15, 0, 80));
		g.fillRect(0, 0, windowSize.x, windowSize.y);
		super.onDraw(g);
	}
	
	@Override
	public void onKeyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyPressed(KeyEvent e) {
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

}
