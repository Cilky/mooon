package hchaoyidan.engine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import hchaoyidan.engine.Shape;
import starter.Vec2f;
import starter.Vec2i;

/**
 * The Text representation within a game: essentially a rectangle with a child text element
 * @author yidanzeng
 *
 */
public class Text extends UIRectangle {

	private int fontSize;
	private String family;
	private Color backgroundColor;
	public String text;
	private int textWidth;
	private int textHeight;
	private int widthPadding = 10;
	
	/**
	 * Instantiates the Text object
	 * @param text to be displayed
	 * @param color of text
	 * @param position where it resides on the 2D plane
	 * @param parent object
	 * @param type identification string
	 */
	public Text(String text, Color color, Vec2f position, Shape parent, Vec2i size) {
		super(color, position, parent, size);
		this.text = text;	
		this.type = "text";
		this.fontSize = 12;
	}

	/**
	 * Sets the backgroundColor for the text object
	 * @param color 
	 */
	public void setBackground(Color color) {
		this.backgroundColor = color;
	}
	
	/**
	 * Sets the fontsize for the text object
	 * @param fontSize 
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;	
	}
	
	/**
	 * Sets the family for the text object
	 * @param family
	 */
	public void setFamily(String family) {
		this.family = family;	
	}
	
	/**
	 * Sets the text for the text object, for instance if something needs to be updated
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}


	@Override
	public void drawSelf(Graphics2D g) { // drawing the text and rectangle (if needed)
		if(backgroundColor != null) {
			g.setColor(backgroundColor);
			g.fillRect((int) position.x, (int) position.y, width, height);
		}
		
		// finding the width and height
		g.setColor(color);
		g.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		g.setFont(new Font(family, Font.PLAIN, fontSize)); 
		
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D stringBounds = fm.getStringBounds(text, g);
		textWidth = (int) stringBounds.getWidth();
		textHeight = (int) (stringBounds.getHeight() + fm.getAscent());
		int widthLoLimit = width - (widthPadding + 10);
		int widthHiLimit = width - (widthPadding - 10);
		int heightLoLimit = height - (widthPadding + 10);
		int heightHiLimit = height - (widthPadding - 10);
		
		if(textWidth < widthLoLimit && textHeight < heightLoLimit) {
			while(textWidth < widthLoLimit && textHeight < heightLoLimit) {
				fontSize++;
				g.setFont(new Font(family, Font.PLAIN, fontSize)); 
				fm = g.getFontMetrics();
				stringBounds = fm.getStringBounds(text, g);
				textWidth = (int) stringBounds.getWidth();
				textHeight = (int) (stringBounds.getHeight() + fm.getAscent());
			}
		} else if (textWidth > widthHiLimit && textHeight > heightHiLimit) {
			while(textWidth > widthHiLimit && textHeight > heightHiLimit) {
				fontSize--;
				g.setFont(new Font(family, Font.PLAIN, fontSize)); 
				fm = g.getFontMetrics();
				stringBounds = fm.getStringBounds(text, g);
				textWidth = (int) stringBounds.getWidth();
				textHeight = (int) (stringBounds.getHeight() + fm.getAscent());
			}
		}
		
		float midWidth = width / 2f;
		float midHeight = height / 2f;
		float textMidWidth = (int) textWidth / 2f;
		float textMidHeight = (int) textHeight / 5f;
		
		float newX = position.x + (midWidth - textMidWidth);
		float newY = position.y + (midHeight + textMidHeight);

		g.drawString(text, newX, newY);
		
	}
	
	@Override
	public void fadeOut() {
		super.fadeOut();
		backgroundColor = new Color(backgroundColor.getRed(),
									backgroundColor.getGreen(),
									backgroundColor.getBlue(),
									getCurrAlpha());
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}
	

}
