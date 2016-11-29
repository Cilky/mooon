package hchaoyidan.engine.particles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.entity.CollisionShape;
import starter.Vec2f;

public class Particle implements Serializable {
	Vec2f position;
	Vec2f velocity = new Vec2f(0,0);
	boolean fade;
	boolean destroy = false;
	CollisionShape shape;
	int startAlpha = 255;
	int currAlpha = 255;
	
	public Particle(Vec2f position) {
		this.position = position;
	}
	
	public Particle(Vec2f position, CollisionCircle circle) {
		this.position = position;
		this.shape = circle;
	}
	
	public void update() {	
		setPosition(position.plus(velocity));
		shape.move(velocity.x, velocity.y);
	}

	public void onDraw(Graphics2D g) {
		shape.onDraw(g);
		if (fade) {
			fadeOut();
		}
	}
	
	public void fadeOut() {
		currAlpha = currAlpha - 9;
		if (currAlpha < 0) {
			currAlpha = 0;
		}
		shape.changeColor(new Color(shape.getColor().getRed(), 
								shape.getColor().getGreen(), 
								shape.getColor().getBlue(), 
							  currAlpha));
		if (startAlpha - currAlpha == startAlpha) {
			destroy = true;
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

	public CollisionShape getShape() {
		return shape;
	}

	public void setShape(CollisionShape shape) {
		this.shape = shape;
	}

	public int getStartAlpha() {
		return startAlpha;
	}

	public void setAlpha(int alpha) {
		this.startAlpha = alpha;
	}

	public boolean isFade() {
		return fade;
	}

	public void setFade(boolean fade) {
		this.fade = fade;
	}
	
	
	
	
}
