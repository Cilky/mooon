package hchaoyidan.game.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;

import hchaoyidan.engine.Edge;
import hchaoyidan.engine.entity.CollisionPolygon;
import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.game.MWorld;
import starter.Vec2f;

public class FishEnemy extends Enemy implements Serializable {

	private boolean faceRight = true;
	private boolean direction = true;
	
	public FishEnemy(CollisionShape shape, MWorld world) {
		super(shape, world);
		this.type = "fish";
	}
	
	public void changeDir() {
		direction = !direction;
		int size = 40;
		float mid = size / 2f;
		Vec2f pos = new Vec2f(shape.getX(), shape.getY());
		ArrayList<Edge> list = new ArrayList<Edge>();
		
		if(direction) {
			Edge v1 = new Edge(new Vec2f(pos.x, pos.y), new Vec2f(pos.x + size, pos.y + mid));
			Edge v2 = new Edge(new Vec2f(pos.x + size, pos.y + mid), new Vec2f(pos.x, pos.y + size));
			Edge v3 = new Edge(new Vec2f(pos.x, pos.y + size), new Vec2f(pos.x, pos.y));
			
			list.add(v1);
			list.add(v2);
			list.add(v3);
			
		} else {
			Edge v1 = new Edge(new Vec2f(pos.x + size, pos.y), new Vec2f(pos.x, pos.y + mid));
			Edge v2 = new Edge(new Vec2f(pos.x, pos.y + mid), new Vec2f(pos.x + size, pos.y + size));
			Edge v3 = new Edge(new Vec2f(pos.x + size, pos.y + size), new Vec2f(pos.x + size, pos.y));
			
			list.add(v1);
			list.add(v2);
			list.add(v3);
		}
		
		// change shape
		shape = new CollisionPolygon(Color.RED, world.getBackground(), list);
	}
	
	@Override
	public void doCollide(MPhysicsEntity other) {
		other.doCollideFishEnemy(this);
	}
	
	@Override
	public void onTick(long nanosSincePreviousTick) {
		float t = nanosSincePreviousTick / 1_000_000_000f;
		
		vel = vel.plus(force.smult(t).sdiv(mass)).plus((impulse).sdiv(mass));
		shape.move(t*vel.x, t*vel.y);
		impulse = new Vec2f(0,0);
		force = new Vec2f(0,0);
		
		countdown--;
		if(countdown == 0) {
			int impulse = 400;
			if(!faceRight) {
				impulse = -400;
			}
			applyImpulse(new Vec2f(impulse, 0));
			countdown = 50 + (int)(Math.random() * ((250 - 50) + 1));
		}
		
		if(shape.position.x > world.windowSize.x) {
			faceRight = false;
			vel = new Vec2f(0,0);
			if(faceRight != direction) {
				changeDir();
				countdown = 50 + (int)(Math.random() * ((250 - 50) + 1));
			}
		} else if(shape.position.x <= -100){
			vel = new Vec2f(0,0);
			faceRight = true;
			if(faceRight != direction) {
				changeDir();
				countdown = 50 + (int)(Math.random() * ((250 - 50) + 1));
			}
		}
	}
	
}
