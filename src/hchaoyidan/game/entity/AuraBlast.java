package hchaoyidan.game.entity;

import java.awt.Graphics2D;
import java.io.File;
import java.io.Serializable;

import hchaoyidan.engine.entity.Collision;
import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.engine.entity.PhysicsEntity;
import hchaoyidan.engine.sound.SoundPlayer;
import hchaoyidan.game.MWorld;
import starter.Vec2f;

public class AuraBlast extends MPhysicsEntity implements Serializable {
	private CollisionShape parent;
	private int destroyCountDown = 20;
	
	public AuraBlast(CollisionShape shape, CollisionShape parent, MWorld world) {
		super(shape);
		this.parent = parent;
		this.world = world;
		drawOrder = 2;
		this.type = "blast";
	}

	@Override
	public void onCollide(Collision collision) {
		
	}
	
	@Override
	public void applyForce(Vec2f f) {
	}
	
	@Override
	public void applyImpulse(Vec2f f) {
	}
	
	@Override 
	public void onTick(long nanosSincePreviousTick) {
		destroyCountDown--;
		if (destroyCountDown <= 0) {
			setDestroyed(true);
		}
	}
	
	@Override 
	public void onDraw(Graphics2D g) {
		super.onDraw(g);
	}
	
	@Override
	public void doCollide(MPhysicsEntity other) {
		other.doCollideAuraBlast(this);
	}
	
	@Override
	public void doCollidePlayer(Player player) {
		
	}

	@Override
	public void doCollideFishEnemy(FishEnemy fe) {
		fe.setDestroyed(true);
	}

	@Override
	public void doCollideBirdEnemy(BirdEnemy be) {
		be.setDestroyed(true);
		
	}

	@Override
	public void doCollideStarEnemy(StarEnemy se) {
		se.setDestroyed(true);
		
	}

	@Override
	public void doCollideBeam(Beam b) {
		// TODO Auto-generated method stub
		
	}
	
	public void doCollideAuraBlast(AuraBlast ab) {
		
	}

	@Override
	public void doCollideObstacle(Obstacle o) {
		// TODO Auto-generated method stub
		
	}
}
