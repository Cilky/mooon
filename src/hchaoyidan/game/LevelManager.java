package hchaoyidan.game;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hchaoyidan.engine.Edge;
import hchaoyidan.engine.Friction;
import hchaoyidan.engine.ad.AdaptiveDifficulty;
import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.entity.CollisionPolygon;
import hchaoyidan.game.entity.BirdEnemy;
import hchaoyidan.game.entity.FishEnemy;
import hchaoyidan.game.entity.MPhysicsEntity;
import hchaoyidan.game.entity.StarEnemy;
import starter.Vec2f;

public class LevelManager implements Serializable {

	private MWorld world;
	private AdaptiveDifficulty ad;
	private int countdown = 1000; 
	private int highScoreLevel = 30; 
	private int level = 1;
	
	public LevelManager(MWorld world) {
		this.world = world;
		this.ad = new AdaptiveDifficulty();
	}
	
	public FishEnemy makeFish() {
		Vec2f pos = getPos();
		int size = 40;
		float mid = size / 2f;
		
		Edge v1 = new Edge(new Vec2f(pos.x, pos.y), new Vec2f(pos.x + size, pos.y + mid));
		Edge v2 = new Edge(new Vec2f(pos.x + size, pos.y + mid), new Vec2f(pos.x, pos.y + size));
		Edge v3 = new Edge(new Vec2f(pos.x, pos.y + size), new Vec2f(pos.x, pos.y));
		
		ArrayList<Edge> list = new ArrayList<Edge>();
		list.add(v1);
		list.add(v2);
		list.add(v3);
		
		FishEnemy fish = new FishEnemy(new CollisionPolygon(Color.RED, world.getBackground(), list), world);
		
		if(pos.x > 0) {
			fish.changeDir();
		}
		
		return fish;
	}
	
	public BirdEnemy makeBird() {
		Vec2f pos = getPos();
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

	public StarEnemy makeStar() {
		Vec2f pos = getPos();
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
	
	public void changeLevel(int highScore) {
		level++;
		world.reset();
		
		if(level == 2) {
			world.environ = Friction.AIR;
			highScoreLevel = 60;
			countdown = 1500;
			
			List<Integer> toMake = ad.onTick(highScore/highScoreLevel, world.getEnemies(), world.particles.size(), level);
			replenish(toMake);
			
			world.changeColor(new Color(255, 188, 0));
			System.out.println("SWITCHED TO AIR");
		} else if(level == 3) {
			world.environ = Friction.SPACE;
			highScoreLevel = 100;
			countdown = 2000; 
			
			List<Integer> toMake = ad.onTick(highScore/highScoreLevel, world.getEnemies(), world.particles.size(), level);
			replenish(toMake);
			
			world.changeColor(new Color(80, 37, 174));
			System.out.println("SWITCHED TO SPACE");
		} else if(level == 4){
			world.gameOver(true);
			world.changeColor(new Color(43, 0, 56));
			System.out.println("GAME WON");
		}

	}

	public List<MoonParticle> makeParticles(int numParticles) {
		List<MoonParticle> particles = new ArrayList<>();

		for (int i = 0; i < numParticles; i++) {
			Random randX = new Random();
			Random randY = new Random();
			int randomNumX = randX.nextInt((world.windowSize.x - 0) + 1) + 0;
			int randomNumY = randY.nextInt((world.windowSize.y - 0) + 1) + 0;
			float positionX = (float) randomNumX;
			float positionY = (float) randomNumY;
			Vec2f position = new Vec2f((float) randomNumX, (float) randomNumY);
			CollisionCircle circle = new CollisionCircle(Color.WHITE, position, world.getBackground(), 6);
			MoonParticle particle = new MoonParticle(new Vec2f(positionX, positionY), circle);
			particles.add(particle);
		}
		
		return particles;
	}
	
	public Vec2f getPos() {
		Random randY = new Random();
		int randomNumY = randY.nextInt(((world.windowSize.y - 200) - 0) + 1);
		int x = -100;
		if(randomNumY >= 300) {
			x = world.windowSize.y + 10;
		}
		
		return new Vec2f(x, randomNumY);
	}
	
	public void replenish(List<Integer> toMake) {
		int enemies = toMake.get(0);
		int points = toMake.get(1);
		
		for(int i = 0; i < enemies; i++) {
			if(level == 1) {
				MPhysicsEntity fish = makeFish();
				world.addPhysicEntity(fish);
			} else if(level == 2) {
				MPhysicsEntity bird = makeBird();
				world.addPhysicEntity(bird);
			} else {
				MPhysicsEntity star = makeStar();
				world.addPhysicEntity(star);
			}
		}
		
		List<MoonParticle> newp = makeParticles(points);
		for(int i = 0; i < newp.size(); i++) {
			world.particles.add(newp.get(i));
		}
	}
	
	public void onTick(long nanosSincePreviousTick, int highScore) {
		countdown--;
		if(countdown <= 0 && highScore > highScoreLevel) {
			changeLevel(highScore);
		} if(highScore < 0) {
			world.gameOver(false);
			world.changeColor(Color.BLACK);
			System.out.println("GAME LOST");
		}
		
		
		// replenishing enemies and points
		List<Integer> toMake = ad.onTick(highScore/highScoreLevel, world.getEnemies(), world.particles.size(), level);
		replenish(toMake);
	}
}
