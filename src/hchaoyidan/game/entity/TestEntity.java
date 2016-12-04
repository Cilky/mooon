package hchaoyidan.game.entity;

import java.awt.Color;
import java.awt.Graphics2D;

import hchaoyidan.engine.entity.Collision;
import hchaoyidan.engine.entity.CollisionShape;
import starter.Vec2f;

/**
 * Test entities for debugger
 * @author yidanzeng
 *
 */
public class TestEntity extends MPhysicsEntity{

	
	public TestEntity(CollisionShape s) {
		super(s);
	}

	@Override
	public void doCollide(MPhysicsEntity other) {

	}
	
	@Override
	public void doCollidePlayer(Player player) {
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
	public void onCollide(Collision<MPhysicsEntity> collision) {
		shape.mtv = collision.mtv;
	}

	@Override
	public void doCollideFishEnemy(FishEnemy fe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doCollideBirdEnemy(BirdEnemy be) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doCollideStarEnemy(StarEnemy se) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doCollideBeam(Beam b) {
		// TODO Auto-generated method stub
		
	}

	
}
