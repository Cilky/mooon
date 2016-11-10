package hchaoyidan.engine.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import hchaoyidan.engine.Pair;
import hchaoyidan.engine.entity.Collision;
import hchaoyidan.engine.entity.CollisionAAB;
import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.entity.CollisionShape;
import starter.Vec2f;
import starter.Vec2i;

/**
 * Player representation in game
 * @author yidanzeng
 *
 */
public class Player extends MPhysicEntity {

	public int health = 100;
	private CollisionShape parent;
	public MPhysicEntity lastEnemy;
	
	
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
		Pair<MPhysicEntity, Float> hit = world.rayCast(vec, this);
		MPhysicEntity entity = hit.getK();
		float distance = hit.getV();
		
		if(entity != null && distance > 0) {
			entity.applyImpulse(vec.smult(400/distance));
		}
		
	}
	
	public void grenade(Vec2f mousePos) {
		System.out.println("grenade");
		Vec2f vec = new Vec2f(mousePos.x - shape.centerPoint.x, mousePos.y - shape.centerPoint.y);
		
		Grenade grenade = new Grenade((int)shape.getX(), (int)shape.getY(), this.parent, world);
		grenade.applyImpulse(vec.normalized().smult(70f));
		
		world.addPhysicEntity(grenade);
	}
	
	@Override
	public void doCollide(MPhysicEntity other) {
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
	public void doCollideEnemy(SlowEnemy enemy) {
		if(lastEnemy == null) {
			health = health - 10;
		} else if(!lastEnemy.equals(enemy)) {
			health = health - 10;
		}
		
		lastEnemy = enemy;
	}
	
	@Override
	public void doCollidePlayer(Player player) {
		// nothing happens because only one player
	}
	
	@Override
	public void doCollideTest(TestEntity test) {
		// nothing
	}

	@Override
	public void doCollideGround(Ground ground) {
		// TODO Auto-generated method stub
	}

	@Override
	public void doCollideGrenade(Grenade g) {
		
	}
	
}
