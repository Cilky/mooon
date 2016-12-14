package hchaoyidan.game.entity;

import hchaoyidan.engine.entity.CollisionShape;

public class Beam extends MPhysicsEntity {

	public Beam(CollisionShape s) {
		super(s);
		drawOrder = 4;
	}
	
	@Override
	public void doCollide(MPhysicsEntity other) {
		other.doCollideBeam(this);
	}
	

	@Override
	public void doCollidePlayer(Player player) {
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

	@Override
	public void doCollideObstacle(Obstacle o) {
		// TODO Auto-generated method stub
		
	}


}
