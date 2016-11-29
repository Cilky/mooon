package hchaoyidan.game.entity;

import java.io.Serializable;

import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.game.MWorld;

public class BirdEnemy extends Enemy implements Serializable {

	public BirdEnemy(CollisionShape shape, MWorld world) {
		super(shape, world);
		this.type = "bird";
	}

	@Override
	public void doCollide(MPhysicsEntity other) {
		other.doCollideBirdEnemy(this);
	}
	
	
}
