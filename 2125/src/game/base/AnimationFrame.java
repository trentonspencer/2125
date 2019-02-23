package game.base;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class AnimationFrame {
	private BufferedImage image;
	private Collision collision;
	
	public AnimationFrame(String file, boolean doCollision) {
		image = Sprite.loadImage(getClass().getResourceAsStream(file));
		this.collision = doCollision ? new Collision(image) : null;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public Collision getCollision() {
		return collision;
	}
}
