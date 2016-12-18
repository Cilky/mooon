package hchaoyidan.game.entity;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import hchaoyidan.engine.Pair;
import hchaoyidan.engine.entity.Collision;
import hchaoyidan.engine.entity.CollisionCircle;
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
	public SoundPlayer fishHit;
	public SoundPlayer birdHit;
	public SoundPlayer starHit;
	public long timeStamp;
	private Vec2f reset;
	
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
		this.birdHit = new SoundPlayer(new File("sounds/hit.wav"), false);
		this.fishHit = new SoundPlayer(new File("sounds/smallbell.wav"), false);
		this.starHit = new SoundPlayer(new File("sounds/starhit.wav"), false);
		this.timeStamp = System.currentTimeMillis();
		this.reset = shape.getPosition();
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
	
	@Override
	public void onCollide(Collision collision) {
		if (!collision.other.type.equals("blast")) {
			super.onCollide(collision);
		} 
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
		
		if(shape.position.x >= world.worldSize.x - shape.getWidth()) {
			shape.position = new Vec2f(world.worldSize.x - shape.getWidth(), shape.position.y);
		}
		if(shape.position.x <= 0) {
			shape.position = new Vec2f(0, shape.position.y);
		} 
		if(shape.position.y >= world.worldSize.y - shape.getHeight()) {
			shape.position = new Vec2f(shape.position.x, world.worldSize.y - shape.getHeight());
		} 
		if(shape.position.y <= 0) {
			shape.position = new Vec2f(shape.position.x, 0);
		}
		
		
		float mult = 1;

		switch(world.environ) {
			case WATER:
				mult = 0.97f;
				break;
			case AIR:
				mult = 0.98f;
				break;
			case SPACE:
				mult = 0.99f;
				break;
		}
		
		
		float t = nanosSincePreviousTick / 1_000_000_000f;
		
		vel = vel.plus(force.smult(t).sdiv(mass)).plus((impulse).sdiv(mass));
		vel = vel.smult(mult);
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
	public void doCollideFishEnemy(FishEnemy fe) {
		long now = System.currentTimeMillis();
		
		if(now - timeStamp > 1000){
			//System.out.println("player collided with " + fe.type);
			int highscore = world.getHighScoreInt() - 15;
			
			CollisionCircle shape = new CollisionCircle(Color.WHITE, getShape().getPosition(), world.getBackground(),
					(int)(getShape().getWidth() - 2));
			setShape(shape);
			world.setHighScoreInt(highscore);
			
			if(world.soundToggled) {
				if (fishHit != null) {
				fishHit.run();
				}
			}
			
			timeStamp = now;
		}
		
	}

	@Override
	public void doCollideBirdEnemy(BirdEnemy be) {
		long now = System.currentTimeMillis();
		
		if(now - timeStamp > 1000){
			//System.out.println("player collided with " + be.type);
			int highscore = world.getHighScoreInt() - 20;
			
			CollisionCircle shape = new CollisionCircle(Color.WHITE, getShape().getPosition(), world.getBackground(),
					(int)(getShape().getWidth() - 3));
			setShape(shape);
			world.setHighScoreInt(highscore);
			
			if(world.soundToggled) {
				if (birdHit != null) {
				birdHit.run();
				}
			}
			timeStamp = now;
		}
	}

	@Override
	public void doCollideStarEnemy(StarEnemy se) {
		long now = System.currentTimeMillis();
		
		if(now - timeStamp > 1000){
			//System.out.println("player collided with " + se.type);
			int highscore = world.getHighScoreInt() - 25;
			
			CollisionCircle shape = new CollisionCircle(Color.WHITE, getShape().getPosition(), world.getBackground(),
					(int)(getShape().getWidth() - 4));
			setShape(shape);
			world.setHighScoreInt(highscore);
			
			if(world.soundToggled) {
				if (starHit != null) {
				starHit.run();
				}
			}
			timeStamp = now;
		}
		
	}

	public Vec2f getPosition() {
		return shape.getPosition();
	}
	
	public void setPosition(Vec2f position) {
		 shape.setPosition(position);
	}
	
	public void reset() {
		shape.position = reset;
	}

	@Override
	public void doCollideBeam(Beam b) {
		int highscore = world.getHighScoreInt() - 10;
		
		CollisionCircle shape = new CollisionCircle(Color.WHITE, getShape().getPosition(), world.getBackground(),
				(int)(getShape().getWidth() - 4));
		setShape(shape);
		world.setHighScoreInt(highscore);
		b.delete = true;

	}

	@Override

	public void doCollideObstacle(Obstacle o) {
		// TODO Auto-generated method stub

	}
	
	public void doCollideAuraBlast(AuraBlast ab) {
		// TODO Auto-generated method stub
		applyForce(new Vec2f(0, 0));
		applyImpulse(new Vec2f(0, 0));
		
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public SoundPlayer getFishHit() {
		return fishHit;
	}

	public void setFishHit(SoundPlayer fishHit) {
		this.fishHit = fishHit;
	}

	public SoundPlayer getBirdHit() {
		return birdHit;
	}

	public void setBirdHit(SoundPlayer birdHit) {
		this.birdHit = birdHit;
	}

	public SoundPlayer getStarHit() {
		return starHit;
	}

	public void setStarHit(SoundPlayer starHit) {
		this.starHit = starHit;
	}
	
	
	
	
	
}
