package engine;

import starter.Vec2f;

public class Edge {

	private Vec2f start;
	private Vec2f end;
	
	public Edge(Vec2f start, Vec2f end) {
		this.start = start;
		this.end = end;
	}
	
	public float getDistance(){
		return start.dist(end);
	}
	
	public Vec2f getStart() {
		return start;
	}
	
	public Vec2f getEnd() {
		return end;
	}
	
	/**
	 * Returns the cross product of this edge with another edge
	 * @param other
	 * @return
	 */
	public final float cross(Edge other) {
		Vec2f currVec = makeVector(start, end);
		Vec2f newVec = makeVector(other.getStart(), other.getEnd());
		
		float result = currVec.cross(newVec);
		
		return result;
	}
	
	public Vec2f makeVector(Vec2f start, Vec2f end) {
		return new Vec2f(end.x - start.x, end.y - start.y);
	}
	
	/**
	 * Returns the perpendicular form of this edge
	 * @return
	 */
	public Vec2f makePerp() {
		Vec2f vec = new Vec2f(end.x - start.x, end.y - start.y);
		
		return new Vec2f(-(vec.y), vec.x);
	}
	
	@Override
	public String toString() {
		return "[" + start.toString() + ", " + end.toString() + "]";
	}
}

