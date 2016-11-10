package hchaoyidan.engine.animation;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import starter.Vec2i;


public class SpriteSheet {
	
	public static BufferedImage spriteSheet;
	private static final int PADDING = 1;
    private static final int TILE_SIZE = 33;
	
    public static BufferedImage loadSprite(String file) {

        BufferedImage sprite = null;

        try {
            sprite = ImageIO.read(new File(file));
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        return sprite;
    }

    public static BufferedImage getSprite(Vec2i position) {

        if (spriteSheet == null) {
            spriteSheet = loadSprite("sprites.png");
        }
        return spriteSheet.getSubimage(position.y * TILE_SIZE + PADDING, position.x * TILE_SIZE + PADDING, TILE_SIZE - PADDING, TILE_SIZE - PADDING);
    }
	
}
