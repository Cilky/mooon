package hchaoyidan.engine.entity;

import java.io.Serializable;

import hchaoyidan.engine.Friction;
import starter.Vec2f;

/**
 * Collision struct
 * @author yidanzeng
 *
 * @param <T>
 */
public class Collision<T extends PhysicEntity<T>> implements Serializable{
	public final PhysicEntity<T> other; 
	public final Vec2f mtv; 
	public final Friction friction;
	
	public Collision(PhysicEntity<T> other, Vec2f mtv, Friction friction) {
		this.other = other;
		this.mtv = mtv;
		this.friction = friction;
	}
}
