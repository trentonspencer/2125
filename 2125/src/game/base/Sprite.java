//loads an image to be used by a SceneObject
//also stores animation and collision data

package game.base;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Sprite {
	private BufferedImage sprite;
	private Collision collision;
	public final int width;
	public final int height;
	private boolean hasCollision;
	
	private HashMap<String, Animation> animations;
	
	public Sprite(String file, boolean hasCollision) {
		sprite = loadImage(getClass().getResourceAsStream(file));
		
		this.width = sprite.getWidth();
		this.height = sprite.getHeight();
		this.animations = new HashMap<String, Animation>();
		
		this.hasCollision = hasCollision;
		collision = hasCollision ? new Collision(sprite) : null;
	}
	
	public static BufferedImage loadImage(InputStream file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
		
//		BufferedImage convertedImg = null;
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		GraphicsDevice gd = ge.getDefaultScreenDevice();
//		GraphicsConfiguration gc = gd.getDefaultConfiguration();
//		convertedImg = gc.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
//		
//		Graphics2D g2d = convertedImg.createGraphics();
//		g2d.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
//		g2d.dispose();
//		
//		return convertedImg;
	}
	
	//returns a brightened version of the image passed to it
	public static BufferedImage getBrightImage(BufferedImage img) {
		BufferedImage bright = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		Graphics2D g = bright.createGraphics();
		g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		g.dispose();
		
		RescaleOp rescaleOp = new RescaleOp(1.0f, 60.0f, null);
		rescaleOp.filter(img, bright);
		return bright;
	}
	
	public BufferedImage getImage() {
		return sprite;
	}
	
	public Collision getCollision() {
		return collision;
	}
	
	public boolean hasCollision() {
		return hasCollision;
	}
	
	//add the animation to a hash table with a string key for easy lookup
	public void addAnimation(String key, Animation animation) {
		animations.put(key, animation);
	}
	
	public Animation getAnimation(String key) {
		return animations.get(key);
	}
}
