package hchaoyidan.game;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hchaoyidan.engine.Edge;
import hchaoyidan.engine.Friction;
import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.entity.CollisionPolygon;
import hchaoyidan.game.entity.BirdEnemy;
import hchaoyidan.game.entity.FishEnemy;
import hchaoyidan.game.entity.MPhysicsEntity;
import hchaoyidan.game.entity.StarEnemy;
import starter.Vec2f;

public class LevelManager implements Serializable {

	private MWorld world;
	private int fric = 0;
	private int countdown = 1000; 
	private int highScoreLevel = 40; 
	
	public LevelManager(MWorld world) {
		this.world = world;
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
	
	public void changeFriction() {
		fric++;
		world.reset();
		
		if(fric == 1) {
			world.environ = Friction.AIR;
			highScoreLevel = 70;
			countdown = 1500;
			world.particles = makeParticles(20);
			world.changeColor(new Color(255, 188, 0));

			for(int i = 0; i < 4; i++) {
				MPhysicsEntity bird = makeBird();
				world.physicEntities.add(bird);
			}
			System.out.println("SWITCHED TO AIR");
		} else if(fric == 2) {
			world.environ = Friction.SPACE;
			highScoreLevel = 100;
			countdown = 2000; 
			world.particles = makeParticles(15);
			world.changeColor(new Color(80, 37, 174));
			
			for(int i = 0; i < 3; i++) {
				MPhysicsEntity star = makeStar();
				world.physicEntities.add(star);
			}
			
			System.out.println("SWITCHED TO SPACE");
		} else if(fric == 3){
			world.gameOver(true);
			fric = 2;	
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
	
	public void onTick(long nanosSincePreviousTick, int highScore) {
		countdown--;
		
		if(countdown <= 0 && highScore > highScoreLevel) {
			changeFriction();
		} if(highScore < 0) {
			world.gameOver(false);
			System.out.println("GAME LOST");
		}
	}
}
