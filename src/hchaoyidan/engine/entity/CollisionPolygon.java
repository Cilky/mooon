package hchaoyidan.engine.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import hchaoyidan.engine.Edge;
import hchaoyidan.engine.Shape;
import starter.Vec2f;
import starter.Vec2i;

public class CollisionPolygon extends CollisionShape {

	public List<Edge> edges;
	
	public CollisionPolygon(Color color, Shape parent, List<Edge> edges) {
		super(color, null, parent, "polygon", null); 
		this.edges = edges;
		
		// figure out width and height and position
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;

		for(int i = 0; i < edges.size(); i++) {
		    double x = edges.get(i).getStart().x;
		    double y = edges.get(i).getStart().y;
		    
		    minX = Math.min(minX, x);
		    maxX = Math.max(maxX, x);
		    minY = Math.min(minY, y);
		    maxY = Math.max(maxY, y);    
		}

		width = (int) maxX - (int) minX;
		height = (int) maxY - (int) minY;
		position = new Vec2f((float)minX, (float)minY);
		
		findCenter();
	}

	@Override
	public void move(float deltaX, float deltaY) {
		List<Edge> newEdges = new ArrayList<Edge>();
		
		for(int i = 0; i < edges.size(); i++) {
			Vec2f newStart = new Vec2f(edges.get(i).getStart().x + deltaX, edges.get(i).getStart().y + deltaY);
			Vec2f newEnd = new Vec2f(edges.get(i).getEnd().x + deltaX, edges.get(i).getEnd().y + deltaY);
			Edge newEdge = new Edge(newStart, newEnd);
			newEdges.add(newEdge);
		}
		
		edges = newEdges;
		
		position = new Vec2f(position.x + deltaX, position.y + deltaY);
		findCenter();
	}
	
	
	@Override
	public Vec2f collides(CollisionShape o) {
		return o.collidesPoly(this);
	}

	@Override
	public Vec2f collidesCircle(CollisionCircle c) {
		List<Vec2f> allPerp = new ArrayList<Vec2f>();
		allPerp.addAll(getPerpVectors());
		allPerp.add(c.getCircleAxis(this));
		
		float minMagnitude = Float.POSITIVE_INFINITY;
		Vec2f mtv = new Vec2f(0,0);
		for(Vec2f axis : allPerp) {
			float mtv1d = mtv(project(axis), c.project(axis));
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
		
		return mtv;		
	}
	
	@Override
	public Vec2f project(Vec2f axis) {
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


	@Override
	public void onResize(Vec2i newsize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDraw(Graphics2D g) {
		g.setColor(color);
		
		int nPoints = edges.size();
		int[] x = new int[nPoints];
		int[] y = new int[nPoints];
		
		for(int i = 0; i < nPoints; i++) {
			Vec2f vec = edges.get(i).getStart();
			x[i] = (int) vec.x;
			y[i] = (int) vec.y;
		}
		
		g.fillPolygon(x, y, nPoints);
		
		if(showMtv) {
			drawMtv(g);
		}
	}

	@Override
	public boolean isWithin(Vec2i mouse) {
		boolean isInside = true;
		
		for(int i = 0; i < edges.size(); i++) {
			if(!isInside) {
				break;
			} else {
				Edge currEdge = edges.get(i);
				Edge newEdge = new Edge(currEdge.getStart(), new Vec2f(mouse.x, mouse.y));
				
				float result = currEdge.cross(newEdge);
				
				if(result < 0) {
					isInside = false;
				}
			}
		}
		
		return isInside;
	}

	@Override
	public List<Vec2f> getPerpVectors() {
		ArrayList<Vec2f> perpVectors = new ArrayList<Vec2f>();
		
		for(int i = 0; i < edges.size(); i++) {
			Edge currVer = edges.get(i);
			Vec2f currPerp = currVer.makePerp();
			perpVectors.add(currPerp.normalized());
		}
		
		return perpVectors;
	}

	@Override
	public void findCenter() {
		centerPoint = new Vec2f(position.x + width/2f, position.y + height/2f);
	}

	@Override
	public float checkRay(Vec2f ray, Vec2f origin) {
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
