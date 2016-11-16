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
public class Collision<T extends PhysicsEntity<T>> implements Serializable{
	public final PhysicsEntity<T> other; 
	public final Vec2f mtv; 
	
	public Collision(PhysicsEntity<T> other, Vec2f mtv) {
		this.other = other;
		this.mtv = mtv;
	}
}
