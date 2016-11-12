package hchaoyidan.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import hchaoyidan.engine.Pair;
import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.entity.CollisionShape;
import starter.Vec2f;


/**
 * Enemy entity within the game
 * @author yidanzeng
 *
 */
public class Grenade extends MPhysicEntity {

	public CollisionShape parent;
	public Player lastPlayer;
	public MWorld world;

	/**
	 * Constructor for enemy
	 * @param x
	 * @param y
	 * @param parent
	 * @param world
	 */
	public Grenade(int x, int y, CollisionShape parent, MWorld world) {
		super(new CollisionCircle(Color.RED, new Vec2f(x - 10, y - 30), parent, 15));
		this.parent = parent;
		this.type = "enemy";
		drawOrder = 2;
		this.world = world;
		this.restitution = 0f;
	}

	@Override
	public void doCollide(MPhysicEntity other) {
		other.doCollideGrenade(this);
	}
	
	@Override
	public void doCollidePlayer(Player player) {
	}

	@Override
	public void doCollideEnemy(SlowEnemy enemy) {
		explode();
	}

	@Override
	public void doCollideGround(Ground ground) {
		explode();
	}

	@Override
	public void doCollideTest(TestEntity test) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void doCollideGrenade(Grenade g) {
		// TODO Auto-generated method stub
	}
	
	public void explode() {
		float limit = 200f;
		List<Pair<MPhysicEntity, Float>> entitiesInRange = new ArrayList<Pair<MPhysicEntity, Float>>();
		
		for(MPhysicEntity e : world.physicEntities) { // finding all the entities within range
//			System.out.println(shape.position);
			Vec2f ray = (e.getShape().centerPoint).minus(shape.position);
			Pair<MPhysicEntity, Float> entityHit = world.rayCast(ray, this);
			
			if(entityHit.getV() <= limit && entityHit.getV() != 0) {
				entitiesInRange.add(entityHit); 
			}
		}
		
		for(Pair<MPhysicEntity, Float> k : entitiesInRange) { // applyImpulse for those affected
			Vec2f vec = k.getK().getShape().centerPoint.minus(shape.position);
			k.getK().applyImpulse(vec.smult(100/k.getV()));
		}
		
		remove();
	}
}
