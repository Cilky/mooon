package hchaoyidan.engine.game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import hchaoyidan.engine.Application;
import hchaoyidan.engine.Screen;
import hchaoyidan.engine.ui.UIRectangle;
import hchaoyidan.engine.ui.UIShape;
import hchaoyidan.engine.ui.Viewport;
import starter.Vec2f;
import starter.Vec2i;


/**
 * Debug screen for Tou
 * @author yidanzeng
 *
 */
public class DebugScreen extends Screen {
	
	/**
	 * Instantiates the DebugScreen
	 * @param game
	 */
	public DebugScreen(Application game) {
		super(game);
		setup();
	}
	
	@Override
	public void setup() {
		
		world = new DebugWorld(windowSize.x, windowSize.y);
		world.setBackground(Color.BLUE);

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
