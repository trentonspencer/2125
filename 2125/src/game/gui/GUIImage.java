package game.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.base.Sprite;

public class GUIImage extends GUIBase {
	protected BufferedImage image;

	public GUIImage(int x, int y, Sprite sprite) {
		super(x, y);
		
		image = sprite.getImage();
		width = image.getWidth();
		height = image.getHeight();
	}
	
	@Override
	public void render(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, image.getWidth(), image.getHeight(), null);
	}
}
