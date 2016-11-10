package hchaoyidan.engine.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import hchaoyidan.engine.Shape;
import starter.Vec2f;
import starter.Vec2i;

/**
 * Circle UI object
 * @author yidanzeng
 *
 */
public class UICircle extends UIShape {
	
	public double widthRatio;
	public double xRatio;
	public double yRatio;
	Vec2f centerPoint;
	public double radius;
	
	/**
	 * Instantiates the circle object
	 * @param color of circle
	 * @param position where it resides on the 2D plane
	 * @param parent object
	 * @param type identification string
	 * @param width diameter of the circle
	 */
	public UICircle(Color color, Vec2f position, Shape parent, int width) {
		super(color, position, parent, "circle", new Vec2i(width, width));
		this.width = width;
		this.height = width;
		radius = width / 2.0;
		lockRatio = true;
		
		findCenter();
	}

	/**
	 * Finds center point of this circle
	 */
	public void findCenter() {
		double centerX = (width / 2.0) + position.x;
		double centerY = (width / 2.0) + position.y;
		centerPoint = new Vec2f((float) centerX, (float) centerY); 

	}
	
	
	@Override
	public void drawSelf(Graphics2D g) {
		g.setColor(color);
		g.fillOval((int)position.x, (int)position.y, width, height);
	}

	@Override
	public boolean isWithin(Vec2i mouse) {
		double xDist = mouse.x - centerPoint.x;
		double yDist = mouse.y - centerPoint.y;
		double dist = Math.sqrt(xDist * xDist + yDist * yDist);
		if(dist < (double) width/2) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void onResize(Vec2i newsize) {
		float newRatio = (float) newsize.x / (float) newsize.y;
		
		// for width and height
		if(lockRatio) { 
			findAspectRatio();
			if(newRatio < 1) { // go by the smallest side: width
				width = (int) (sizeRatio.x * newsize.x);
				height = (int) (width * aspectRatio);
				
			} else { // height
				height = (int) (sizeRatio.y * newsize.y); 
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
	
		radius = width / 2.0;
		findCenter();
	}

}
