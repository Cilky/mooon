package hchaoyidan.engine.entity;

import java.io.Serializable;

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
	
	public Collision(PhysicEntity<T> other, Vec2f mtv) {
		this.other = other;
		this.mtv = mtv;
	}
}
