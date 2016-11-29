package hchaoyidan.game;

import java.awt.Color;
import java.util.ArrayList;

import hchaoyidan.engine.Edge;
import hchaoyidan.engine.entity.CollisionPolygon;
import hchaoyidan.game.entity.BirdEnemy;
import hchaoyidan.game.entity.FishEnemy;
import hchaoyidan.game.entity.StarEnemy;
import starter.Vec2f;

public class LevelManager {

	private MWorld world;
	
	public LevelManager(MWorld world) {
		this.world = world;
	}
	
	public FishEnemy makeFish(Vec2f pos) {
		int size = 40;
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
	
	public BirdEnemy makeBird(Vec2f pos) {
		int size = 40;
		float mid = size / 2f;
		
		Edge v1 = new Edge(pos, new Vec2f(pos.x + mid, pos.y + (mid / 2f)));
		Edge v2 = new Edge(new Vec2f(pos.x + mid, pos.y + (mid / 2f)), new Vec2f(pos.x + size, pos.y));
		Edge v3 = new Edge(new Vec2f(pos.x + size, pos.y), new Vec2f(pos.x + mid, pos.y + (mid / 4f)));
		Edge v4 = new Edge(new Vec2f(pos.x + mid, pos.y + (mid / 4f)), pos);
		
		ArrayList<Edge> list = new ArrayList<Edge>();
		list.add(v1);
		list.add(v2);
		list.add(v3);
		list.add(v4);
		
		return new BirdEnemy(new CollisionPolygon(Color.YELLOW, world.getBackground(), list), world);
	}

	public StarEnemy makeStar(Vec2f pos) {
		int size = 50;
		float mid = size / 2f;
		
		Edge v1 = new Edge(pos, new Vec2f(pos.x + mid, pos.y - (mid / 2f)));
		Edge v2 = new Edge(new Vec2f(pos.x + mid, pos.y - (mid / 2f)), new Vec2f(pos.x + size, pos.y));
		Edge v3 = new Edge(new Vec2f(pos.x + size, pos.y), new Vec2f(pos.x + mid, pos.y + (mid / 2f)));
		Edge v4 = new Edge(new Vec2f(pos.x + mid, pos.y + (mid / 2f)), pos);
		
		ArrayList<Edge> list = new ArrayList<Edge>();
		list.add(v1);
		list.add(v2);
		list.add(v3);
		list.add(v4);
		
		return new StarEnemy(new CollisionPolygon(Color.PINK, world.getBackground(), list), world);
	}
	
	public void changeFriction() {
		
	}

}
