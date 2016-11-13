package hchaoyidan.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hchaoyidan.engine.Application;
import hchaoyidan.engine.Screen;
import hchaoyidan.engine.highscore.HighScoreManager;
import hchaoyidan.engine.ui.Text;
import hchaoyidan.engine.ui.UIRectangle;
import hchaoyidan.engine.ui.UIShape;
import hchaoyidan.engine.ui.Viewport;
import starter.Vec2f;
import starter.Vec2i;

public class HighScoresScreen extends Screen {

	List<Text> highScores = new ArrayList<>();
	UIShape background;
	int drawCount = 0;

	public HighScoresScreen(Application game) {
		super(game);
		setup();
	}

	@Override
	public void setup() {
		background = new UIRectangle(new Color(181, 214, 255), new Vec2f(0, 0), null,
				new Vec2i(windowSize.x, windowSize.y));
		content.add(background);

		HighScoreManager hsm = new HighScoreManager();
		List<Integer> scores = hsm
				.readHighScore(Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + "highScores");
		for (int i = Math.min(scores.size() - 1, 4); i >= 0; i--) {
			float t1 = windowSize.x * 40 / 100;
			float t2 = windowSize.y * (5 * (4 - i)) / 100;
			Text score = new Text(Integer.toString(scores.get(i)), new Color(86, 142, 210), new Vec2f(t1, t2),
					background, new Vec2i(100, 100));
			score.setFamily("Andale Mono");
			content.add(score);
			highScores.add(score);
		}
	}

	@Override
	public void onTick(long nanosSincePreviousTick) {
	}

	@Override
	public void onDraw(Graphics2D g) {
		if (drawCount == 0) {
			g.setColor(new Color(181, 214, 255));
			g.fillRect(0, 0, windowSize.x, windowSize.y);
		}

		for (Text text : highScores) {
			text.onDraw(g);
		}
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
	}

	@Override
	public void onMouseClicked(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());

		// if(view.isWithin(mouse)) {
		// world.onMouseClicked(e);
		// }
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
