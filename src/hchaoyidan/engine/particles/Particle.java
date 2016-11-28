package hchaoyidan.engine.particles;

import java.awt.Color;
import java.awt.Graphics2D;

import hchaoyidan.engine.entity.CollisionShape;
import starter.Vec2f;

public class Particle {
	Vec2f position;
	Vec2f velocity;
	boolean destroy = false;
	CollisionShape shape;
	Color color;
	int alpha = 1;
	
	
	public Particle(Vec2f position) {
		this.position = position;
		color = new Color(255,255,255, alpha);
	}
	
	public void onTick(long nanosSincePreviousTick) {	
		position.plus(velocity);
	}

	public void onDraw(Graphics2D g) {
		if (!destroy) {
		shape.onDraw(g);
		}
	}
	
	public void destroy() {
		destroy = true;
	}

	public Vec2f getPosition() {
		return position;
	}

	public void setPosition(Vec2f position) {
		this.position = position;
	}

	public Vec2f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vec2f velocity) {
		this.velocity = velocity;
	}

	public boolean isDestroy() {
		return destroy;
	}

	public void setDestroy(boolean destroy) {
		this.destroy = destroy;
	}
}
