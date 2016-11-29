package hchaoyidan.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

import starter.Vec2f;
import starter.Vec2i;

/**
 * The general representation of a Shape object
 * @author yidanzeng
 *
 */
public abstract class Shape implements Serializable {
	protected Color color;
	public Vec2f position;
	public String type;
	public boolean transition;
	
	protected int width = 0;
	protected int height = 0;
	
	public Shape parent;

	public Vec2f posRatio;
	public float aspectRatio;
	public Vec2f sizeRatio;
	
	protected boolean lockRatio;
	
	/**
	 * Instantiation for Shape object
	 * @param color of object
	 * @param position where it resides on the 2D plane
	 * @param parent object
	 * @param type identification string
	 */
	public Shape(Color color, Vec2f position, Shape parent, String type, Vec2i size) {
		this.color = color;
		this.parent = parent;
		this.type = type;
		this.lockRatio = false;
		
		if(parent != null && position != null) {
			this.position = new Vec2f(parent.getX() + position.x, parent.getY() + position.y);
		} else {
			this.position = position;
		}
		
		if(position != null) {
			findPosRatios();
		}
		
		if(size != null) {
			width = size.x;
			height = size.y;
			findSizeRatios();
			findAspectRatio();	
		}
		
	}
	
	/**
	 * Locks the aspect ratio of this object for resize and saves it
	 */
	public void lockRatio() {
		lockRatio = true;
	}

	/**
	 * Saves the necessary position ratios for the specific subclass
	 */
	public void findPosRatios() {
		if(parent != null) {
			posRatio = new Vec2f(((position.x - parent.getX())/parent.getWidth()), ((position.y - parent.getY())/parent.getHeight()));
		} else {
			posRatio = new Vec2f(0, 0);
		}
	}
	
	/**
	 * Saves the necessary aspect ratio for the specific subclass
	 */
	public void findAspectRatio() {
		aspectRatio = (float) width / (float) height;
	}
	
	/**
	 * Saves the necessary size ratios for the specific subclass
	 */
	public void findSizeRatios() {
		if(parent != null) {
			sizeRatio = new Vec2f((width/parent.getWidth()) * parent.sizeRatio.x, (height/parent.getHeight()) * parent.sizeRatio.y);
		} else {
			sizeRatio = new Vec2f(1, 1);
		}
	}
	
	/**
	 * Resizes the shape
	 * @param newsize
	 */
	public abstract void onResize(Vec2i newsize);
	
	/**
	 * Changes the color of this object when needed after instantiation
	 * @param color new color
	 */
	public void changeColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	/**
	 * Gets the X coordinate of this element
	 * @return X coordinate
	 */
	public float getX() {
		return position.x;
	}
	
	/**
	 * Gets the Y coordinate of this element
	 * @return Y coordinate
	 */
	public float getY() {
		return position.y;
	}
	
	/**
	 * Gets the width of this element
	 * @return width
	 */
	public float getWidth() {
		return width;
	}
	
	/**
	 * Gets the height of this element
	 * @return height
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * The draw function that displays this object through calling drawSelf and then calling the draw function on this
	 * object's children
	 * @param g Graphics2D object
	 */
	public abstract void onDraw(Graphics2D g);

	/**
	 * Checks if the position of the mouse was within this element
	 * @param mouse object
	 * @return true if is within, false otherwise
	 */
	public abstract boolean isWithin(Vec2i mouse);

	public Vec2f getPosition() {
		return position;
	}

	public void setPosition(Vec2f position) {
		this.position = position;
	}
	
	
	
}
