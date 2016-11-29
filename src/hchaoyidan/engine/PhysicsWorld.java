package hchaoyidan.engine;

import java.awt.Color;
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
import java.util.HashSet;
import java.util.List;

import hchaoyidan.engine.entity.Collision;
import hchaoyidan.engine.entity.Entity;
import hchaoyidan.engine.entity.PhysicsEntity;
import hchaoyidan.game.entity.MPhysicsEntity;
import starter.Vec2f;
import starter.Vec2i;

/**
 * Representation of a physics world
 * @author yidanzeng
 *
 * @param <T>
 */
public abstract class PhysicsWorld<T extends PhysicsEntity<T>> implements Serializable {
	
	public List<Entity> entities;
	public List<T> physicEntities;
	public List<T> newPhyEnt;
	public Vec2i windowSize;
	protected Color backgroundColor;
	public Friction environ;
	
	/**
	 * Constructor for world
	 * @param windowSizeX
	 * @param windowSizeY
	 */
	public PhysicsWorld(int windowSizeX, int windowSizeY) {
		windowSize = new Vec2i (windowSizeX, windowSizeY);
		entities = new ArrayList<Entity>();
		physicEntities = new ArrayList<T>();
		newPhyEnt = new ArrayList<T>();
	}
	
	/**
	 * Sets up the world
	 */
	protected abstract void setup();
	
	/**
	 * Takes care of removing, adding, and checking collision of entities
	 */
	protected abstract void update();
	
	/**
	 * Adds a PhysicEntity to an arraylist to be added during update
	 * @param toAdd
	 */
	public void addPhysicEntity(T toAdd) {
		newPhyEnt.add(toAdd);
	}
	
	/**
	 * Adds an Entity to world's collection of Entities
	 * @param toAdd
	 */
	public void addEntity(T toAdd) {
		entities.add(toAdd);
	}
	
	/**
	 * Sets the background color for the world
	 * @param color
	 */
	public void setBackground(Color color) {
		backgroundColor = color;
	}
	
	/**
	 * Called at a regular interval set by {@link #setTickFrequency(long)}. Use to update any state
	 * that changes over time.
	 * 
	 * @param nanosSincePreviousTick	approximate number of nanoseconds since the previous call
	 *                              	to onTick
	 */
	public void onTick(long nanosSincePreviousTick) {
		selfTick(nanosSincePreviousTick);
		boolean setCollide = false;
		if (physicEntities.size() > 1) {

			for (int i = 0; i < physicEntities.size(); i++) {
				T entity1 = physicEntities.get(i);
				for (int k = i + 1; k < physicEntities.size(); k++) {
					T entity2 = physicEntities.get(k);
					Vec2f isColliding = entity1.collide(entity2);
					if (isColliding.mag2() < 0.0001) {
						continue;
					}
					if (entity1.isStatic && !entity2.isStatic) {
						setCollide = true;
						entity2.onCollide(new Collision<T>(entity1, isColliding, environ));
					} else if (!entity1.isStatic && entity2.isStatic) {
						setCollide = true;
						entity1.onCollide(new Collision<T>(entity2, isColliding.smult(-1f), environ));
					} else if (!entity1.isStatic && !entity2.isStatic) {
						setCollide = true;
						entity1.onCollide(
								new Collision<T>(entity2, new Vec2f(-isColliding.x / 2f, -isColliding.y / 2f), environ));
						entity2.onCollide(new Collision<T>(entity1, new Vec2f(isColliding.x / 2f, isColliding.y / 2f), environ));
					}

					if(setCollide) {
						entity1.isColliding = true;
						entity2.isColliding = true;
					}
					
					entity1.doCollide(entity2);
					entity2.doCollide(entity1);
				}

			}
		}
	}
	
	/**
	 * Casts a ray and returns the object that is closest to the ray's line of sight, or null otherwise
	 * @param vec
	 * @param origin
	 */
	public Pair<T, Float> rayCast(Vec2f vec, T origin) {
		T toReturn = null;
		float t = 0;
		
		if(physicEntities.size() > 1) {
			for(int i = 0; i < physicEntities.size(); i++) {
				T entity = physicEntities.get(i);
					if(!entity.equals(origin)) {
						float distance = entity.checkRay(vec, origin.getShape().centerPoint);
						
						if(distance > 0) { // if the ray hit
							toReturn = entity;
							t = distance;
						}
					}
				}
			}
		
		return new Pair<T, Float>(toReturn, t);
	}
	
	public abstract void selfTick(long nanosSincePreviousTick);
	
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
		
		for(Entity e : entities) {
			e.onDraw(g);
		}
		
		for(PhysicsEntity<T> p : physicEntities) {
			p.onDraw(g);
		}
	}
	/**
	 * @param e		an AWT {@link KeyEvent} representing the input event.
	 * @see KeyListener#keyTyped(KeyEvent)
	 */
	public abstract void onKeyTyped(KeyEvent e);
	
	/**
	 * @param e		an AWT {@link KeyEvent} representing the input event.
	 * @see KeyListener#keyPressed(KeyEvent)
	 */

	public abstract void onKeyPressed(KeyEvent e);
	
	/**
	 * @param e		an AWT {@link KeyEvent} representing the input event.
	 * @see KeyListener#keyReleased(KeyEvent)
	 */

	public abstract void onKeyReleased(KeyEvent e);
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */

	public abstract void onMouseClicked(MouseEvent e);
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseListener#mousePressed(MouseEvent)
	 */

	public abstract void onMousePressed(MouseEvent e);
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseListener#mouseReleased(MouseEvent)
	 */

	public abstract void onMouseReleased(MouseEvent e);
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseMotionListener#mouseDragged(MouseEvent)
	 */

	public abstract void onMouseDragged(MouseEvent e);
	
	/**
	 * @param e		an AWT {@link MouseEvent} representing the input event.
	 * @see MouseMotionListener#mouseMoved(MouseEvent)
	 */

	public abstract void onMouseMoved(MouseEvent e);
	
	/**
	 * @param e		an AWT {@link MouseWheelEvent} representing the input event.
	 * @see MouseWheelListener#mouseWheelMoved(MouseWheelEvent)
	 */

	public abstract void onMouseWheelMoved(MouseWheelEvent e);
	
	/**
	 * Called when the size of the drawing area changes. Any subsequent calls to onDraw should note
	 * the new size and be sure to fill the entire area appropriately. Guaranteed to be called
	 * before the first call to onDraw.
	 * 
	 * @param newSize	the new size of the drawing area.
	 */

	public void onResize(Vec2i newSize) {
		windowSize = newSize;
		
		for(Entity e : entities) {
			e.onResize(newSize);
		}
		
		for(PhysicsEntity<T> p : physicEntities) {
			p.onResize(newSize);
		}
	}
	
	/**
	 * Logs key inputs
	 * @author yidanzeng
	 *
	 */
	public static final class KeyLogger {
		
		private static HashSet<Character> keysPressed = new HashSet<Character>();
		
		private KeyLogger () { // private constructor
		}
		
		public static HashSet<Character> getKeys() {
			return keysPressed;
		}
		
		public static void add(char key) {
			if(!keysPressed.contains(key)) {
				keysPressed.add(key);
			}
		}
		
		public static void remove(char key) {
			if(keysPressed.contains(key)) {
				keysPressed.remove(key);
			}
		}
		
		public static void reset() {
			keysPressed = new HashSet<Character>();
		}
	}
	
}
