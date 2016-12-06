package hchaoyidan.game;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import hchaoyidan.engine.Friction;
import hchaoyidan.engine.PhysicsWorld;
import hchaoyidan.engine.entity.CollisionAAB;
import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.engine.entity.Entity;
import hchaoyidan.engine.entity.PhysicsEntity;
import hchaoyidan.engine.highscore.HighScoreManager;
import hchaoyidan.engine.particles.Particle;
import hchaoyidan.engine.persistence.Persistence;
import hchaoyidan.engine.sound.SoundPlayer;
import hchaoyidan.engine.ui.Text;
import hchaoyidan.game.entity.MPhysicsEntity;
import hchaoyidan.game.entity.Player;
import starter.Vec2f;
import starter.Vec2i;

/**
 * World for the tou game, contains key, mouse, and entity collision logic
 * 
 * @author yidanzeng
 *
 */
public class MWorld extends PhysicsWorld<MPhysicsEntity> {
	public Player player;
	private CollisionShape background;
	private CollisionShape background2;
	private Entity back;
	private Entity back2;
	private Text highScoreText;
	private int highScoreInt;
	private HighScoreManager hsm;
	private Persistence p;
	private Text soundText;
	public boolean soundToggled;
	private String configFile;
	public SoundPlayer gameSound;
	private boolean soundIsRunning = false;
	private LevelManager lm;
	public List<MoonParticle> particles;
	List<Particle> toRemove = new ArrayList<>();
	private boolean isGameOver = false;
	private int transitionCountDown = 200;
	private int enemyNum = 0;
	private Vec2f lastPlayerPos;

	/**
	 * Constructor for TouWorld
	 * 
	 * @param windowSizeX
	 * @param windowSizeY
	 */
	public MWorld(Vec2i worldSize) {
		super(worldSize);
	}

	@Override
	public void setup() {
		
		lm = new LevelManager(this);
		
		environ = Friction.WATER;
		gameSound = new SoundPlayer(new File("sounds/ambient.wav"), true);

		KeyLogger.reset();
		background = new CollisionAAB(new Color(15, 0, 80), new Vec2f(0, 0), null, new Vec2i(worldSize.x, worldSize.y));
		back = new Entity(background);
		back.drawOrder = 0;
		entities.add(back);
		
		p = new Persistence();
		
		Vec2f windowSize = new Vec2f(view.getWidth(), view.getHeight());
		float t1 = windowSize.x * 3 / 100;
		float t2 = windowSize.y * 88 / 100;
		highScoreText = new Text(Integer.toString(highScoreInt), new Color(86, 142, 210), view.screenToGame(new Vec2f(t1, t2)), background,
				new Vec2i(100, 100));
		highScoreText.setFamily("Andale Mono");

		configFile = Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + "resources"
				+ File.separator + "config.properties";

		soundToggled = Boolean.parseBoolean((p.loadConfig(configFile)).getProperty("sound"));

		soundText = new Text("Sound" + " : " + soundToggled, new Color(86, 142, 210), view.screenToGame(new Vec2f(t1 * 25, t2)),
				background, new Vec2i(100, 100));

		soundText.setFamily("Andale Mono");


		hsm = new HighScoreManager();
		
		// PLAYER
		CollisionCircle shape = new CollisionCircle(Color.WHITE, view.screenToGame(new Vec2f((windowSize.x / 2) - 25, windowSize.y - 200)), background,
				50);
		player = new Player(shape, background, this);
		physicEntities.add((MPhysicsEntity) player);
		
		// ENEMY
		for(int i = 0; i < 5; i++) {
			MPhysicsEntity fish = lm.makeFish();
			physicEntities.add(fish);
		}
		
		particles = lm.makeParticles(100);
		
		lm.setChangeLevelHeight(worldSize.y/3);
		lm.setPlayer(player);
		lm.setPlayerStartY((int)player.getPosition().y);
	}

	@Override
	public void update() {
		for (MPhysicsEntity t : newPhyEnt) {
			physicEntities.add(t);
		}

		newPhyEnt = new ArrayList<MPhysicsEntity>();

		// remove elements that are expired
		List<MPhysicsEntity> toKeep = new ArrayList<MPhysicsEntity>();

		highScoreText.setText(Integer.toString(highScoreInt));

		for (MPhysicsEntity p : physicEntities) {
			if (!p.delete) {
				toKeep.add(p);
			}
		}

		physicEntities = toKeep;

		if (soundToggled) {
			if (!soundIsRunning) {
				gameSound.run();
				soundIsRunning = true;
			}
		} else {
			gameSound.stop();
			soundIsRunning = false;
		}
		
		
	}

	@Override
	public void selfTick(long nanosSincePreviousTick) {
		if(lastPlayerPos == null) {
			lastPlayerPos = player.getPosition();
		}
		
		getBoundedBox();
		
		enemyNum = 0;
		if(!isGameOver) {
			for (PhysicsEntity<MPhysicsEntity> p : physicEntities) {
				p.isColliding = false;
				p.onTick(nanosSincePreviousTick);
				if(p.getType().equals("fish") || p.getType().equals("bird") || p.getType().equals("start")) {
					//System.out.println(p.getType() + " " + p.getShape().getPosition());
					if(p.isInsideBox(getBoundedBox())) {
						enemyNum++;
					} /*else {
						p.delete = true;
					}*/
					                                                                                                                                                                                                                                                                                                                                                                                          
				}
			}
			
			keyLogger();
			
			if(view.gameToScreen(player.getPosition()).y <= view.getHeight() / 2f || view.gameToScreen(player.getPosition()).x >= view.getWidth() - 40 || view.gameToScreen(player.getPosition()).x >= 20) {
				float deltaX = lastPlayerPos.x - player.getPosition().x;
				float deltaY = lastPlayerPos.y - player.getPosition().y;
				
				// figuring out the change of gamePoints from screen points and passing that
				Vec2f translate = new Vec2f(-deltaX/view.getScale(), -deltaY/view.getScale());
		        view.pan(translate);
		        
		        soundText.move(translate.x, translate.y);
		        highScoreText.move(translate.x, translate.y);
			}
			
			update();

			soundText.setText("Sound" + " : " + soundToggled);

			if (lm.isLevelTransition()) {
				transitionCountDown--;
			}
			// counting down to level change
			lm.onTick(nanosSincePreviousTick, highScoreInt);
			
			lastPlayerPos = player.getPosition();
		}
	}
	
	public List<Vec2f> getBoundedBox() {
		Vec2f boxUpper = view.upperGamePt.plus(new Vec2f(-300, -500));
		Vec2f boxLower = view.upperGamePt.plus(new Vec2f(view.getWidth(), view.getHeight())).plus(new Vec2f(300, 0));
		List<Vec2f> toReturn = new ArrayList<Vec2f>();
		
		float uX = boxUpper.x;
		float uY = boxUpper.y;
		
		if(uX < 0) {
			uX = 0;
		} 
		
		if(uY < 0) {
			uY = 0;
		}
		boxUpper = new Vec2f(uX, uY);
		
		float lX = boxLower.x;
		float lY = boxLower.y;
		
		if(lX > worldSize.x) {
			lX = worldSize.x;
		} 
		
		if(lY > worldSize.y) {
			lY = worldSize.y;
		}
		boxLower = new Vec2f(lX, lY);

		toReturn.add(boxUpper);
		toReturn.add(boxLower);

		return toReturn;
	}

	public int getEnemies() {
		return enemyNum;
	}
	
	public int getParticles() {
		int num = 0;
		for(MoonParticle m : particles) {
			if(m.isInsideBox(getBoundedBox())) {
				num++;
			}
		}
		
		return num;
	}
	
	public void keyLogger() {
		int deltaX = 0;
		int deltaY = 0;

		for (Character c : KeyLogger.getKeys()) {
			if (c == "a".charAt(0)) {
				deltaX += -2;
			} else if (c == "d".charAt(0)) {
				deltaX += 2;
			} else if (c == "w".charAt(0)) {
				deltaY += -2;
			} else if (c == "s".charAt(0)) {
				//deltaY += 2;
			} else if (c == "s".charAt(0)) {
				//deltaY += 2;
			} else if (c == "j".charAt(0)) {
				environ = Friction.WATER;
				System.out.println("changed to water");
			} else if (c == "k".charAt(0)) {
				environ = Friction.AIR;
				System.out.println("changed to air");
			} else if (c == "l".charAt(0)) {
				environ = Friction.SPACE;
				System.out.println("changed to space");
			}
		}

		player.applyForce(new Vec2f(deltaX * 200, deltaY * 200));

	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		KeyLogger.add(e.getKeyChar());
		if (e.getKeyCode() == KeyEvent.VK_1) {
			String filePath = Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + "resources"
					+ File.separator + "highScores";
			System.out.println(filePath);
			hsm.setNewHighScores(highScoreInt, filePath);
			hsm.outputHighScores(filePath);
		}
		if (e.getKeyCode() == KeyEvent.VK_2) {
			gameSound.stop();
			gameSound = null;
			player.setSound(null);
		}
		if (e.getKeyCode() == KeyEvent.VK_3) {
			Properties props = new Properties();
			props.setProperty("sound", "true");
			p.saveConfig(configFile, props);
			Properties loaded = p.loadConfig(configFile);
			soundToggled = Boolean.parseBoolean((String) loaded.get("sound"));
		}
		if (e.getKeyCode() == KeyEvent.VK_4) {
			Properties props = new Properties();
			props.setProperty("sound", "false");
			p.saveConfig(configFile, props);
			Properties loaded = p.loadConfig(configFile);
			soundToggled = Boolean.parseBoolean((String) loaded.get("sound"));
		}
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		KeyLogger.remove(e.getKeyChar());
	}

	@Override
	public void onDraw(Graphics2D g) {
		int drawOrder = 0;

		while (drawOrder < 5) {
			if (drawOrder == 1) {
//				GradientPaint gP = new GradientPaint(0,
//						worldSize.y/2 - 1,
//						new Color(255, 188, 0), //orange
//			             0,
//			             worldSize.y,
//			             new Color(15, 0, 80)); //blue
//				g.setPaint(gP);
//				g.fill(new Rectangle2D.Double(0, worldSize.y/2 - 1, worldSize.x, worldSize.y));
//				
//				GradientPaint gP2 = new GradientPaint(0,0,
//						new Color(80, 37, 174), //gray
//			             0,
//			             worldSize.y/2,
//			             new Color(255, 188, 0)); //orange
//				g.setPaint(gP2);
//				g.fill(new Rectangle2D.Double(0, 0, worldSize.x, worldSize.y/2));
				GradientPaint gP = new GradientPaint(0,
						worldSize.y * 2/3 - 3,
						new Color(255, 188, 0), //orange
			             0,
			             worldSize.y,
			             new Color(15, 0, 80)); //blue
				g.setPaint(gP);
				g.fill(new Rectangle2D.Double(0, worldSize.y * 2/3 -100, worldSize.x, worldSize.y));
					
				GradientPaint gP2 = new GradientPaint(0,worldSize.y * 1/3 - 3,
						new Color(80, 37, 174), //gray
			             0,
			             worldSize.y * 2/3,
			             new Color(255, 188, 0)); //orange
				g.setPaint(gP2);
				g.fill(new Rectangle2D.Double(0, worldSize.y * 1/3 - 3, worldSize.x, worldSize.y * 1/3));
				
				GradientPaint gP3 = new GradientPaint(0, 0,
						new Color(43, 0, 56), //gray
			             0,
			             worldSize.y * 1/3,
			             new Color(80, 37, 174)); //orange
				g.setPaint(gP3);
				g.fill(new Rectangle2D.Double(0, 0, worldSize.x, worldSize.y * 1/3));
				//
			}
			
			for (Entity e : entities) {
				if (drawOrder == e.drawOrder && !e.delete) {
					e.onDraw(g);
				}
			}

			for (PhysicsEntity<MPhysicsEntity> p : physicEntities) {
				if (drawOrder == p.drawOrder && !p.delete) {
					p.onDraw(g);
				}
			}

			drawOrder++;
		}
		
		
		highScoreText.onDraw(g);
		soundText.onDraw(g);

		Iterator<MoonParticle> i = particles.iterator();
		while (i.hasNext()) {
			MoonParticle p = i.next(); // must be called before you can call
										// i.remove()
			p.onDraw(g);
			p.update();
			if (!player.collideParticle(p).equals(new Vec2f(0, 0))) {
				p.setFade(true);
			}
			if (p.isDestroy()) {
				highScoreInt = highScoreInt + 10;
				CollisionCircle shape = new CollisionCircle(Color.WHITE, player.getShape().getPosition(), background,
						(int)(player.getShape().getWidth() + 1));
				player.setShape(shape);
				i.remove();
			}
		}

	}
	
	public int getHighScoreInt() {
		return highScoreInt;
	}

	public void setHighScoreInt(int highScoreInt) {
		this.highScoreInt = highScoreInt;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public SoundPlayer getGameSound() {
		return gameSound;
	}

	public void setGameSound(SoundPlayer gameSound) {
		this.gameSound = gameSound;
	}

	
	public CollisionShape getBackground() {
		return background;
	}
	
	@Override
	public void gameOver(boolean win) {
		if(win) {
			highScoreText.setCenter();
			highScoreText.setText("YOU WON!!!");
			
		} else {
			highScoreText.setCenter();
			highScoreText.setText("YOU LOST...");
		}
		
		isGameOver = true;
	}

	@Override
	public void changeColor(Color color) {
		back.changeColor(color);
	}
	
	@Override
	public void onMouseClicked(MouseEvent e) {
	}

	@Override
	public void onMousePressed(MouseEvent e) {
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
	}

	@Override
	public void reset() {
		physicEntities = new ArrayList<MPhysicsEntity>();
		particles = new ArrayList<>();
		
		//player.reset();
		physicEntities.add((MPhysicsEntity) player); 
	}

	public LevelManager getLm() {
		return lm;
	}

	public void setLm(LevelManager lm) {
		this.lm = lm;
	}
	
	

}

