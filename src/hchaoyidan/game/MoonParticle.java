package hchaoyidan.game;

import java.awt.Color;

import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.particles.Particle;
import starter.Vec2f;

public class MoonParticle extends Particle {
	
	
	public MoonParticle(Vec2f position, CollisionCircle circle) {
		super(position, circle);
	}
	
	@Override
	public void update() {	
		double rand = Math.random();
		if (rand < .25) {
			setVelocity(new Vec2f(0, -.5f));
		} 
		if (rand >= .25 && rand < .5) {
			setVelocity(new Vec2f(0, .5f));
		} 
		if (rand >= .5 && rand < .75) {
			setVelocity(new Vec2f(-.5f, 0));
		} 
		if (rand >= .75 && rand <= 1) {
			setVelocity(new Vec2f(.5f, 0));
		} 
		super.update();
	}

}
