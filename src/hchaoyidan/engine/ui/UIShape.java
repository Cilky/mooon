package hchaoyidan.engine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

import hchaoyidan.engine.Shape;
import starter.Vec2f;
import starter.Vec2i;

/**
 * The main UI element for displaying
 * @author yidanzeng
 *
 */
public abstract class UIShape extends Shape implements Serializable {

	public boolean clickable = false;
	protected boolean isCenter = false;
	
	/**
	 * Instantiation for Shape object
	 * @param color of object
	 * @param position where it resides on the 2D plane
	 * @param parent object
	 * @param type identification string
	 */
	public UIShape(Color color, Vec2f position, Shape parent, String type, Vec2i size) {
		super(color, position, parent, type, size);
	}
	
	/**
	 * Calculates the center of the parent element
	 * @return the Vec2i coordinates for this element to be centered inside its parent
	 */
	public void center() {
		if(parent != null) {
			float parMidWidth = parent.getWidth() / 2f;
			float parMidHeight = parent.getHeight() / 2f;
			float midWidth = width / 2f;
			float midHeight = height / 2f;
			
			float newX = parent.getX() + (parMidWidth - midWidth);
			float newY = parent.getY() + (parMidHeight - midHeight);
			
			position = new Vec2f(newX, newY);
			findPosRatios();
		}
	}
	

	/**
	 * Sets this element to take click events
	 * @param clickable
	 */
	public void setClickable() {
		this.clickable = true;
	}
	
	/**
	 * Sets this element to be center
	 * @param center
	 */
	public void setCenter() {
		this.isCenter = true;
	}
	
	/**
	 * The draw function that displays this object through calling drawSelf and then calling the draw function on this
	 * object's children
	 * @param g Graphics2D object
	 */
	@Override
	public void onDraw(Graphics2D g) {
		if(isCenter) {
			center();
		}
		
		drawSelf(g);
	}
	
	public abstract void drawSelf(Graphics2D g);
	
}
