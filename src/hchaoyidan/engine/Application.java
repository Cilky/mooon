package hchaoyidan.engine;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.Serializable;
import java.util.ArrayList;

import starter.SwingFrontEnd;
import starter.Vec2i;

/**
 * Application within the engine that handles the delegation and passing of inputs to the current screen.
 * @author yidanzeng
 *
 */

public class Application extends SwingFrontEnd implements Serializable {
	
	ArrayList<Screen> screenStack = new ArrayList<Screen>();
	Screen currentScreen;
	
	/**
	 * Creates the Application
	 * @param title of the application
	 * @param fullscreen true for entering into fullscreen, false for default
	 */
	public Application(String title, boolean fullscreen) {
		super(title, fullscreen);
	}

	/**
	 * Sets the current screen of the application from the outside 
	 * @param toSet screen to be added as currentScreen
	 */
	public void setScreen(Screen toSet) {
		currentScreen = toSet;
	}
	
	/**
	 * Called at a regular interval set by {@link #setTickFrequency(long)}. Use to update any state
	 * that changes over time.
	 * 
	 * @param nanosSincePreviousTick	approximate number of nanoseconds since the previous call
	 *                              	to onTick
	 */
	@Override
	protected void onTick(long nanosSincePreviousTick) {
		currentScreen.onTick(nanosSincePreviousTick);
	}
	
	/**
	 * Called when the screen needs to be redrawn. This is at least once per tick, but possibly
	 * more frequently (for example, when the window is resizing).
	 * <br><br>
	 * Note that the entire drawing area is cleared before each call to this method. Furthermore,
	 * {@link #onResize} is guaranteed to be called before the first invocation of onDraw.
	 * 
	 * @param g		a {@link Graphics2D} object used for drawing.
	 */
	@Override
	protected void onDraw(Graphics2D g) {
		currentScreen.onDraw(g);
		
	}
	
	/**
	 * @param e		an AWT {@link KeyEvent} representing the input event.
	 * @see KeyListener#keyTyped(KeyEvent)
	 */
	@Override
	protected void onKeyTyped(KeyEvent e) {
		currentScreen.onKeyTyped(e);
	}
	
	/**
	 * @param e		an AWT {@link KeyEvent} representing the input event.
	 * @see KeyListener#keyPressed(KeyEvent)
	 */
	@Override
	protected void onKeyPressed(KeyEvent e) {
		currentScreen.onKeyPressed(e);
	}
	
	/**
	 * @param e		an AWT {@link KeyEvent} representing the input event.
	 * @see KeyListener#keyReleased(KeyEvent)
	 */
	@Override
	protected void onKeyReleased(KeyEvent e) {
		currentScreen.onKeyReleased(e);
	}
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	@Override
	protected void onMouseClicked(MouseEvent e) {
		currentScreen.onMouseClicked(e);
	}
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseListener#mousePressed(MouseEvent)
	 */
	@Override
	protected void onMousePressed(MouseEvent e) {
		currentScreen.onMousePressed(e);
	}
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseListener#mouseReleased(MouseEvent)
	 */
	@Override
	protected void onMouseReleased(MouseEvent e) {
		currentScreen.onMouseReleased(e);
	}
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseMotionListener#mouseDragged(MouseEvent)
	 */
	@Override
	protected void onMouseDragged(MouseEvent e) {
		currentScreen.onMouseDragged(e);
	}
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseMotionListener#mouseMoved(MouseEvent)
	 */
	@Override
	protected void onMouseMoved(MouseEvent e) {
		currentScreen.onMouseMoved(e);
	}
	
	/**
	 * @param e		an AWT {@link MouseWheelEvent} representing the input event.
	 * @see MouseWheelListener#mouseWheelMoved(MouseWheelEvent)
	 */
	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		currentScreen.onMouseWheelMoved(e);
	}
	
	/**
	 * Called when the size of the drawing area changes. Any subsequent calls to onDraw should note
	 * the new size and be sure to fill the entire area appropriately. Guaranteed to be called
	 * before the first call to onDraw.
	 * 
	 * @param newSize	the new size of the drawing area.
	 */
	@Override
	protected void onResize(Vec2i newSize) {
		windowedSize = newSize;
		currentScreen.onResize(newSize);
	}
	
	/**
	 * Helpful in getting the size of the current window
	 * 
	 * @return windowedSize the size of the window
	 */
	public Vec2i getWindowSize() {
		return windowedSize;
	}
	
	/**
	 * Pops the last screen off when needing to revert to the previous Screen
	 */
	public void back() {
		if(screenStack.size() != 0) {
			currentScreen = screenStack.remove(screenStack.size() - 1);
		}
	}
	
	/**
	 * Sets the currentScreen to a new screen resulting from user input
	 * 
	 * @param toSet	new Screen that now handles all calls
	 */
	public void next(Screen toSet) {
		screenStack.add(currentScreen);
		currentScreen = toSet;
	}
	
}
