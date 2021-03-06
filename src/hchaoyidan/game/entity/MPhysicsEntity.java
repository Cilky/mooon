package hchaoyidan.game.entity;

import java.util.List;

import hchaoyidan.engine.PhysicsWorld;
import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.engine.entity.PhysicsEntity;
import hchaoyidan.game.MWorld;
import starter.Vec2f;

/**
 * The physic entity for Tou
 * @author yidanzeng
 *
 */
public abstract class MPhysicsEntity extends PhysicsEntity<MPhysicsEntity>{
	
	public MWorld world;
	/**
	 * Constructor for TouPhysicEntity
	 * @param s
	 */
	public MPhysicsEntity(CollisionShape s) {
		super(s);
	}

	@Override
	public void doCollide(MPhysicsEntity other) {
	}
	
	/**
	 * Carries out action if this entity collides with a Player entity
	 * @param player
	 */
	public abstract void doCollidePlayer(Player player);
	
	/**
	 * Carries out action if this entity collides with a FishEnemy entity
	 * @param enemy
	 */
    public abstract void doCollideFishEnemy(FishEnemy fe);
    
    /**
	 * Carries out action if this entity collides with a FishEnemy entity
	 * @param enemy
	 */
    public abstract void doCollideBirdEnemy(BirdEnemy be);
    /**
	 * Carries out action if this entity collides with a FishEnemy entity
	 * @param enemy
	 */
    public abstract void doCollideStarEnemy(StarEnemy se);

    /**
   	 * Carries out action if this entity collides with a Beam entity
   	 * @param beam
   	 */
    public abstract void doCollideBeam(Beam b);
    

    /**
   	 * Carries out action if this entity collides with an Obstacle entity
   	 * @param beam
   	 */
    public abstract void doCollideObstacle(Obstacle o);

    public abstract void doCollideAuraBlast(AuraBlast ab);

     
}
