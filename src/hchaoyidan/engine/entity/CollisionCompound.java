package hchaoyidan.engine.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import starter.Vec2f;
import starter.Vec2i;

/**
 * Compound collision shape, aka shape that combines other shapes
 * @author yidanzeng
 *
 */
public class CollisionCompound extends CollisionShape{

	protected ArrayList<CollisionShape> otherShapes;
	
	/**
	 * Constructor for a compound shape
	 * @param color
	 * @param position
	 * @param parent
	 * @param shapes
	 */
	public CollisionCompound(Vec2f position, CollisionShape parent, CollisionShape ... shapes) {
		
		super(null, position, parent, "compound", new Vec2i(1,1));
		
		otherShapes = new ArrayList<CollisionShape>();
		
		// figure out width and height
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		
		for (int i=0; i < shapes.length; ++i) {
			otherShapes.add(shapes[i]);
			CollisionShape currShape = shapes[i];
			
			float x = currShape.getX();
			float y = currShape.getY();
			float width = currShape.getWidth();
			float height = currShape.getHeight();

		    minX = Math.min(minX, x);
		    maxX = Math.max(maxX, x + width);
		    minY = Math.min(minY, y);
		    maxY = Math.max(maxY, y + height); 
		}
		
		width = (int) maxX - (int) minX;
		height = (int) maxY - (int) minY;
	}

	/**
	 * Gets all the shapes within this compound shape
	 * @return
	 */
	public ArrayList<CollisionShape> getOtherShapes() {
		return otherShapes;
	}
	
	@Override
	public void move(float deltaX, float deltaY) {
		position = new Vec2f(position.x - deltaX, position.y - deltaY);
		
		findPosRatios();
		
		for(CollisionShape s : otherShapes) {
			s.move(deltaX, deltaY);
		}
	}
	
	@Override
	public void onResize(Vec2i newsize) {
		for(CollisionShape s : otherShapes) {
			s.onResize(newsize);
		}
		
	}

	@Override
	public void onDraw(Graphics2D g) {
		for(CollisionShape s : otherShapes) {
			s.onDraw(g);
		}
	}

	@Override
	public boolean isWithin(Vec2i mouse) {
		boolean isWithin = false;
		for(CollisionShape s: otherShapes) {
			if(s.isWithin(mouse)) {
				isWithin = true;
			}
		}
		
		return isWithin;
	}

	@Override
	public Vec2f collides(CollisionShape o) {
		return o.collidesCompound(this);
	}

	@Override
	public Vec2f collidesCircle(CollisionCircle c) {
		boolean collided = false;
		for(CollisionShape s : otherShapes) {
			
		}
		
		return null;
	}

	@Override
	public Vec2f collidesAAB(CollisionAAB aab) {
		boolean collided = false;
		for(CollisionShape s : otherShapes) {
			
		}
		
		return null;
	}

	@Override
	public Vec2f collidesCompound(CollisionCompound comp) {
		boolean collided = false;
		for(CollisionShape s : otherShapes) {
			for(CollisionShape k : comp.getOtherShapes()) {
				
			}
		}
		
		return null;
	}
	
	@Override
	public Vec2f collidesPoly(CollisionPolygon m) {
		boolean isColliding = false;
		
		for(CollisionShape c : otherShapes) {
		}
		
		return null;
	}
	
	@Override
	public void changeColor(Color color) {
		for(CollisionShape s : otherShapes) {
			s.changeColor(color);
		}
	}

	public List<Vec2f> getCompAxis(CollisionPolygon m) {
		List<Vec2f> allPerp = new ArrayList<Vec2f>();
		for(CollisionShape s : otherShapes) {
			if(s.type.equals("circle")) {
				CollisionCircle c = (CollisionCircle) s;
				allPerp.add(c.getCircleAxis(m));
			} else {
				allPerp.addAll(s.getPerpVectors());
			}
			
		}
		
		return allPerp;
	}
	
	@Override
	public List<Vec2f> getPerpVectors() {
		return null;
	}
	
	@Override
	public Vec2f project(Vec2f axis) {
		return null;
	}

	@Override
	public void findCenter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float checkRay(Vec2f ray, Vec2f origin) {
		// TODO Auto-generated method stub
		return 0;
	}
}
