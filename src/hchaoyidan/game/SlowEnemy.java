package hchaoyidan.game;

import java.awt.Color;
import java.util.Random;

import hchaoyidan.engine.entity.Collision;
import hchaoyidan.engine.entity.CollisionAAB;
import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.entity.CollisionShape;
import starter.Vec2f;
import starter.Vec2i;

/**
 * Enemy entity within the game
 * @author yidanzeng
 *
 */
public class SlowEnemy extends MPhysicEntity {

	public CollisionShape parent;
	public Player lastPlayer;

	/**
	 * Constructor for enemy
	 * @param x
	 * @param y
	 * @param parent
	 * @param world
	 */
	public SlowEnemy(int x, int y, CollisionShape parent, MWorld world, int size) {
		super(new CollisionCircle(Color.GREEN, new Vec2f(x + 5, y), parent, size));
		this.parent = parent;
		this.type = "enemy";
		drawOrder = 2;
		this.world = world;
		this.restitution = 0.5f;
	}
	
	public SlowEnemy(CollisionShape shape, MWorld world) {
		super(shape);
		this.parent = (CollisionShape) shape.parent;
		this.type = "enemy";
		drawOrder = 2;
		this.world = world;
		
	}

	@Override
	public void doCollide(MPhysicEntity other) {
		other.doCollideEnemy(this);
	}
	
	@Override
	public void doCollidePlayer(Player player) {
		if(lastPlayer == null) {
			player.health = player.health - 10;
			
		} else if(!lastPlayer.equals(player)) {
			player.health = player.health - 10;
			
		}
		
		lastPlayer = player;
		
	}

	@Override
	public void doCollideEnemy(SlowEnemy enemy) {
		// nothing happens
		
	}
	
	@Override
	public void doCollideTest(TestEntity test) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doCollideGround(Ground ground) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doCollideGrenade(Grenade g) {
		g.explode();
		
	}

}
