package hchaoyidan.engine.game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import hchaoyidan.engine.Edge;
import hchaoyidan.engine.PhysicsWorld;
import hchaoyidan.engine.entity.CollisionAAB;
import hchaoyidan.engine.entity.CollisionCircle;
import hchaoyidan.engine.entity.CollisionPolygon;
import hchaoyidan.engine.entity.CollisionShape;
import hchaoyidan.engine.entity.Entity;
import hchaoyidan.engine.entity.PhysicEntity;
import starter.Vec2f;
import starter.Vec2i;

/**
 * World representation for Debugger
 * @author yidanzeng
 *
 */
public class DebugWorld extends PhysicsWorld<MPhysicEntity>{

	private Vec2f lastDrag;
	private Entity selected;
	
	public DebugWorld(int windowSizeX, int windowSizeY) {
		super(windowSizeX, windowSizeY);
		setup();
	}
	
	@Override
	protected void setup() {
		CollisionShape background = new CollisionAAB(Color.BLUE, new Vec2f(0,0), null, new Vec2i(windowSize.x, windowSize.y));
		Entity back = new Entity(background);
		entities.add(back);

		MPhysicEntity bCircle = new TestEntity(new CollisionCircle(Color.PINK, new Vec2f(200, 100), background, 150)); 
		physicEntities.add(bCircle);
		
		MPhysicEntity aCircle = new TestEntity(new CollisionCircle(Color.PINK, new Vec2f(150, 300), background, 50));
		physicEntities.add(aCircle);
		
		MPhysicEntity dRect = new TestEntity(new CollisionAAB(Color.GREEN, new Vec2f(100, 400), background, new Vec2i(100, 50)));
		physicEntities.add(dRect);
		
		MPhysicEntity dRect2 = new TestEntity(new CollisionAAB(Color.GREEN, new Vec2f(500, 300), background, new Vec2i(200, 100)));
		physicEntities.add(dRect2);
		
		Edge v1 = new Edge(new Vec2f(400, 100), new Vec2f(500, 100));
		Edge v2 = new Edge(new Vec2f(500, 100), new Vec2f(400, 300));
		Edge v3 = new Edge(new Vec2f(400, 300), new Vec2f(300, 200));
		Edge v4 = new Edge(new Vec2f(300, 200), new Vec2f(400, 100));
		
		ArrayList<Edge> list = new ArrayList<Edge>();
		list.add(v1);
		list.add(v2);
		list.add(v3);
		list.add(v4);
		
		MPhysicEntity poly1 = new TestEntity(new CollisionPolygon(Color.WHITE, background, list));
		physicEntities.add(poly1);
			
		float x = windowSize.x / 2;
		float y = windowSize.y / 2;
		
		Edge k1 = new Edge(new Vec2f(x + 15, y), new Vec2f(x + 45, y + 15));
		Edge k2 = new Edge(new Vec2f(x + 45, y + 15), new Vec2f(x + 15, y + 30));
		Edge k3 = new Edge(new Vec2f(x + 15, y + 30), new Vec2f(x + 15, y));
		
		ArrayList<Edge> klist = new ArrayList<Edge>();
		klist.add(k1);
		klist.add(k2);
		klist.add(k3);
		MPhysicEntity poly2 = new TestEntity(new CollisionPolygon(Color.WHITE, background, klist));
		physicEntities.add(poly2);
	}
	
	
	@Override
	public void onMousePressed(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
		for(MPhysicEntity p : physicEntities) {
			if(p.isWithin(mouse)) { 
				selected = p;
			}
		}
		
		lastDrag = new Vec2f(e.getX(), e.getY());
	}


	@Override
	public void onMouseReleased(MouseEvent e) {
		lastDrag = new Vec2f(e.getX(), e.getY());
		selected = null;
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		
		if(selected != null && selected.getType().equals("physicEntity")) {
			float deltaX = lastDrag.x - e.getX();
			float deltaY = lastDrag.y - e.getY();
			selected.move(-deltaX, -deltaY);
			lastDrag = new Vec2f(e.getX(), e.getY());
		}
	}
	
	@Override
	public void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selfTick(long nanosSincePreviousTick) {
		
		for(PhysicEntity<MPhysicEntity> p : physicEntities) {
			p.getShape().showMtv = false; // make all false
			p.onTick(nanosSincePreviousTick);
		}
	}


}
