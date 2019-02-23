//GUI text class

package game.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import game.scene.Scene;

public class GUIText extends GUIBase {
	protected static final FontRenderContext fontContext = new FontRenderContext(null, false, false);
	protected static final Font defaultFont = new Font("Consolas", Font.PLAIN, 24);
	protected static final Color defaultTextColor = new Color(0, 0, 0);
	
	protected Color textColor;
	protected Font font;
	protected String text;
	
	public GUIText(int x, int y, String text) {
		super(x, y);
		this.text = text;
		textColor = defaultTextColor;
		font = defaultFont;
		
		autoResize();
	}
	
	public GUIText(int x, int y, int width, int height, String text) {
		super(x, y, width, height);
		this.text = text;
		textColor = defaultTextColor;
		font = defaultFont;
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	public Font getFont() {
		return font;
	}
	
	public Dimension getStringSize(String text) {
		Rectangle2D metrics = font.getStringBounds(text, fontContext);
		int height = (int)metrics.getHeight();
		int width = (int)metrics.getWidth();
		
		return new Dimension(width, height);
	}
	
	public String getText() {
		return text.toString();
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setText(String text, boolean resize) {
		this.text = text;
		
		if(resize)
			autoResize();
	}
	
	public void autoResize() {
		Dimension size = getStringSize(text);
		width = (int)size.getWidth();
		height = (int)size.getHeight();
	}

	@Override
	public void render(Graphics2D g) {
		Dimension size = getStringSize(text);
		g.setFont(font);
		g.setColor(textColor);
		g.drawString(text, x, y+(int)size.getHeight());
	}
}
