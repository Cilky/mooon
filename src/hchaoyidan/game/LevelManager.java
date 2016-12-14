package hchaoyidan.game;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hchaoyidan.engine.Edge;
import hchaoyidan.engine.Friction;
import hchaoyidan.engine.ad.AdaptiveDifficulty;
import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.entity.CollisionPolygon;
import hchaoyidan.engine.sound.SoundPlayer;
import hchaoyidan.game.entity.BirdEnemy;
import hchaoyidan.game.entity.FishEnemy;
import hchaoyidan.game.entity.MPhysicsEntity;
import hchaoyidan.game.entity.Obstacle;
import hchaoyidan.game.entity.Player;
import hchaoyidan.game.entity.StarEnemy;
import starter.Vec2f;

public class LevelManager implements Serializable {

	private MWorld world;
	private AdaptiveDifficulty ad;
	private int levelCount = 1000; 
	private int adCheck = 400;
	private int highScoreLevel = 100; 
	private int level = 1;
	private boolean levelTransition = false;
	private Player player = null;
	private int changeLevelHeight;
	private int playerStartY;
	private final Vec2f obstacleWidth = new Vec2f(100, 150);
	private final Vec2f obstacleHeight = new Vec2f(70, 120);
	
	public LevelManager(MWorld world) {
		this.world = world;
		this.ad = new AdaptiveDifficulty();
	}
	
	public Obstacle makeObstacle() {
		Vec2f pos = getPos();
		Vec2f end = null;
		ArrayList<Edge> list = new ArrayList<Edge>();
		for(int i = 0; i < 6; i++){
			Random randW = new Random();
			Random randH = new Random();
			int randomWidth = randW.nextInt((int)(obstacleWidth.y - obstacleWidth.x + 1)) + (int)obstacleWidth.x;
			int randomHeight = randH.nextInt((int)(obstacleHeight.y - obstacleHeight.y + 1)) + (int)obstacleHeight.y;
			
			switch(i) {
			case 0 : 
			case 1 :
			case 2 : 
				end = new Vec2f(pos.x - randomWidth, pos.y + randomHeight);
				break;
			case 3 :
				end = new Vec2f(pos.x + randomWidth, pos.y + randomHeight);
				break;
			case 4 : 
				end = new Vec2f(pos.x + randomWidth, pos.y - randomHeight);
				break;
			case 5 :
				end = list.get(0).getStart();
				break;
			}
			
			Edge ya = new Edge(pos, end);
			list.add(ya);
			pos = end;
		}
	
		
		return new Obstacle(new CollisionPolygon(Color.BLACK, world.getBackground(), list));
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
		
		return new BirdEnemy(new CollisionPolygon(Color.BLACK, world.getBackground(), list), world);
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
		levelTransition = true;
		
		if(level == 2) {
			world.environ = Friction.AIR;
			highScoreLevel = 200;
			levelCount = 1500;

			List<Integer> toMake = ad.onTick(((float)highScore) / highScoreLevel, world.getEnemies(), world.getParticles(), true);

			adjust(toMake);

			world.gameSound.stop();
			world.gameSound = null;
			world.gameSound = new SoundPlayer(new File("sounds/softwind.wav"), true);
			world.gameSound.run();
			System.out.println("SWITCHED TO AIR");
		} else if(level == 3) {
			world.environ = Friction.SPACE;
			highScoreLevel = 300;
			levelCount = 2000; 
			
			List<Integer> toMake = ad.onTick(((float)highScore) / highScoreLevel, world.getEnemies(), world.getParticles(), true);
			adjust(toMake);
			world.gameSound.stop();
			world.gameSound = null;
			world.gameSound = new SoundPlayer(new File("sounds/space.wav"), true);
			world.gameSound.run();
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
			Vec2f position = getPos();
			CollisionCircle circle = new CollisionCircle(Color.WHITE, position, world.getBackground(), 6);
			MoonParticle particle = new MoonParticle(position, circle);
			particles.add(particle);
		}
		
		return particles;
	}
	
	public Vec2f getPos() {
		List<Vec2f> box = world.getBoundedBox();
		Vec2f upper = box.get(0);
		Vec2f lower = box.get(1);

		Random randX = new Random();
		Random randY = new Random();
		int randomNumX = randX.nextInt((int)(lower.x - upper.x + 1)) + (int)upper.x;
		int randomNumY = randY.nextInt((int)(lower.y - upper.y + 1)) + (int)upper.y;
		
		return new Vec2f(randomNumX, randomNumY);
	}
	
	public void adjust(List<Integer> adapt) {
		int adjustEnemies = adapt.get(0);
		int adjustPoints = adapt.get(1);
		
		if(adjustEnemies < 0) {
			
			String type = "";
			for(MPhysicsEntity m : world.physicEntities) {
				if(adjustEnemies == 0) {
					break;
				}

				if(level == 1) {
					type = "fish";
				} else if(level == 2) {
					type = "bird";
				} else {
					type = "star";
				}
				
				if(m.getType().equals(type)) {
					m.delete = true;
					adjustEnemies++;
				}
			}
			
			
		} else {

			for(int i = 0; i < adjustEnemies; i++) {
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
		}
		
		
		if(adjustPoints < 0) {
			for(MoonParticle m : world.particles) {
				if(adjustPoints == 0) {
					break;
				} else {
					m.destroy();
					adjustPoints++;
				}
			}
		} else {
			List<MoonParticle> newp = makeParticles(adjustPoints);
			for(int i = 0; i < newp.size(); i++) {
				world.particles.add(newp.get(i));
			}
		}

	}
	
	public void onTick(long nanosSincePreviousTick, int highScore) {
		levelCount--;
		adCheck--;
		
		if((Math.abs(player.getPosition().y - playerStartY) >= changeLevelHeight * level)) {
			changeLevel(highScore);
			adCheck = 200;
		} else if(highScore < 0) {
			world.gameOver(false);
			world.changeColor(Color.BLACK);
			System.out.println("GAME LOST");
		} else if(adCheck == 0) {
			// adjusting enemies and points
			List<Integer> toMake = ad.onTick(((float)highScore) / highScoreLevel, world.getEnemies(), world.getParticles(), false);
			adjust(toMake);
			
			if(world.getObNum() < 2) {
				MPhysicsEntity ob = makeObstacle();
				for(Obstacle o : world.getObstacles()) {
					if(ob.getShape().collidesPoly((CollisionPolygon)o.getShape()).equals(new Vec2f(0,0))) {
						world.addPhysicEntity(ob);
						break;
					} else {
						ob = makeObstacle();
					}
				}
			}
			
			adCheck = 200;
		}
		
	}

	public boolean isLevelTransition() {
		return levelTransition;
	}

	public void setLevelTransition(boolean levelTransition) {
		this.levelTransition = levelTransition;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getChangeLevelHeight() {
		return changeLevelHeight;
	}

	public void setChangeLevelHeight(int changeLevelHeight) {
		this.changeLevelHeight = changeLevelHeight;
	}

	public int getPlayerStartY() {
		return playerStartY;
	}

	public void setPlayerStartY(int playerStartY) {
		this.playerStartY = playerStartY;
	}
	
	
	
	
}
