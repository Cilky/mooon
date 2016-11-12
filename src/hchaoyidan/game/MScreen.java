package hchaoyidan.game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import hchaoyidan.engine.Application;
import hchaoyidan.engine.Screen;
import hchaoyidan.engine.ui.Text;
import hchaoyidan.engine.ui.UIRectangle;
import hchaoyidan.engine.ui.UIShape;
import hchaoyidan.engine.ui.Viewport;
import starter.Vec2f;
import starter.Vec2i;


/**
 * Default launch screen for Tou
 * @author yidanzeng
 *
 */
public class MScreen extends Screen {

	/**
	 * Instantiates the StartScreen
	 * @param game
	 */
	public MScreen(Application game) {
		super(game);
		setup();
	}
	
	/** 
	 * Initiates all the screen elements
	 */
	@Override
	public void setup() {
		
		world = new MWorld(windowSize.x, windowSize.y);

		UIShape background = new UIRectangle(null, new Vec2f(0,0), null, new Vec2i(windowSize.x, windowSize.y));
		content.add(background);
		
		// viewport, does the drawing
		view = new Viewport(new Vec2f(0, 0), new Vec2i(windowSize.x,windowSize.y), background, world);
		content.add(view);

	}

	@Override
	public void onTick(long nanosSincePreviousTick) {
		world.onTick(nanosSincePreviousTick);	
	}
	
	@Override
	public void onKeyTyped(KeyEvent e) {
		world.onKeyTyped(e);	
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		if(e.getKeyChar() == "r".charAt(0)) {
			game.setScreen(new MScreen(game));
		}
		
		world.onKeyPressed(e);
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		world.onKeyReleased(e);
	}
	
	@Override
	public void onMouseClicked(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMouseClicked(e);
        }
	}

	@Override
	public void onMousePressed(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMousePressed(e);
        }
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMouseReleased(e);
        }
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMouseDragged(e);
        }
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMouseMoved(e);
        }
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMouseWheelMoved(e);
        }
	}
	
}
