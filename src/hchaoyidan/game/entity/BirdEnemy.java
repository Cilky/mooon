package hchaoyidan.game.entity;

import java.io.Serializable;

import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.game.MWorld;
import starter.Vec2f;

public class BirdEnemy extends Enemy implements Serializable {
	
	public BirdEnemy(CollisionShape shape, MWorld world) {
		super(shape, world);
		this.type = "bird";
	}

	@Override
	public void doCollide(MPhysicsEntity other) {
		other.doCollideBirdEnemy(this);
	}
	
	@Override
	public void onTick(long nanosSincePreviousTick) {
		float t = nanosSincePreviousTick / 1_000_000_000f;
		
		vel = vel.plus(force.smult(t).sdiv(mass)).plus((impulse).sdiv(mass));
		shape.move(t*vel.x, t*vel.y);
		impulse = new Vec2f(0,0);
		force = new Vec2f(0,0);
		
		countdown--;
		Vec2f playerPos = world.getPlayer().getPosition();
		Vec2f vec = playerPos.minus(shape.getPosition()).normalized();
		
		if(countdown == 0) {
			int impulse = 100;
			applyImpulse(vec.smult(impulse));
			countdown = 50 + (int)(Math.random() * ((250 - 50) + 1));
		}
		
		if(shape.position.x > world.windowSize.x || 
				shape.position.y > world.windowSize.y || 
				shape.position.x < -100 || 
				shape.position.x < -100 ) {
			vel = new Vec2f(0,0);
		} 

		
	}
}
