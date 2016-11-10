package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import engine.Edge;
import engine.PhysicsWorld;
import engine.entity.CollisionAAB;
import engine.entity.CollisionPolygon;
import engine.entity.CollisionShape;
import engine.entity.Entity;
import engine.entity.PhysicEntity;
import engine.ui.Text;
import starter.Vec2f;
import starter.Vec2i;

/**
 * World for the tou game, contains key, mouse, and entity collision logic
 * @author yidanzeng
 *
 */
public class MWorld extends PhysicsWorld<MPhysicEntity> {
	private Player player;
	private CollisionShape background;
	private final Vec2f g = new Vec2f(0, 900.8f);
	private Text health;
	private boolean grenade = false;

	/**
	 * Constructor for TouWorld
	 * @param windowSizeX
	 * @param windowSizeY
	 */
	public MWorld(int windowSizeX, int windowSizeY) {
		super(windowSizeX, windowSizeY);
		setup();
	}
	
	@Override
	protected void setup() {
		KeyLogger.reset();
		
		background = new CollisionAAB(Color.YELLOW, new Vec2f(0,0), null, new Vec2i(windowSize.x, windowSize.y));
		Entity back = new Entity(background);
		back.drawOrder = 0;
		entities.add(back);
		
		float t1 = windowSize.x * 3/100;
		float t2 = windowSize.y * 80/100;
		health = new Text("100", new Color(86, 142, 210), new Vec2f(t1, t2), background, new Vec2i(100, 100));
		health.setFontSize(20);
		health.setFamily("Andale Mono");
		
		float x = windowSize.x * 2/10;
		float y = windowSize.y * 2/10;
		
		Edge k1 = new Edge(new Vec2f(x + 15, y), new Vec2f(x + 45, y + 15));
		Edge k2 = new Edge(new Vec2f(x + 45, y + 15), new Vec2f(x + 15, y + 30));
		Edge k3 = new Edge(new Vec2f(x + 15, y + 30), new Vec2f(x + 15, y));
		
		ArrayList<Edge> klist = new ArrayList<Edge>();
		klist.add(k1);
		klist.add(k2);
		klist.add(k3);
		
		SlowEnemy poly2 = new SlowEnemy(new CollisionPolygon(Color.GREEN, background, klist), this);
		physicEntities.add((MPhysicEntity) poly2);
		
		SlowEnemy circle1 = new SlowEnemy(200, 200, background, this, 80);
		physicEntities.add((MPhysicEntity) circle1);
		
		SlowEnemy circle2 = new SlowEnemy(400, 0, background, this, 50);
		physicEntities.add((MPhysicEntity) circle2);
		
		SlowEnemy aab1 = new SlowEnemy(new CollisionAAB(Color.GREEN, new Vec2f(150, windowSize.y/2 - 20), background, new Vec2i(100,100)), this);
		physicEntities.add((MPhysicEntity) aab1);
		
		SlowEnemy aab2 = new SlowEnemy(new CollisionAAB(Color.GREEN, new Vec2f(450, 200), background, new Vec2i(50,50)), this);
		physicEntities.add((MPhysicEntity) aab2);
		
		CollisionAAB shape = new CollisionAAB(Color.BLACK, new Vec2f(100, windowSize.y/2 - 100), background, new Vec2i(50,50));
		player = new Player(shape, background, this);
		physicEntities.add((MPhysicEntity) player);
		
		Ground g = new Ground(0, 450, background, new Vec2i(980, 300));
		g.isStatic = true;
		physicEntities.add((MPhysicEntity) g);
	
		Ground g1 = new Ground(300, 150, background, new Vec2i(100, 50));
		g1.isStatic = true;
		physicEntities.add((MPhysicEntity) g1);

		Ground g2 = new Ground(700, 200, background, new Vec2i(75, 50));
		g2.isStatic = true;
		physicEntities.add((MPhysicEntity) g2);

		// left wall
		Ground g4 = new Ground(0, 0, background, new Vec2i(50, 540));
		g4.isStatic = true;
		physicEntities.add((MPhysicEntity) g4);
		
		// right wall
		Ground g5 = new Ground(910, 0, background, new Vec2i(50, 960));
		g5.isStatic = true;
		physicEntities.add((MPhysicEntity) g5);
		
		// top wall
		Ground g6 = new Ground(50, 0, background, new Vec2i(860, 50));
		g6.isStatic = true;
		physicEntities.add((MPhysicEntity) g6);

	}
	

	@Override
	protected void update() {
		for(MPhysicEntity t : newPhyEnt) {
			physicEntities.add(t);
		}
		
		newPhyEnt = new ArrayList<MPhysicEntity>();
		
		// remove elements that are expired
		List<MPhysicEntity> toKeep = new ArrayList<MPhysicEntity>();
		
		health.setText(Integer.toString(player.health));
		
		for(MPhysicEntity p : physicEntities) {
			if(!p.delete) {
				toKeep.add(p);
			}
		}
		
		physicEntities = toKeep;
			
	}
	
	@Override
	public void selfTick(long nanosSincePreviousTick) {
		
		for(PhysicEntity<MPhysicEntity> p : physicEntities) {
			p.isColliding = false;
			p.applyForce(g.smult(p.mass));
			p.onTick(nanosSincePreviousTick);	
			
		}
		
		keyLogger();
		update();
	}
	
	public void keyLogger() {
		int deltaX = 0;
		int deltaY = 0;
		
		for(Character c : KeyLogger.getKeys()) {
			if(c == "a".charAt(0)) {
				deltaX += -2;
			} else if(c == "d".charAt(0)) {
				deltaX += 2;
			} else if(c == "w".charAt(0)) {
				if(grenade) {
					grenade = false;
				} else {
					grenade = true;
				}
			
			}
		}
		player.applyForce(new Vec2f(deltaX * 200, deltaY * 200));
		
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			player.jump();
		} else {
			KeyLogger.add(e.getKeyChar());
		}
	}
	
	@Override
	public void onKeyReleased(KeyEvent e) {
		KeyLogger.remove(e.getKeyChar());
	}

	
	@Override
	public void onMouseClicked(MouseEvent e) {
		if(grenade) {
			player.grenade(new Vec2f(e.getX(), e.getY()));
		} else {
			player.shoot(new Vec2f(e.getX(), e.getY()));
		}
		
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
	public void onDraw(Graphics2D g) { 
		int drawOrder = 0; 
		
		while(drawOrder < 5) {
			for(Entity e : entities) {
				if(drawOrder == e.drawOrder && !e.delete) {
					e.onDraw(g);
				}
			}
			
			for(PhysicEntity<MPhysicEntity> p : physicEntities) {
				if(drawOrder == p.drawOrder && !p.delete) {
					p.onDraw(g);
				}
			}
			
			drawOrder++;
		}
		
		//health.onDraw(g);
	}
	

	@Override
	public void onKeyTyped(KeyEvent e) {
	}
	
	@Override
	public void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
	}

}
