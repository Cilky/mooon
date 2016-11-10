package engine.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import engine.Edge;
import engine.Shape;
import starter.Vec2f;
import starter.Vec2i;

/**
 * Representation of an axis aligned rectangle class that collides
 * @author yidanzeng
 *
 */
public class CollisionAAB extends CollisionShape {
	
	/**
	 * Instantiates the Rectangle object
	 * @param color of rectangle
	 * @param pos where it resides on the 2D plane
	 * @param parent object
	 * @param type identification string
	 * @param width of rectangle
	 * @param height of rectangle
	 */
	public CollisionAAB(Color color, Vec2f pos, Shape parent, Vec2i size) {
		super(color, pos, parent, "rectangle", size);
		findCenter();
	}

	@Override
	public void onDraw(Graphics2D g) {
		g.setColor(color);
		g.fillRect((int) position.x, (int) position.y, width, height);
		
		if(showMtv) {
			drawMtv(g);
		}
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
		
		findCenter();
	}
	
	@Override
	public Vec2f collides(CollisionShape o) {
		return o.collidesAAB(this);
	}

	@Override
	public Vec2f collidesCircle(CollisionCircle c) {
		// checking if circle center in AAB
		
		float aabXMax = position.x + width;
		float aabYMax = position.y + height;
		
		// if centerpoint is within aab
		if(c.centerPoint.x >= position.x && c.centerPoint.x <= aabXMax && c.centerPoint.y >= position.y && c.centerPoint.y <= aabYMax) {
			List<Vec2f> boxPerp = getPerpVectors();
			
			float minMagnitude = Float.POSITIVE_INFINITY;
			Vec2f mtv = new Vec2f(0,0);
			for(Vec2f axis : boxPerp) {
				float mtv1d = mtv(project(axis), c.project(axis));
				if(mtv1d == 0) {
					return new Vec2f(0,0);
				} else if(Math.abs(mtv1d) < minMagnitude) {
					minMagnitude = Math.abs(mtv1d);
					mtv = axis.smult(mtv1d);
					
					Vec2f vector = new Vec2f(centerPoint.x - c.centerPoint.x, centerPoint.y - c.centerPoint.y);
					assert mtv.dot(vector) > 0;
				}
			}
		
			return mtv;

		} else { // clamp point check
			float x = 0;
			float y = 0;
			
			if(c.centerPoint.x >= aabXMax) {
				x = aabXMax;
			} else if(c.centerPoint.x <= position.x) {
				x = position.x;
			} else {
				x = c.centerPoint.x;
			}
			
			if(c.centerPoint.y >= aabYMax) {
				y = aabYMax;
			} else if(c.centerPoint.y <= position.y) {
				y = position.y;
			} else {
				y = c.centerPoint.y;
			}
			 
			if(c.centerPoint.dist(new Vec2f(x, y)) <= c.radius && !c.centerPoint.equals(new Vec2f(x,y))) { // if circle is intersecting with the box
				
				Vec2f vector = new Vec2f(c.centerPoint.x - x, c.centerPoint.y - y);
				Vec2f axis = vector.normalized();

				//System.out.println("vec "  + vector);
				//System.out.println("axis " + axis);
				
				Vec2f thisRange = project(axis);
				Vec2f otherRange = c.project(axis);
				
				Vec2f mtv = axis.smult(mtv(thisRange, otherRange));
				assert mtv.dot(vector) > 0;
				
				return mtv;
				
			} else {
				return new Vec2f(0,0);
			}
		}
		
	}

	@Override
	public Vec2f collidesAAB(CollisionAAB aab) {
		
		List<Vec2f> allPerp = new ArrayList<Vec2f>();
		allPerp.addAll(getPerpVectors());
		allPerp.addAll(aab.getPerpVectors());
		
		float minMagnitude = Float.POSITIVE_INFINITY;
		Vec2f mtv = new Vec2f(0,0);
		for(Vec2f axis : allPerp) {
			float mtv1d = mtv(project(axis), aab.project(axis));
			if(mtv1d == 0) {
				return new Vec2f(0,0); 
			} else if(Math.abs(mtv1d) < minMagnitude) {
				minMagnitude = Math.abs(mtv1d);
				mtv = axis.smult(mtv1d);
			}
		}
		
		Vec2f vector = new Vec2f(centerPoint.x - aab.centerPoint.x, centerPoint.y - aab.centerPoint.y);
		assert mtv.dot(vector) > 0;
		
		return mtv;		
	}
	
	@Override
	public List<Vec2f> getPerpVectors() {
		ArrayList<Vec2f> perpVectors = new ArrayList<Vec2f>();
		
		perpVectors.add(new Vec2f(1,0));
		perpVectors.add(new Vec2f(0,1));
		
		return perpVectors;
	}

	@Override
	public Vec2f project(Vec2f axis) {
		List<Edge> edges = getEdges();
		
		double min = axis.dot(edges.get(0).getStart());
		double max = min;
		
		for (int i = 1; i < edges.size(); i++) {
		  double p = axis.dot(edges.get(i).getStart());
		  if (p < min) {
		    min = p;
		  } else if (p > max) {
		    max = p;
		  }
		}	
		
		return new Vec2f((float)min, (float)max);
	}

	private List<Edge> getEdges() {
		List<Edge> edges = new ArrayList<Edge>();
		
		Edge e1 = new Edge(new Vec2f(position.x, position.y), new Vec2f(position.x, position.y + height));
		Edge e2 = new Edge(new Vec2f(position.x, position.y + height), new Vec2f(position.x + width, position.y + height));
		Edge e3 = new Edge(new Vec2f(position.x + width, position.y + height), new Vec2f(position.x + width, position.y));
		Edge e4 = new Edge(new Vec2f(position.x + width, position.y), new Vec2f(position.x, position.y));
		
		edges.add(e1);
		edges.add(e2);
		edges.add(e3);
		edges.add(e4);
		
		return edges;
	}
	
	@Override
	public void findCenter() {
		centerPoint = new Vec2f(position.x + width/2f, position.y + height/2f);
	}

	@Override
	public float checkRay(Vec2f ray, Vec2f origin) {
		List<Edge> edges = getEdges();
		Vec2f axis = ray.normalized();
		float toReturn = Float.MAX_VALUE;
		float min = Float.MAX_VALUE;
		
		for(Edge e : edges) {
			Vec2f a = e.getStart();
			Vec2f b = e.getEnd();
			
			Vec2f ap = a.minus(origin);
			Vec2f bp = b.minus(origin);
			
			float crossA = ap.cross(axis);
			float crossB = bp.cross(axis);
			
			if(crossA * crossB < 0) {
				Vec2f perp = e.makePerp().normalized();
				float t = perp.dot(bp) / axis.dot(perp); 
				
				if(t > 0 && t < min) {
					min = t;	
				}
			}
		}
		
		if(toReturn != min) {
			Vec2f projectPoint = axis.smult(min);
			Vec2f screenProjectPt = new Vec2f((projectPoint.x + origin.x), (projectPoint.y + origin.y));
			toReturn = screenProjectPt.dist(origin);
			
		} else {
			toReturn = 0;
		}
		
		return toReturn;
	}

}
