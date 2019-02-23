package game.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
//multi-line GUI text class

import java.awt.geom.Rectangle2D;

import game.scene.Scene;

public class GUIMLText extends GUIText {

	public GUIMLText(int x, int y, String text) {
		super(x, y, text);
	}
	
	public Dimension getMLStringSize(String text) {
		String[] strings = text.split("\n");
		
		int fullWidth = 0;
		int fullHeight = 0;
		
		for(String str : strings) {
			Rectangle2D metrics = font.getStringBounds(str, fontContext);
			int width = (int)metrics.getWidth();
			if(width > fullWidth)
				fullWidth = width;
			
			fullHeight += (int)metrics.getHeight();
		}
		
		return new Dimension(fullWidth, fullHeight);
	}
	
	@Override
	public void autoResize() {
		Dimension size = getMLStringSize(text);
		width = (int)size.getWidth();
		height = (int)size.getHeight();
	}
	
	@Override
	public void render(Graphics2D g) {
		String[] strings = text.split("\n");
		Dimension size = getMLStringSize(text);
		
		g.setFont(font);
		g.setColor(textColor);
		
		for(int i = 0; i < strings.length; i++) {
			String str = strings[i];
			Dimension subSize = getStringSize(str);
			
			g.drawString(str, (int)(x + size.getWidth()/2 - subSize.getWidth()/2), (int)(y + subSize.getHeight() * i));
		}
	}
}
