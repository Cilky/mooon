package hchaoyidan.engine.entity;

import hchaoyidan.engine.Friction;
import starter.Vec2f;

/**
 * The representation for an entity within a game that has physics attributes, like collision
 * @author yidanzeng
 *
 * @param <T>
 */
public abstract class PhysicsEntity<T extends PhysicsEntity<T>> extends Entity {

	/**
	 * Constructor for PhysicEntity
	 * @param s
	 */
	
	public final float mass;
	protected Vec2f vel = new Vec2f(0,0); 
	protected Vec2f impulse = new Vec2f(0,0); 
	protected Vec2f force  = new Vec2f(0,0);
	public boolean isStatic = false;
	public float restitution = 0f; // perfectly inelastic
	public boolean isColliding = false;
	public float fricVal = 1;
	
	
	public PhysicsEntity(CollisionShape s) {
		super(s);
		this.type = "physicEntity";
		mass = shape.getHeight() * shape.getWidth() / 2_000f;
	}

	/**
	 * Collides shape within this entity with otherEntity's shape
	 * @param otherEntity
	 * @return a Vec2f, mtv, of the collision, or (0,0) if not colliding
	 */
	public Vec2f collide(PhysicsEntity<T> otherEntity) {
		return shape.collides(otherEntity.getShape());
	}
	 
	/**
	 * Sets this entity to be removed by the world
	 */
	public void remove() {
		delete = true;
	}
	
	/**
	 * Carries out the collision actions if two entities are colliding (double dispatch for entities)
	 * @param other Entity
	 */
    public abstract void doCollide(T other);
	
	@Override
	public String toString() {
		return shape.type;
	}
	
	/**
	 * Applies force
	 * @param f
	 */
	public void applyForce(Vec2f f) {
		if(!isStatic) {
			force = force.plus(f);
//			force = new Vec2f(force.x + f.x, force.y + f.y);
		}
	}
	
	/**
	 * Applies impulse
	 * @param p
	 */
	public void applyImpulse(Vec2f p) {
		if(!isStatic) {
			impulse = impulse.plus(p);
//			impulse = new Vec2f(impulse.x + p.x, impulse.y + p.y);
//			vel = new Vec2f(0,0);
		}
	}
	
	/**
	 * What happens when two objects are colliding, needing to move out of collision
	 * @param collision
	 * @return
	 */
	public void onCollide(Collision<T> collision) {
		
		PhysicsEntity<T> other = collision.other;
		Vec2f mtv = collision.mtv;

		double cor = Math.sqrt(restitution * collision.other.restitution);
		Vec2f norm = collision.mtv.normalized();
		float ua = vel.dot(norm);
		float ub = other.vel.dot(norm);
		
		double massMult = (mass * other.mass) * (1 + cor) / (mass + other.mass);
		
		float impulseA = 0;
		boolean both = false;
		
		if(isStatic && !other.isStatic) {
			massMult = other.mass * (1 + cor);
			other.shape.move(-mtv.x, -mtv.y);
		} else if(!isStatic && other.isStatic) {
			massMult = mass * (1 + cor);
			shape.move(mtv.x, mtv.y);
		} else if(!isStatic && !other.isStatic) {
			shape.move(mtv.x, mtv.y);
			both = true;
		}
		
		if(!both) {
			impulseA = (ub - ua) * (float)massMult;
			applyImpulse(norm.smult(impulseA));
		} else {
			impulseA = (ub-ua) * (float) massMult;
			applyImpulse(norm.smult(impulseA));
		}
		
		// calculating my friction
		double cof = Math.sqrt(fricVal * other.fricVal);
		Vec2f n = new Vec2f(-norm.y, norm.x); // right-hand side vector, (y, -x) is left
		float uRel = ub - ua; 
		if(uRel > 0) {
			uRel = 1;
		} else {
			uRel = -1;
		}
		
		Vec2f impulseApplied = norm.smult(impulseA);
		double impulseMag = Math.sqrt(impulseApplied.x * impulseApplied.x + impulseApplied.y * impulseApplied.y);
		Vec2f force = n.smult((float)(cof * 0.2f * impulseMag * uRel)); // cos acting kind of weird
		applyImpulse(force);
		
	}
	
	@Override
	public void onTick(long nanosSincePreviousTick) {
		float t = nanosSincePreviousTick / 1_000_000_000f;
		vel = vel.plus(force.smult(t).sdiv(mass)).plus((impulse).sdiv(mass));
		
		shape.move(t*vel.x, t*vel.y);
		impulse = new Vec2f(0,0);
		force = new Vec2f(0,0);
          
	}
	
	public float checkRay(Vec2f ray, Vec2f sourcePoint) {
		float toReturn = shape.checkRay(ray, sourcePoint);
		
		if(isStatic) {
			toReturn = 0;
		}
		
		return toReturn;
	}

}
