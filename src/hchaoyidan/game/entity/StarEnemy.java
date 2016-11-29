package hchaoyidan.game.entity;

import java.io.Serializable;

import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.game.MWorld;

public class StarEnemy extends Enemy implements Serializable {

	public StarEnemy(CollisionShape shape, MWorld world) {
		super(shape, world);
		this.type = "star";
	}

	@Override
	public void doCollide(MPhysicsEntity other) {
		other.doCollideStarEnemy(this);
	}
	
}
