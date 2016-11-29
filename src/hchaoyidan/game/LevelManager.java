package hchaoyidan.game;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import hchaoyidan.engine.Edge;
import hchaoyidan.engine.entity.CollisionCompound;
import hchaoyidan.engine.entity.CollisionPolygon;
import hchaoyidan.game.entity.BirdEnemy;
import hchaoyidan.game.entity.FishEnemy;
import hchaoyidan.game.entity.MPhysicsEntity;
import hchaoyidan.game.entity.StarEnemy;
import hchaoyidan.game.entity.TestEntity;
import starter.Vec2f;

public class LevelManager implements Serializable {

	private final int MIN_SIZE = 50;
	private final int MAX_SIZE = 150;
	private MWorld world;
	
	public LevelManager(MWorld world) {
		this.world = world;
	}
	
	public FishEnemy makeFish(Vec2f pos, String beginDir) {
		// gotta fix the shape and its reliance on background shape...it should just be the aspect ratios
		// of the screen and fixed
		int size = 60;
		float mid = size / 2f;
		
		Edge v1 = new Edge(new Vec2f(pos.x, pos.y), new Vec2f(pos.x + size, pos.y + mid));
		Edge v2 = new Edge(new Vec2f(pos.x + size, pos.y + mid), new Vec2f(pos.x, pos.y + size));
		Edge v3 = new Edge(new Vec2f(pos.x, pos.y + size), new Vec2f(pos.x, pos.y));
		
		ArrayList<Edge> list = new ArrayList<Edge>();
		list.add(v1);
		list.add(v2);
		list.add(v3);
		
		return new FishEnemy(new CollisionPolygon(Color.RED, world.getBackground(), list), world);
	}
	
	public BirdEnemy makeBird(Vec2f pos, String beginDir) {
		int size = 40;
		float mid = size / 2f;
		
		Edge v1 = new Edge(pos, new Vec2f(pos.x + size, pos.y + mid));
		Edge v2 = new Edge(new Vec2f(pos.x + size, pos.y + mid), new Vec2f(pos.x, pos.y + size));
		Edge v3 = new Edge(new Vec2f(pos.x, pos.y + size), pos);
		Edge v4 = new Edge(new Vec2f(pos.x + size, pos.y + mid), new Vec2f(pos.x + (size * 2), pos.y));
		Edge v5 = new Edge(new Vec2f(pos.x + (size * 2), pos.y), new Vec2f(pos.x + (size * 2), pos.y + size));
		Edge v6 = new Edge(new Vec2f(pos.x + (size * 2), pos.y + size), new Vec2f(pos.x + size, pos.y + mid));
		
		ArrayList<Edge> list = new ArrayList<Edge>();
		list.add(v1);
		list.add(v2);
		list.add(v3);
		
		ArrayList<Edge> list2 = new ArrayList<Edge>();
		list2.add(v4);
		list2.add(v5);
		list2.add(v6);
		
		CollisionPolygon poly1 = new CollisionPolygon(Color.YELLOW, world.getBackground(), list);
		CollisionPolygon poly2 = new CollisionPolygon(Color.YELLOW, world.getBackground(), list2);
		
		
		return new BirdEnemy(new CollisionCompound(pos, world.getBackground(), poly1, poly2), world);
	}

	public StarEnemy makeStar(Vec2f pos, String beginDir) {
		int size = 40;
		float mid = size / 2f;
		
		
		return null;
	}
	
	public void changeFriction() {
		
	}

}
