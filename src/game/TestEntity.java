package game;

import java.awt.Color;
import java.awt.Graphics2D;

import engine.entity.Collision;
import engine.entity.CollisionShape;
import starter.Vec2f;

/**
 * Test entities for debugger
 * @author yidanzeng
 *
 */
public class TestEntity extends MPhysicEntity{

	
	public TestEntity(CollisionShape s) {
		super(s);
	}

	@Override
	public void doCollide(MPhysicEntity other) {
		other.doCollideTest(this);
	}
	
	@Override
	public void doCollideTest(TestEntity test) {
		test.getShape().showMtv = true;
		shape.showMtv = true;
	}
	
	@Override
	public void doCollidePlayer(Player player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void doCollideEnemy(SlowEnemy enemy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onDraw(Graphics2D g) {
		if(!delete) {
			shape.onDraw(g);
		}
	}

	@Override
	public void doCollideGround(Ground ground) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTick(long nanosSincePreviousTick) {
	}
	
	@Override
	public void onCollide(Collision<MPhysicEntity> collision) {
		shape.mtv = collision.mtv;
	}

	@Override
	public void doCollideGrenade(Grenade g) {
		// TODO Auto-generated method stub
		
	}
	
}
