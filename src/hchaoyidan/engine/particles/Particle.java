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
	boolean destroy = false;
	CollisionShape shape;
	Color color;
	int alpha = 255;
	
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

	public CollisionShape getShape() {
		return shape;
	}

	public void setShape(CollisionShape shape) {
		this.shape = shape;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}
	
	
	
}
