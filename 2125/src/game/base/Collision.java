package game.base;

import java.awt.image.BufferedImage;

public class Collision {
	public final int width;
	public final int height;
	
	private boolean[][] mask;
	
	public Collision(BufferedImage image) {
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		this.mask = new boolean[width][height];
		
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++)
				mask[i][j] = ((image.getRGB(i, j) >> 24) & 0xff) > 16; //set to true if the alpha is higher than 16
		}
	}
	
	//handles both AABB collision and pixel collision
	public boolean isCollidingWith(Collision c, double x, double y, double x2, double y2) {
		if(x < x2 + c.width &&
				x + width > x2 &&
				y < y2 + c.height &&
				y + height > y2) {
			//find the intersecting region of the two boxes
			int left = (int)Math.max(x, x2);
			int right = (int)Math.min(x+width, x2+c.width);
			int top = (int)Math.max(y, y2);
			int bottom = (int)Math.min(y+height, y2+c.height);
			
			//iterate those regions of the masks and check for a pixel collision
			for(int xx = left; xx < right; xx++) {
				for(int yy = top; yy < bottom; yy++) {
					if(mask[xx-(int)x][yy-(int)y] & c.mask[xx-(int)x2][yy-(int)y2])
						return true; //collision was found
				}
			}
		}
		
		return false; //no collision was found
	}
}
