package hchaoyidan.game.entity;

import java.awt.Color;

import hchaoyidan.engine.entity.CollisionAAB;
import hchaoyidan.engine.entity.CollisionShape;
import starter.Vec2f;
import starter.Vec2i;

public class Ground extends MPhysicsEntity {

	public Ground(int x, int y, CollisionShape parent, Vec2i size) {
		super(new CollisionAAB(Color.GRAY, new Vec2f(x, y), parent, size));
		isStatic = true;
		this.type = "ground";
		this.restitution = 1f;
	}

	@Override
	public void doCollidePlayer(Player player) {
		// TODO Auto-generated method stub
		
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

	public void doCollideAuraBlast(AuraBlast ab) {
		
	}
}
