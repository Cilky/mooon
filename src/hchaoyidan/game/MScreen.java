package hchaoyidan.game;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.nio.file.Paths;

import hchaoyidan.engine.Application;
import hchaoyidan.engine.PhysicsWorld;
import hchaoyidan.engine.Screen;
import hchaoyidan.engine.persistence.Persistence;
import hchaoyidan.engine.sound.SoundPlayer;
import hchaoyidan.engine.ui.Text;
import hchaoyidan.engine.ui.UIRectangle;
import hchaoyidan.engine.ui.UIShape;
import hchaoyidan.engine.ui.Viewport;
import starter.Vec2f;
import starter.Vec2i;


/**
 * Default launch screen for Tou
 * @author yidanzeng
 *
 */
public class MScreen extends Screen {

	UIShape background;
	private Persistence p;
	boolean isSaved = false;
	
	/**
	 * Instantiates the StartScreen
	 * @param game
	 */
	public MScreen(Application game) {
		super(game);
		setup();
	}
	
	/** 
	 * Initiates all the screen elements
	 */
	@Override
	public void setup() {
		background = new UIRectangle(null, new Vec2f(0,0), null, new Vec2i(windowSize.x, windowSize.y));
		content.add(background);
		
		if (!isSaved()) {
		world = new MWorld(new Vec2i(2000, 25000));
		view = new Viewport(new Vec2f(0, 0), new Vec2i(windowSize.x,windowSize.y), background, world);
		}

		
		
		// viewport, does the drawing
		content.add(view);
		p = new Persistence();
		world.setView(view);
		world.setup();
	}

	@Override
	public void onTick(long nanosSincePreviousTick) {
		world.onTick(nanosSincePreviousTick);
		if (world.isWinGame()) {
			WinScreen win = new WinScreen(game);
			game.setScreen(win);
		}
		}
	
	@Override
	public void onKeyTyped(KeyEvent e) {
		world.onKeyTyped(e);	
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		if(e.getKeyChar() == "r".charAt(0)) {
			((MWorld) world).gameSound.stop();
			((MWorld)world).gameSound = null;
			
			MScreen screen = new MScreen(game);
			((MWorld)world).gameSound = new SoundPlayer(new File("sounds/ambient.wav"), true);
			((MWorld)world).gameSound.run();
			game.setScreen(screen);
		}
		world.onKeyPressed(e);
		if (e.getKeyCode() == KeyEvent.VK_2) {
			p.saveScreen(this, Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + "resources"
					+ File.separator + "screen");
			StartScreen startMenu = new StartScreen(Main.game);
			Main.game.setScreen(startMenu);
			Main.game.startup();
		}
		
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		world.onKeyReleased(e);
	}
	
	@Override
	public void onMouseClicked(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMouseClicked(e);
        }
	}

	@Override
	public void onMousePressed(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMousePressed(e);
        }
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMouseReleased(e);
        }
	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMouseDragged(e);
        }
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMouseMoved(e);
        }
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		Vec2i mouse = new Vec2i(e.getX(), e.getY());
		
        if(view.isWithin(mouse)) {
        	world.onMouseWheelMoved(e);
        }
	}
	
	@Override
	public void setWorld(PhysicsWorld world) {
		super.setWorld(world);
		world.setView(view);
	}

	@Override
	public void setView(Viewport view) {
		content.remove(view);
		this.view = view;
		content.add(view);

	}
	
	public boolean isSaved() {
		return isSaved;
	}

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}
	
	
	
}
