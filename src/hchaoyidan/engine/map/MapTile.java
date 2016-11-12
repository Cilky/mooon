package hchaoyidan.engine.map;

import java.awt.Color;
import java.awt.Graphics2D;

import hchaoyidan.engine.ui.UIRectangle;
import starter.Vec2f;
import starter.Vec2i;

/**
 * A representation of a tile inside the map, contains info for drawing and mouse sensitive responses
 * @author yidanzeng
 *
 */
public class MapTile extends UIRectangle {
	
	public boolean highlight = false;
	public boolean isSelected = false;
	private Color color;
	private Color highlightColor = Color.RED;
	private Color selectColor = Color.YELLOW;
	public String type;

	
	/**
	 * Creates a maptile
	 * @param type of tile, i.e. "land" or "water"
	 * @param color of tile
	 * @param indexPos where in the map matrix this tile is located
	 */
	public MapTile(String type, Color color, Vec2i indexPos) { 
		super(color, new Vec2f(indexPos.x, indexPos.y), null, new Vec2i(1, 1));
		this.type = type;
		this.color = color;
	}
	
	/**
	 * Draws the tile, includes transform functions
	 */
	public void draw(Graphics2D g) {
		if(isSelected) {
			g.setColor(selectColor);
			g.fillRect((int) position.x, (int) position.y, width, height);
		} else {
			g.setColor(color);
			g.fillRect((int) position.x, (int) position.y, width, height);
		}
		
		if(highlight){
			g.setColor(highlightColor);
			g.fillRect((int) position.x, (int) position.y, width, height);
			// drawRect is buggy for some reason?
		}
		
	}
	
	@Override
	public String toString() {
		return "[" + position + " : " + type + "]";
	}

	
	public boolean isWithinTile(Vec2f mouse) {
		if(mouse.x >= position.x && mouse.x <= position.x + width) {
			if(mouse.y >= position.y && mouse.y <= position.y + height) {
				return true;
			}
		}
		return false;
	}
	
}
