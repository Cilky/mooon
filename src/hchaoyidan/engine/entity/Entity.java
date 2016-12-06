package hchaoyidan.engine.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.Serializable;

import starter.Vec2i;

/**
 * Representation of an entity within a game, usually static
 * 
 * @author yidanzeng
 *
 */
public class Entity implements Serializable {
	
	protected CollisionShape shape;
	public String type;
	public int drawOrder;
	public boolean delete = false;
	
	/**
	 * Constructor for Entity
	 * @param s Shape
	 */
	public Entity(CollisionShape s) {
		shape = s;
		type = "entity";
	}
	
	/**
	 * Returns shape within entity
	 * @return shape
	 */
	public CollisionShape getShape() {
		return shape;
	}
	
	/**
	 * Returns type of entity
	 * @return type 
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Called at a regular interval set by {@link #setTickFrequency(long)}. Use to update any state
	 * that changes over time.
	 * 
	 * @param nanosSincePreviousTick	approximate number of nanoseconds since the previous call
	 *                              	to onTick
	 */
	public void onTick(long nanosSincePreviousTick) {	
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
	public void onDraw(Graphics2D g) {
		if(!delete) {
			shape.onDraw(g);
		}
	}


	protected void onMouseClicked(MouseEvent e) {
		
	}
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseListener#mousePressed(MouseEvent)
	 */

	protected void onMousePressed(MouseEvent e) {
		
	}
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseListener#mouseReleased(MouseEvent)
	 */

	protected void onMouseReleased(MouseEvent e) {
		
	}
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseMotionListener#mouseDragged(MouseEvent)
	 */

	protected void onMouseDragged(MouseEvent e) {
		
	}
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseMotionListener#mouseMoved(MouseEvent)
	 */

	protected void onMouseMoved(MouseEvent e) {
		
	}
	
	/**
	 * @param e		an AWT {@link MouseWheelEvent} representing the input event.
	 * @see MouseWheelListener#mouseWheelMoved(MouseWheelEvent)
	 */

	protected void onMouseWheelMoved(MouseWheelEvent e) {
		
	}
	
	/**
	 * Called when the size of the drawing area changes. Any subsequent calls to onDraw should note
	 * the new size and be sure to fill the entire area appropriately. Guaranteed to be called
	 * before the first call to onDraw.
	 * 
	 * @param newSize	the new size of the drawing area.
	 */

	public void onResize(Vec2i newSize) {
		shape.onResize(newSize);
	}
	
	/**
	 * Checks if mouse is within entity
	 * @param mouse
	 * @return true if within, false otherwise
	 */
	public boolean isWithin(Vec2i mouse) {
		return shape.isWithin(mouse);
	}
	
	/**
	 * Moves the entity 
	 * @param deltaX
	 * @param deltaY
	 */
	public void move(float deltaX, float deltaY) {
		shape.move(deltaX, deltaY);
	}

	public void setShape(CollisionShape shape) {
		this.shape = shape;
	}

	public void changeColor(Color color) {
		shape.changeColor(color);
	}
	
	

}
