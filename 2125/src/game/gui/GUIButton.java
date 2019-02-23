package game.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import game.base.ColorUtil;
import game.scene.Scene;

public class GUIButton extends GUIText {
	public GUIButton(int x, int y, int width, int height, String text) {
		super(x, y, width, height, text);
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillRoundRect(x, y, width, height, 5, 5);
		
		if(showBorder) {
			g.setColor(isHovered ? ColorUtil.mul(borderColor, 1.35) : borderColor);
			g.drawRoundRect(x, y, width, height, 5, 5);
		}
		
		Dimension size = getStringSize(text);
		g.setFont(defaultFont);
		g.setColor(isHovered ? ColorUtil.mul(textColor, 1.35) : textColor);
		
		g.drawString(text, (int)((x+width/2)-(size.getWidth()/2)), (int)((y+height)-(size.getHeight()/4)));
	}
}
