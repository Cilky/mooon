package engine.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import engine.Shape;
import starter.Vec2f;
import starter.Vec2i;

/**
 * Collision main shape class
 * @author yidanzeng
 *
 */
public abstract class CollisionShape extends Shape {
	
	public Vec2f mtv = new Vec2f(0,0);
	public Vec2f centerPoint = new Vec2f(0,0);
	public boolean showMtv = false;
	
	/**
	 * Instantiation for Shape object
	 * @param color of object
	 * @param position where it resides on the 2D plane
	 * @param parent object
	 * @param type identification string
	 */
	public CollisionShape(Color color, Vec2f position, Shape parent, String type, Vec2i size) {
		super(color, position, parent, type, size);
	}
	
	/**
	 * Collides function that the class itself will then further call collides for itself
	 * @param o
	 * @return true if this shape and o is colliding, false otherwise
	 */
	public abstract Vec2f collides(CollisionShape o);
	
	/**
	 * Collides this shape with a circle
	 * @param c
	 * @return true if colliding, false otherwise
	 */
	public abstract Vec2f collidesCircle(CollisionCircle c);
	
	/**
	 * Collides this shape with a AAB (rectangle)
	 * @param aab
	 * @return true if colliding, false otherwise
	 */
	public abstract Vec2f collidesAAB(CollisionAAB aab);
	
	/**
	 * Finds the centerpoint for the shape
	 */
	public abstract void findCenter(); 
	
	/**
	 * Collides this shape with a polygon shape
	 * @param m
	 * @return true if colliding, false otherwise
	 */
	public Vec2f collidesPoly(CollisionPolygon m) {
		
		List<Vec2f> allPerp = new ArrayList<Vec2f>();
		allPerp.addAll(getPerpVectors());
		allPerp.addAll(m.getPerpVectors());
		
		float minMagnitude = Float.POSITIVE_INFINITY;
		Vec2f mtv = new Vec2f(0,0);
		for(Vec2f axis : allPerp) {
			float mtv1d = mtv(project(axis), m.project(axis));
			if(mtv1d == 0) {
				return new Vec2f(0,0); 
			} else if(Math.abs(mtv1d) < minMagnitude) {
				minMagnitude = Math.abs(mtv1d);
				mtv = axis.smult(mtv1d);
			}
		}
		
		return mtv;		
	}

	
	/**
	 * Draws the mtv lines
	 * @param g
	 */
	public void drawMtv(Graphics2D g) {
		
		g.setColor(Color.BLACK);
		g.drawLine((int)centerPoint.x, (int)centerPoint.y, (int) (centerPoint.x + mtv.x), (int) (centerPoint.y + mtv.y));
	}
	
	/**
	 * Moves this shape
	 * @param deltaX
	 * @param deltaY
	 */
	public void move(float deltaX, float deltaY) {
		position = new Vec2f(position.x + deltaX, position.y + deltaY);
		
		findPosRatios();
		findCenter();
	}
	
	/**
	 * Finds all the vectors of the edges of this polygon
	 * @return arraylist of all the edge vectors
	 */
	public abstract List<Vec2f> getPerpVectors();
	
	/**
	 * Checks if ranges overlap
	 * @param range
	 * @param otherRange
	 * @return true if overlap, false otherwise
	 */
	public boolean overlap(Vec2f range, Vec2f otherRange) { 
		return range.x <= otherRange.y && range.y >= otherRange.x || otherRange.x <= range.y && otherRange.y >= range.x;
	}
	
    /**
     * Calculating the MTV given two intervals of object a and b, assuming that a is colliding with b
     * @param a
     * @param b
     * @return the value of mtv that a must move to uncollide with object b
     */
	public float mtv(Vec2f range, Vec2f otherRange) {
		float aRight = otherRange.y - range.x;
		float aLeft = range.y - otherRange.x;
		
		if(aLeft < 0 || aRight < 0) {
			return 0;
		} else if(aRight < aLeft) {
			return aRight;
		} else {
			return -aLeft;
		}
	}
	
	/**
	 * Projecting this shape onto a normalized axis
	 * @param axis
	 * @return
	 */
	public abstract Vec2f project(Vec2f axis);
	
	/**
	 * Returns the distance between the intersection of CollisionShape and the ray source, or 0 otherwise
	 * @param ray
	 * @param origin
	 * @return
	 */
	public abstract float checkRay(Vec2f ray, Vec2f origin);
}
