package hchaoyidan.game.entity;

import java.awt.Color;
import java.io.Serializable;
import java.util.Random;

import hchaoyidan.engine.entity.Collision;
import hchaoyidan.engine.entity.CollisionAAB;
import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.game.MWorld;
import starter.Vec2f;
import starter.Vec2i;

/**
 * Enemy entity within the game
 * @author yidanzeng
 *
 */
public class Enemy extends MPhysicsEntity implements Serializable {

	public CollisionShape parent;
	protected int countdown;
	protected Player lastPlayer;

	/**
	 * Constructor for enemy
	 * @param x
	 * @param y
	 * @param parent
	 * @param world
	 */
	
	public Enemy(CollisionShape shape, MWorld world) {
		super(shape);
		this.parent = (CollisionShape) shape.parent;
		this.type = "enemy";
		drawOrder = 2;
		this.world = world;
		this.restitution = 0.5f;
		this.countdown = 50 + (int)(Math.random() * ((250 - 50) + 1));
	}

	@Override
	public void doCollide(MPhysicsEntity other) {
	}
	
	@Override
	public void doCollidePlayer(Player player) {
	}

	@Override
	public void doCollideGround(Ground ground) {
		// TODO Auto-generated method stub
		
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
