package hchaoyidan.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import hchaoyidan.engine.Edge;
import hchaoyidan.engine.Friction;
import hchaoyidan.engine.PhysicsWorld;
import hchaoyidan.engine.Shape;
import hchaoyidan.engine.entity.CollisionAAB;
import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.entity.CollisionPolygon;
import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.engine.entity.Entity;
import hchaoyidan.engine.entity.PhysicsEntity;
import hchaoyidan.engine.highscore.HighScoreManager;
import hchaoyidan.engine.particles.Particle;
import hchaoyidan.engine.persistence.Persistence;
import hchaoyidan.engine.sound.SoundPlayer;
import hchaoyidan.engine.ui.Text;
import hchaoyidan.game.entity.Enemy;
import hchaoyidan.game.entity.Ground;
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
	private Player player;
	private CollisionShape background;
	private Text highScoreText;
	private int highScoreInt;
	private HighScoreManager hsm;
	private Persistence p;
	private Text soundText;
	public boolean soundToggled;
	private String configFile;
	private SoundPlayer gameSound;
	private boolean soundIsRunning = false;
	private LevelManager lm;
	private int numParticles;
	private List<MoonParticle> particles;
	List<Particle> toRemove = new ArrayList<>();

	/**
	 * Constructor for TouWorld
	 * 
	 * @param windowSizeX
	 * @param windowSizeY
	 */
	public MWorld(int windowSizeX, int windowSizeY) {
		super(windowSizeX, windowSizeY);
		setup();
	}

	@Override
	protected void setup() {

		lm = new LevelManager(this);
		
		environ = Friction.WATER;
		gameSound = new SoundPlayer(new File("sounds/ambient.wav"), true);

		KeyLogger.reset();

		background = new CollisionAAB(new Color(15, 0, 80), new Vec2f(0, 0), null,
				new Vec2i(windowSize.x, windowSize.y));
		Entity back = new Entity(background);
		back.drawOrder = 0;
		entities.add(back);

		p = new Persistence();

		float t1 = windowSize.x * 3 / 100;
		float t2 = windowSize.y * 88 / 100;
		highScoreText = new Text(Integer.toString(highScoreInt), new Color(86, 142, 210), new Vec2f(t1, t2), background,
				new Vec2i(100, 100));
		highScoreText.setFamily("Andale Mono");

		configFile = Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + "resources"
				+ File.separator + "config.properties";

		soundToggled = Boolean.parseBoolean((p.loadConfig(configFile)).getProperty("sound"));

		soundText = new Text("Sound" + " : " + soundToggled, new Color(86, 142, 210), new Vec2f(t1 * 25, t2),
				background, new Vec2i(100, 100));

		soundText.setFamily("Andale Mono");

		// PLAYER
		CollisionCircle shape = new CollisionCircle(Color.WHITE, new Vec2f((windowSize.x / 2) - 25, windowSize.y - 100), background,
				50);
		player = new Player(shape, background, this);
		physicEntities.add((MPhysicsEntity) player);

		hsm = new HighScoreManager();

		// ENEMY
		MPhysicsEntity fish = lm.makeFish(new Vec2f(-100, 500));
		physicEntities.add(fish);

		MPhysicsEntity bird = lm.makeBird(new Vec2f(100, 200));
		physicEntities.add(bird);
		
		MPhysicsEntity star = lm.makeStar(new Vec2f(400, 350));
		physicEntities.add(star);
		
		numParticles = 20;
		particles = new ArrayList<>();

		for (int i = 0; i < numParticles; i++) {
			Random randX = new Random();
			Random randY = new Random();
			int randomNumX = randX.nextInt((windowSize.x - 0) + 1) + 0;
			int randomNumY = randY.nextInt((windowSize.y - 0) + 1) + 0;
			float positionX = (float) randomNumX;
			float positionY = (float) randomNumY;
			Vec2f position = new Vec2f((float) randomNumX, (float) randomNumY);
			Color color = new Color(255, 255, 255, 255);
			CollisionCircle circle = new CollisionCircle(color, position, background, 6);
			MoonParticle particle = new MoonParticle(new Vec2f(positionX, positionY), circle);
			particles.add(particle);
		}
	}

	@Override
	protected void update() {
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

		for (PhysicsEntity<MPhysicsEntity> p : physicEntities) {
			p.isColliding = false;
			p.onTick(nanosSincePreviousTick);
		}

		keyLogger();
		update();

		soundText.setText("Sound" + " : " + soundToggled);

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
				deltaY += 2;
			} else if (c == "s".charAt(0)) {
				deltaY += 2;
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
			p.saveGame(this, Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + "resources"
					+ File.separator + "game");
			StartScreen startMenu = new StartScreen(Main.game);
			Main.game.setScreen(startMenu);
			Main.game.startup();
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
	
}
