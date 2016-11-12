package hchaoyidan.engine.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import starter.Vec2i;

public class Sprite extends MapTile{

	public Sprite(Vec2i indexPos) {
		super("sprite", null, indexPos);
	}
	
	@Override
	public void draw(Graphics2D g) {
		try {
			BufferedImage image = ImageIO.read(new File("resources/sprites.png"));
			
			int frameW = image.getWidth() / 22;
			int frameX = frameW * 15;
			int frameY = 0;
			
			g.drawImage(image, (int)position.x, (int)position.y, (int)position.x + width, (int)position.y + height, frameX, frameY, frameX + frameW, frameY + frameW, null);
			
			System.out.println("finished drawing sprite");
		} catch (IOException e) {
			System.out.println("Bad image source");
		}
	}

}
