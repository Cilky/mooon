package hchaoyidan.game;

import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.engine.entity.PhysicEntity;

/**
 * The physic entity for Tou
 * @author yidanzeng
 *
 */
public abstract class MPhysicEntity extends PhysicEntity<MPhysicEntity>{

	public MWorld world;
	/**
	 * Constructor for TouPhysicEntity
	 * @param s
	 */
	public MPhysicEntity(CollisionShape s) {
		super(s);
	}

	@Override
	public void doCollide(MPhysicEntity other) {
	}
	
	/**
	 * Carries out action if this entity collides with a Player entity
	 * @param player
	 */
	public abstract void doCollidePlayer(Player player);
	
	/**
	 * Carries out action if this entity collides with an Enemy entity
	 * @param enemy
	 */
    public abstract void doCollideEnemy(SlowEnemy enemy);
    
    /**
	 * Carries out action if this entity collides with a Ground entity
	 * @param ground
	 */
    public abstract void doCollideGround(Ground ground);

	/**
	 * Carries out action if this entity collides with a Test entity
	 * @param test
	 */
    public abstract void doCollideTest(TestEntity test);
    
    /**
	 * Carries out action if this entity collides with a Test entity
	 * @param test
	 */
    public abstract void doCollideGrenade(Grenade g);
    


}
