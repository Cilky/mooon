package hchaoyidan.engine.animation;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import starter.Vec2i;


public class Animation {
	private int tickCount;
    private int frameTime;
    private int currentFrameIndex;    
    private boolean isMoving = false;
    private String moveDirection;
    
    
    
    public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	private Vec2i position;

    private List<BufferedImage> frames = new ArrayList<BufferedImage>();    // Arraylist of frames 

    public Animation(List<BufferedImage> frames, int frameTime) {
        this.frameTime = frameTime;
        this.frames = frames;
        this.currentFrameIndex = 0;

    }

    public List<BufferedImage> getFrames() {
		return frames;
	}

	public void setFrames(List<BufferedImage> frames) {
		this.frames = frames;
	}

	public BufferedImage getSprite() {
        return frames.get(currentFrameIndex);
    }

    public void onDraw(Graphics g) {
    	if (position != null) {
    	g.drawImage(getSprite(), position.y, position.x, null);
    	}
    }
    
    public void onTick() {
    		if (isMoving) {
            tickCount++;
            
            if (tickCount == frameTime) {
                tickCount = 0;
                currentFrameIndex = (currentFrameIndex + 1) % frames.size();
                
                
            if (position !=null && moveDirection !=null) {	
            if (moveDirection.equals("left")) {
            	position = new Vec2i(position.x, position.y - 1);
            }
            if (moveDirection.equals("right")) {
            	position = new Vec2i(position.x, position.y + 1);
            }
            if (moveDirection.equals("up")) {
            	position = new Vec2i(position.x - 1, position.y);
            }
            if (moveDirection.equals("down")) {
            	position = new Vec2i(position.x + 1, position.y);
            }
            }
            }
    		
    		}
    }
    
    public void stop() {
    	isMoving = false;
    }
    
    public void move() {
    	isMoving = true;
    }

	public Vec2i getPosition() {
		return position;
	}

	public void setPosition(Vec2i position) {
		this.position = position;
	}

	public String getMoveDirection() {
		return moveDirection;
	}

	public void setMoveDirection(String moveDirection) {
		this.moveDirection = moveDirection;
	}
}
