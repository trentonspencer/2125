//basically a wrapper class for drawing rectangles
//allows easier use with Scene objects

package game.gui;

import java.awt.Graphics2D;

import game.scene.Scene;

public class GUIRect extends GUIBase {

	public GUIRect(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
		
		if(showBorder) {
			g.setColor(borderColor);
			g.drawRect(x, y, width, height);
		}
	}
}
