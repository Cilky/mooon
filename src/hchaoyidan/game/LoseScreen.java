package hchaoyidan.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import hchaoyidan.engine.Screen;

import hchaoyidan.engine.Application;
import hchaoyidan.engine.Screen;
import hchaoyidan.engine.highscore.HighScoreManager;
import hchaoyidan.engine.ui.Text;
import hchaoyidan.engine.ui.UIRectangle;
import hchaoyidan.engine.ui.UIShape;
import starter.Vec2f;
import starter.Vec2i;

public class LoseScreen extends Screen {
	List<Text> highScores = new ArrayList<>();
	UIShape background;
	int drawCount = 0;
	Screen screenToSet;
	Text title;

	public LoseScreen(Application game) {
		super(game);
		setup();
	}

	@Override
	public void setup() {
		background = new UIRectangle(new Color(0, 0, 0), new Vec2f(0, 0), null,
				new Vec2i(windowSize.x, windowSize.y));
		content.add(background);
		
		Text win = new Text("YOU LOSE...", new Color(215, 229, 245),
				new Vec2f(windowSize.x / 2 - 100, windowSize.y / 4 * 1 - 100), background, new Vec2i(200, 100));
		win.setBackground(new Color(86, 142, 210));
		win.type = "game";
		win.setClickable();
		win.setFontSize(10);
		win.setFamily("Andale Mono");

		content.add((UIShape) win);
	}

	@Override
	public void onTick(long nanosSincePreviousTick) {
		super.onTick(nanosSincePreviousTick);
		if (finishFade) {

			game.setScreen(screenToSet);
		}
	}

	@Override
	public void onDraw(Graphics2D g) {
		if (drawCount == 0) {
			g.setColor(new Color(181, 214, 255));
			g.fillRect(0, 0, windowSize.x, windowSize.y);
		}
		super.onDraw(g);
		for (Text text : highScores) {
			text.onDraw(g);
		}
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		if(e.getKeyChar() == "r".charAt(0)) {
		StartScreen screen = new StartScreen(game);
		game.setScreen(screen);
		}
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
	}

	@Override
	public void onMouseClicked(MouseEvent e) {
	}

	@Override
	public void onMousePressed(MouseEvent e) {
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
	}
}
