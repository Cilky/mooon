package hchaoyidan.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.Serializable;
import java.util.ArrayList;

import hchaoyidan.engine.ui.UIShape;
import hchaoyidan.engine.ui.Viewport;
import starter.Vec2f;
import starter.Vec2i;

/**
 * Screen representation within the game engine, displays UI elements and handles mouse and keyboard events
 * @author yidanzeng
 *
 */
public abstract class Screen implements Serializable {

	protected Vec2i windowSize;
	protected Application game;
	protected Viewport view;
	protected PhysicsWorld world;
	protected UIShape fadeMask;
	protected boolean fade = false;
	protected boolean lerp = false;
	public boolean finishFade = false;
	public boolean finishLerp = false;
	private Vec2f lerpVelocity = new Vec2f(0, 0); //default, can set
	
	public ArrayList<UIShape> content = new ArrayList<UIShape>();
	
	/**
	 * Creates the Screen object
	 * @param game takes in the Application game for switching or progressing screens
	 */
	public Screen(Application game) {
		this.game = game;
		windowSize = game.getWindowSize();
	}
	
	/**
	 * Sets up the screen with the necessary UI elements
	 */
	public abstract void setup();
	
	/**
	 * Called at a regular interval set by {@link #setTickFrequency(long)}. Use to update any state
	 * that changes over time.
	 * 
	 * @param nanosSincePreviousTick	approximate number of nanoseconds since the previous call
	 *                              	to onTick
	 */
	public void onTick(long nanosSincePreviousTick) {
		if (fade) {
			fadeOut();
		}
		if (lerp) {
			lerp();
		}
	}

	/**
	 * Called when screen needs to be updated, either by onTick or other methods after an action. Diverts the call to
	 * the UIKit to handle
	 * @param g a Graphics2D object used for drawing
	 */
	public void onDraw(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		
		for(UIShape s : content) {
			s.onDraw(g);
		}
	}
	
	
	
	/* --------------------------------------------------------------------------------------------
	 * Events from the Application
	 * --------------------------------------------------------------------------------------------*/

	public void setWorld(PhysicsWorld world) {
		this.world = world;
	}

	public abstract void onKeyTyped(KeyEvent e);

	public abstract void onKeyPressed(KeyEvent e);

	public abstract void onKeyReleased(KeyEvent e);

	public abstract void onMouseClicked(MouseEvent e);

	public abstract void onMousePressed(MouseEvent e);

	public abstract void onMouseReleased(MouseEvent e);

	public abstract void onMouseDragged(MouseEvent e);

	public abstract void onMouseMoved(MouseEvent e);

	public abstract void onMouseWheelMoved(MouseWheelEvent e);


	/**
	 * Called when screen changes size and needs to re-render its UI elements; goes through its ArrayList of UI
	 * elements and calls their own resize methods
	 * @param newSize the new window size
	 */
	public void onResize(Vec2i newSize) {
		if(!newSize.equals(windowSize)) {
			windowSize = newSize;
			for(UIShape s : content) {
				s.onResize(newSize);
			}
		}
	}
	
	public void fadeOut() {   
		boolean fadeComplete = true;
		for (UIShape s : content) {
			s.fadeOut();
			if (!s.fadeFinished()) {
				fadeComplete = false;
			}
		}
		if (fadeComplete) {
			finishFade = true;
		}
	}
	
	public void lerp() {
		for (UIShape s : content) {
			s.lerp(lerpVelocity);
		}
	}

	public boolean isFade() {
		return fade;
	}

	public void setFade(boolean fade) {
		this.fade = fade;
	}

	public boolean isLerp() {
		return lerp;
	}

	public void setLerp(boolean lerp) {
		this.lerp = lerp;
	}

	public Vec2f getLerpVelocity() {
		return lerpVelocity;
	}

	public void setLerpVelocity(Vec2f lerpVelocity) {
		this.lerpVelocity = lerpVelocity;
	}

	public UIShape getFadeMask() {
		return fadeMask;
	}

	public void setFadeMask(UIShape fadeMask) {
		this.fadeMask = fadeMask;
	}
	
	
}
