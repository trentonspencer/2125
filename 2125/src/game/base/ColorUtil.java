package game.base;

import java.awt.Color;

public class ColorUtil {
	public static Color add(Color color, int amt) {
		if(amt < 0)
			return sub(color, -amt);
		
		int r = color.getRed() + amt;
		int g = color.getGreen() + amt;
		int b = color.getBlue() + amt;
		
		if(r > 255) r = 255;
		if(g > 255) g = 255;
		if(b > 255) b = 255;
		
		return new Color(r, g, b);
	}
	
	public static Color sub(Color color, int amt) {
		if(amt < 0)
			return add(color, -amt);
		
		int r = color.getRed() - amt;
		int g = color.getGreen() - amt;
		int b = color.getBlue() - amt;
		
		if(r < 0) r = 0;
		if(g < 0) g = 0;
		if(b < 0) b = 0;
		
		return new Color(r, g, b);
	}
	
	public static Color mul(Color color, double amt) {
		if(amt < 0)
			return color;
		
		int r = (int) (color.getRed() * amt);
		int g = (int) (color.getGreen() * amt);
		int b = (int) (color.getBlue() * amt);
		
		if(r > 255) r = 255;
		if(g > 255) g = 255;
		if(b > 255) b = 255;
		
		return new Color(r, g, b);
	}
	
	public static Color lerp(Color a, Color b, double step) {
		if(step > 1)
			return b;
		if(step < 0)
			return a;
		
		int red = (int)(a.getRed() + step * (b.getRed() - a.getRed()));
		int green = (int)(a.getGreen() + step * (b.getGreen() - a.getGreen()));
		int blue = (int)(a.getBlue() + step * (b.getBlue() - a.getBlue()));
		return new Color(red, green, blue);
	}
}
