package hchaoyidan.engine.ui;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import hchaoyidan.engine.map.Map;
import hchaoyidan.engine.map.MapTile;
import starter.Vec2f;
import starter.Vec2i;

/**
 * The viewport representation for a game
 * @author yidanzeng
 *
 */
public class TacViewport extends UIRectangle {

	private Map map;
	public Vec2f upperGamePt;
	public float scale = 100;
	public Vec2f translateVar;
	private AffineTransform currentTrans;
	
	/**
	 * Viewport helps display the map
	 * Does translation from gamespace to screenspace
	 * @param pos of viewport
	 * @param size of viewport
	 * @param map that viewport references
	 * @param parent the background
	 */
	public TacViewport(Vec2f pos, Vec2i size, Map map, UIShape parent) {
		super(null, pos, parent, size);
		this.type = "viewport";
		this.map = map;
		upperGamePt = new Vec2f(0,0);
		currentTrans = new AffineTransform();
		currentTrans.translate(position.x, position.y);
		currentTrans.scale(scale, scale);
	}
	

	
	public void pan(Vec2f translate) {
        upperGamePt = upperGamePt.plus(translate);
        setTransform();
    }
	
	public void zoom(int steps, Vec2f mousePoint) {
		Vec2f oldGamePoint = screenToGame(mousePoint);
		
		if (steps < 0) { // zooming out
			if((scale/ 1.1f * map.getMap().length) > 10) {
				scale = scale / 1.1f;
			}
        } else { // zooming in
        	if((scale * 1.1f) < width) {
        		scale = scale * 1.1f;
        	}
        }
		
        Vec2f newGamePoint = screenToGame(mousePoint); 
        
        float deltaX = oldGamePoint.x - newGamePoint.x;
        float deltaY = oldGamePoint.y - newGamePoint.y;
        
		pan(new Vec2f(deltaX, deltaY));
    }
	
	public float getScale() {
		return scale;
	}
	
	public MapTile[][] getMap() {
		return map.getMap();
	}
	
	public void setTransform() {
		currentTrans = new AffineTransform();
		currentTrans.translate(-(upperGamePt.x * scale), -(upperGamePt.y * scale));
		currentTrans.translate(position.x, position.y);
		currentTrans.scale(scale, scale);
	}

	@Override 
	public void drawSelf(Graphics2D g) {
		g.setClip((int) position.x, (int) position.y, width, height);
		AffineTransform old = g.getTransform();
        
        g.setTransform(currentTrans);
        map.drawSelf(g);
		g.setTransform(old);
        
		g.setClip(0, 0, (int)parent.getWidth(), (int)parent.getHeight());
	}
	
	/**
	 * Sets the map reference for this viewport
	 * @param gameMap map reference to be set
	 */
	public void setMap(Map gameMap) {
		this.map = gameMap;
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
		
		setTransform();
	}

}
