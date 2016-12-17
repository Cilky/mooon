package hchaoyidan.game.entity;

import hchaoyidan.engine.entity.CollisionPolygon;


public class Obstacle extends MPhysicsEntity {

	public Obstacle(CollisionPolygon poly) {
		super(poly);
		this.type = "obstacle";
		this.isStatic = true;
		this.restitution = 1f;
		this.drawOrder = 4;
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
	public void doCollideObstacle(Obstacle o) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void doCollideBeam(Beam b) {
		b.delete = true;
	}

	public void doCollideAuraBlast(AuraBlast ab) {
	}
}
