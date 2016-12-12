package hchaoyidan.engine.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import hchaoyidan.engine.Edge;
import hchaoyidan.engine.Shape;
import starter.Vec2f;
import starter.Vec2i;

/**
 * Representation of a circle that collides
 * @author yidanzeng
 *
 */
public class CollisionCircle extends CollisionShape {

	public double radius;
	
	
	/**
	 * Instantiates the circle object
	 * @param color of circle
	 * @param position where it resides on the 2D plane
	 * @param parent object
	 * @param type identification string
	 * @param width diameter of the circle
	 */
	public CollisionCircle(Color color, Vec2f position, Shape parent, int width) {
		super(color, position, parent, "circle", new Vec2i(width, width));
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
	public void onDraw(Graphics2D g) {
		g.setColor(color);
		g.fillOval((int)position.x, (int)position.y, width, height);

		if(showMtv) {
			drawMtv(g);
		}
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


	@Override
	public Vec2f collides(CollisionShape o) {

		return o.collidesCircle(this);
	}

	@Override
	public Vec2f collidesCircle(CollisionCircle c) {
		double radiSum = radius + c.radius;
		
		if(centerPoint.dist(c.centerPoint) <= radiSum) {
			Vec2f vector = new Vec2f(centerPoint.x - c.centerPoint.x, centerPoint.y - c.centerPoint.y);
			Vec2f axis = vector.normalized();
			
			Vec2f thisRange = project(axis);
			Vec2f otherRange = c.project(axis);
			Vec2f result = axis.smult(mtv(thisRange, otherRange));
			
			assert result.dot(vector) > 0;
			return result;
			
		} else {
			return new Vec2f(0,0);
		}
		
	}

	@Override
	public Vec2f collidesAAB(CollisionAAB aab) {
		// checking if circle center in AAB
		
		float aabXMax = aab.getX() + aab.getWidth();
		float aabYMax = aab.getY() + aab.getHeight();
		
		// if centerpoint is within aab
		if(centerPoint.x >= aab.getX() && centerPoint.x <= aabXMax && centerPoint.y >= aab.getY() && centerPoint.y <= aabYMax) {
			List<Vec2f> boxPerp = aab.getPerpVectors();
			
			float minMagnitude = Float.POSITIVE_INFINITY;
			Vec2f mtv = new Vec2f(0,0);
			for(Vec2f axis : boxPerp) {
				float mtv1d = mtv(project(axis), aab.project(axis));
				if(mtv1d == 0) {
					return new Vec2f(0,0);
				} else if(Math.abs(mtv1d) < minMagnitude) {
					minMagnitude = Math.abs(mtv1d);
					mtv = axis.smult(mtv1d);
					
					Vec2f vector = new Vec2f(centerPoint.x - aab.centerPoint.x, centerPoint.y - aab.centerPoint.y);
					assert mtv.dot(vector) > 0;
				}
			}
			return mtv;

		} else { // clamp point check
			float x = 0;
			float y = 0;
			
			if(centerPoint.x >= aabXMax) {
				x = aabXMax;
			} else if(centerPoint.x <= aab.getX()) {
				x = aab.getX();
			} else {
				x = centerPoint.x;
			}
			
			if(centerPoint.y >= aabYMax) {
				y = aabYMax;
			} else if(centerPoint.y <= aab.getY()) {
				y = aab.getY();
			} else {
				y = centerPoint.y;
			}
			 
			if(centerPoint.dist(new Vec2f(x, y)) <= radius && !centerPoint.equals(new Vec2f(x,y))) { // if circle is intersecting with the box
				
				Vec2f vector = new Vec2f(centerPoint.x - x, centerPoint.y - y);
				Vec2f axis = vector.normalized();
				Vec2f thisRange = project(axis);
				Vec2f otherRange = aab.project(axis);
				

				Vec2f mtv = axis.smult(mtv(thisRange, otherRange));
				assert mtv.dot(vector) > 0;
				return mtv;

				
			} else {
				return new Vec2f(0,0);
			}
		}
		
	}

	
	@Override
	public Vec2f collidesPoly(CollisionPolygon m) {

		List<Vec2f> otherPerp = m.getPerpVectors();
		
		List<Vec2f> allPerp = new ArrayList<Vec2f>();
		allPerp.add(getCircleAxis(m));
		allPerp.addAll(otherPerp);
		
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
	
	@Override
	public List<Vec2f> getPerpVectors() {
		return new ArrayList<Vec2f>();
	}
	

	public Vec2f getCircleAxis(CollisionPolygon m) {
		float dist = Float.POSITIVE_INFINITY;
		Vec2f point = new Vec2f(0,0);
		for(Edge e : m.edges) {
			float tempDist = e.getStart().dist(centerPoint);
			if(tempDist < dist) {
				dist = tempDist;
				point = e.getStart();
			}
		}
		
		// centerpoint to vec
		Vec2f vector = new Vec2f(point.x - centerPoint.x, point.y - centerPoint.y);

		return vector.normalized();
	}
	@Override
	public Vec2f project(Vec2f axis) {
		double center = axis.dot(centerPoint);
		double min = center - radius;
		double max = center + radius;
		
		return new Vec2f((float) min, (float) max);
	}

	@Override
	public float checkRay(Vec2f ray, Vec2f origin) {
		float distance = 0;
		Vec2f centerVec = new Vec2f(centerPoint.x - origin.x, centerPoint.y - origin.y);
		
		Vec2f axis = ray.normalized();
		float projection = centerVec.dot(axis);
		Vec2f projectPoint = axis.smult(projection);

		Vec2f screenProjectPt = new Vec2f((projectPoint.x + origin.x), (projectPoint.y + origin.y));
		
		float totalDist = origin.dist(screenProjectPt);
		float x = (float) Math.sqrt(screenProjectPt.dist(centerPoint));
		float insideDist = (float) Math.sqrt(radius*radius - x*x);
		
		if(!isWithin(new Vec2i((int)origin.x, (int)origin.y))) { // if source outside circle
			if(isWithin(new Vec2i((int)screenProjectPt.x, (int)screenProjectPt.y))) { // if the projected point is within
				distance = totalDist - insideDist;
			}
			
		} else { // if source inside circle
			distance =  totalDist + insideDist;
		}
		return distance;
	}

	
}
