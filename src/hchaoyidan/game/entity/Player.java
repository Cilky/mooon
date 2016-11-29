package hchaoyidan.game.entity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import hchaoyidan.engine.Pair;
import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.engine.sound.SoundPlayer;
import hchaoyidan.game.MWorld;
import starter.Vec2f;

/**
 * Player representation in game
 * @author yidanzeng
 *
 */
public class Player extends MPhysicsEntity implements Serializable {

	public int health = 100;
	private CollisionShape parent;
	public MPhysicsEntity lastEnemy;
	private SoundPlayer sound;
	
	/**
	 * Constructor for player
	 * @param x
	 * @param y
	 * @param parent
	 * @param world
	 */
	public Player(CollisionShape shape, CollisionShape parent, MWorld world) {
		super(shape);
		this.parent = parent;
		this.type = "player";
		this.world = world;
		drawOrder = 3;
		this.restitution = 0.0f;
		this.sound = new SoundPlayer(new File("sounds/hit.wav"), false);
		
	}
	
	@Override
	public void move(float x, float y) {
		if(shape.position.x + x < 0 || shape.position.x + x + shape.getWidth() > parent.getWidth()) {
			x = 0;
		}
		if(shape.position.y + y < 0 || shape.position.y + y + shape.getHeight() > parent.getHeight()) {
			y = 0;
		}

		shape.move(-x, -y);
	}
	
	public void jump() {
		if(isColliding) {
			applyImpulse(new Vec2f(0, -800));
		}
		
		isColliding = false;
	}
	
	public void shoot(Vec2f mousePos) {
		System.out.println("shoot");
		Vec2f vec = new Vec2f(mousePos.x - shape.centerPoint.x, mousePos.y - shape.centerPoint.y);
		Pair<MPhysicsEntity, Float> hit = world.rayCast(vec, this);
		MPhysicsEntity entity = hit.getK();
		float distance = hit.getV();
		
		if(entity != null && distance > 0) {
			entity.applyImpulse(vec.smult(400/distance));
		}
		
	}
	
	@Override
	public void doCollide(MPhysicsEntity other) {
		other.doCollidePlayer(this);
	}
	
	
	@Override
	public void onTick(long nanosSincePreviousTick) {
		float t = nanosSincePreviousTick / 1_000_000_000f;
		
		vel = vel.plus(force.smult(t).sdiv(mass)).plus((impulse).sdiv(mass));
		shape.move(t*vel.x, t*vel.y);
		impulse = new Vec2f(0,0);
		force = new Vec2f(0,0);
		
		isColliding = false;
	}


	
	@Override
	public void doCollidePlayer(Player player) {
		// nothing happens because only one player
	}


	@Override
	public void doCollideGround(Ground ground) {
		// TODO Auto-generated method stub
	}

	public SoundPlayer getSound() {
		return sound;
	}

	public void setSound(SoundPlayer sound) {
		this.sound = sound;
	}

	@Override
	public void doCollideFishEnemy(FishEnemy fe) {
		if(lastEnemy == null) {
			health = health - 10;
		} else if(!lastEnemy.equals(fe)) {
			health = health - 10;
		}
		
		lastEnemy = fe;
		// checking for completion of sound
		if(world.soundToggled) {
			sound.run();
		}
		
	}

	@Override
	public void doCollideBirdEnemy(BirdEnemy be) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doCollideStarEnemy(StarEnemy se) {
		// TODO Auto-generated method stub
		
	}


	
}
