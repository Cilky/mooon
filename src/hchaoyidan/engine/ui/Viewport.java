package hchaoyidan.engine.ui;

import java.awt.Graphics2D;

import hchaoyidan.engine.PhysicsWorld;
import hchaoyidan.engine.game.MPhysicEntity;
import starter.Vec2f;
import starter.Vec2i;

/**
 * The viewport representation for a game
 * @author yidanzeng
 *
 */
public class Viewport extends UIRectangle {

	public PhysicsWorld world;
	public Vec2f upperGamePt;
	public int scale = 1;

	
	/**
	 * Viewport helps display the map
	 * Does translation from gamespace to screenspace
	 * @param pos of viewport
	 * @param size of viewport
	 * @param map that viewport references
	 * @param parent the background
	 */
	public Viewport(Vec2f pos, Vec2i size, UIShape parent, PhysicsWorld<MPhysicEntity> world) {
		super(null, pos, parent, size);
		this.type = "viewport";
		upperGamePt = new Vec2f(0,0);
		this.world = world;
	}
	

	@Override 
	public void drawSelf(Graphics2D g) {
		g.setClip((int) position.x, (int) position.y, width, height);

		world.onDraw(g);
   
		g.setClip(0, 0, (int)parent.getWidth(), (int)parent.getHeight());
	}
	
	
	/**
	 * Finds the game equivalent point from a given screen point
	 * @param screenPt
	 * @return gamepoint
	 */
	public Vec2f screenToGame(Vec2f screenPt) {
		float toReturnX = ((screenPt.x - position.x) / scale) + upperGamePt.x;
		float toReturnY = ((screenPt.y - position.y) / scale) + upperGamePt.y;
		
		return new Vec2f(toReturnX, toReturnY);
	}
	
	/**
	 * Finds the screen equivalent point from a given game point
	 * @param gamePt
	 * @return screenpoint
	 */
	public Vec2f gameToScreen(Vec2f gamePt) {
		float toReturnX = ((gamePt.x - upperGamePt.x) * scale) + position.x;
		float toReturnY = ((gamePt.y - upperGamePt.y) * scale) + position.y;
		
		return new Vec2f(toReturnX, toReturnY);
	}

	@Override
	public void onResize(Vec2i newsize) {
		float newRatio = (float) newsize.x / (float) newsize.y;

		if(lockRatio) { 
			findAspectRatio();
			if(newRatio < 1) { // go by the smallest side: width
				width = (int) (sizeRatio.x * newsize.x); // parent??? somewhere
				height = (int) (width * (1/aspectRatio));			
				
			} else { // height
				height = (int) (sizeRatio.y * newsize.y); // it's using height suddenly...
				width = (int) (height * aspectRatio);
			}
		} else {
			width = (int) (sizeRatio.x * newsize.x);
			height = (int) (sizeRatio.y * newsize.y);
		}
		
		// for position
		if(parent != null) {
			position = new Vec2f((posRatio.x * parent.getWidth()) + parent.getX(), (posRatio.y * parent.getHeight()) + parent.getY());
		} else {
			position = new Vec2f(posRatio.x * newsize.x, posRatio.y * newsize.y);
		}

		world.onResize(newsize);
	}

}
