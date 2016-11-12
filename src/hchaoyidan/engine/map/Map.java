package hchaoyidan.engine.map;

import java.awt.Graphics2D;
import java.util.ArrayList;

import starter.Vec2f;
import starter.Vec2i;

/**
 * The map representation of the game space, containing matrix of position of units and other tile types
 * @author yidanzeng
 *
 */
public abstract class Map {
	
	protected float[][] tileSpace;
	public MapTile[][] gameSpace;
	
	public Vec2i size;
	private ValueNoise gen;
	public int seed;
	
	public ArrayList<Sprite> sprites;
	
	/**
	 * Creates the map
	 * @param width
	 * @param height
	 * @param seed for procedural generation
	 */
	public Map(int width, int height, int seed) {
		size = new Vec2i(width, height);
		tileSpace = new float[width][height];
		gameSpace = new MapTile[width][height];
		gen = new ValueNoise(seed);
		this.seed = seed;
		sprites = new ArrayList<Sprite>();
		
		createTileMap();
	}
	
	/**
	 * Finds the surrounding neighbors of the tile at x, y, where x, y is at Integer[2][2]
	 * @param x position
	 * @param y position
	 * @return a 3x3 Integer matrix of neighbors of tile at x,y
	 */
	public float[][] getNeighbors(float x, float y) {
		// TODO
		return new float[3][3];
	}
	
	/**
	 * Populates a matrix with interpolated floats for a procedurally generated map
	 */
	public void createTileMap() {
		for(int i = 0; i < size.x; i++) {
			for(int k = 0; k < size.y; k++) {
				this.tileSpace[i][k] = gen.valueNoise(new Vec2f((float) i, (float) k), 0.12f, 0.5f, 3);
			}
		}
	}
	
	/** 
	 * Gets the mapTile at the position of x, y
	 * @param x
	 * @param y
	 * @return mapTile object at x, y
	 */
	public MapTile getTile(float x, float y) {
		
		if(x > this.size.x - 1 || y > this.size.y - 1) {
			return null;
		} else if(x < 0 || y < 0) {
			return null;
		} else {
			return gameSpace[(int)x][(int)y]; 
		}
	}
	
	public MapTile[][] getMap() {
		return gameSpace;
	}
	
	/**
	 * Populates the gamespace matrix with MapTiles
	 */
	public abstract void createGameMap();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < size.x; i++) {
			for(int k = 0; k < size.y; k++) {
				sb.append(gameSpace[i][k].toString());
				sb.append(tileSpace[i][k]);
			}
		}
		
		return sb.toString();
	}
	
	public void addSprites(int num) {
		while(num > 0) {
			for(MapTile[] t : gameSpace) {
				for(MapTile m : t) {
					if(!m.type.equals("Water")) {
						Sprite toAdd = new Sprite(new Vec2i((int)m.getX(), (int)m.getY()));
						sprites.add(toAdd);
					}
				}
			}
		}
		
		for(int i=0; i<sprites.size(); i++) {
			System.out.println(i + " " + sprites.get(i).position);
		}
		
	}
	
	private void drawSprites(Graphics2D g) {
		for(int i = 0; i < sprites.size(); i++) {
			sprites.get(i).draw(g);
		}
	}
	
	public void drawSelf(Graphics2D g) {
		// drawing each tile
		
		for(MapTile[] t : gameSpace) {
			for(MapTile m : t) {
				m.draw(g);
			}
		}
		
		drawSprites(g);
	}

	
}
