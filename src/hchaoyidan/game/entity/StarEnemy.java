package hchaoyidan.game.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hchaoyidan.engine.Edge;
import hchaoyidan.engine.entity.CollisionPolygon;
import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.game.MWorld;
import starter.Vec2f;

public class StarEnemy extends Enemy implements Serializable {

	private int shootTimer = 200;
	private Vec2f shootVec = new Vec2f(0,0);
	public StarEnemy(CollisionShape shape, MWorld world) {
		super(shape, world);
		this.type = "star";
	}

	@Override
	public void doCollide(MPhysicsEntity other) {
		other.doCollideStarEnemy(this);
	}
	
	public void shoot(Vec2f vec) {
		
		System.out.println("shoootting");
		// checking which edge to shoot from
		CollisionPolygon poly = (CollisionPolygon) getShape();
		List<Edge> e = poly.getEdges();
		
		Vec2f pos = e.get(2).getStart().plus(new Vec2f(3, 0));
		Vec2f end = vec.smult(15);
		Vec2f otherPos = pos.plus(new Vec2f(-3,0));
		
		Edge v1 = new Edge(pos, pos.plus(end));
		Edge v2 = new Edge(pos.plus(end), otherPos.plus(end));
		Edge v3 = new Edge(otherPos.plus(end), otherPos);
		Edge v4 = new Edge(otherPos, pos);
		
		ArrayList<Edge> list = new ArrayList<Edge>();
		list.add(v1);
		list.add(v2);
		list.add(v3);
		list.add(v4);
		
		Beam b = new Beam(new CollisionPolygon(Color.PINK, world.getBackground(), list));
		b.applyImpulse(vec.smult(50));
		
		world.newPhyEnt.add(b);
	}
	
	@Override
	public void onTick(long nanosSincePreviousTick) {
		
		
		float t = nanosSincePreviousTick / 1_000_000_000f;
		
		vel = vel.plus(force.smult(t).sdiv(mass)).plus((impulse).sdiv(mass));
		shape.move(t*vel.x, t*vel.y);
		impulse = new Vec2f(0,0);
		force = new Vec2f(0,0);
		
		shootTimer--;
		countdown--;
		
		CollisionPolygon poly = (CollisionPolygon) getShape();
		List<Edge> e = poly.getEdges();
		Vec2f topWantLeft = e.get(0).getVec();
		Vec2f topWantRight = e.get(1).makeVector(e.get(1).getEnd(), e.get(1).getStart());
		
		Vec2f playerPos = world.getPlayer().getPosition();
		Vec2f playVector = playerPos.minus(e.get(2).getStart()).normalized();
		
		float left = topWantLeft.normalized().cross(playVector.normalized());
		float right = topWantRight.normalized().cross(playVector.normalized());
		shootVec = playVector;
		
		if(left < 0 && right > 0) {
			Vec2f shapePos = getShape().getPosition();
			if(shootTimer == 0 && shapePos.x > 100 && shapePos.x < world.worldSize.x - 100) {
				shoot(playVector);
				shootTimer = 100 + (int)(Math.random() * ((200 - 100) + 1));
			} else if(shootTimer == 0) {
				shootTimer = 100 + (int)(Math.random() * ((200 - 100) + 1));
			}
		}
		
		if(countdown == 0) {
			int impulse = 400;
			applyImpulse(playVector.smult(impulse));
			countdown = 50 + (int)(Math.random() * ((150 - 50) + 1));
		}
		
		checkPosition();
/*		if(shape.position.x > world.worldSize.x || 
				shape.position.y > world.worldSize.y || 
				shape.position.x < -100 || 
				shape.position.x < -100 ) {
			vel = new Vec2f(0,0);
		} */
	}
	
}
