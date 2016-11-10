package hchaoyidan.engine.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import hchaoyidan.engine.Shape;
import starter.Vec2f;
import starter.Vec2i;

/**
 * Representation of a rectangle class
 * @author yidanzeng
 *
 */
public class UIRectangle extends UIShape {
	
	public double ratio;
	public double widthRatio;
	public double heightRatio;
	public double xRatio;
	public double yRatio;
	
	/**
	 * Instantiates the Rectangle object
	 * @param color of rectangle
	 * @param pos where it resides on the 2D plane
	 * @param parent object
	 * @param type identification string
	 * @param width of rectangle
	 * @param height of rectangle
	 */
	public UIRectangle(Color color, Vec2f pos, Shape parent, Vec2i size) {
		super(color, pos, parent, "rectangle", size);
	}

	@Override
	public void drawSelf(Graphics2D g) {
		g.setColor(color);
		g.fillRect((int) position.x, (int) position.y, width, height);
	}
	
	@Override
	public boolean isWithin(Vec2i mouse) {
		if(mouse.x >= position.x && mouse.x <= position.x + width) {
			if(mouse.y >= position.y && mouse.y <= position.y + height) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onResize(Vec2i newsize) {
		float newRatio = (float) newsize.x / (float) newsize.y;
		
		if(lockRatio) { 
			findAspectRatio();
			if(newRatio < 1) { // go by the smallest side: width
				width = (int) (sizeRatio.x * newsize.x); // parent??? somewhere
				height = (int) (width * (1/aspectRatio));			
				
			} else { // height
				height = (int) (sizeRatio.y * newsize.y); // it's using height suddenly...
				width = (int) (height * aspectRatio);
			}
		} else {
			width = (int) (sizeRatio.x * newsize.x);
			height = (int) (sizeRatio.y * newsize.y);
		}
		
		// for position
		if(parent != null) {
			position = new Vec2f((posRatio.x * parent.getWidth()) + parent.getX(), (posRatio.y * parent.getHeight()) + parent.getY());
		} else {
			position = new Vec2f(posRatio.x * newsize.x, posRatio.y * newsize.y);
		}
	}
}
