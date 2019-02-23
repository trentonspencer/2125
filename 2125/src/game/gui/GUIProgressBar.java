//typical looking progress/loading bar GUI class

package game.gui;

import java.awt.Color;
import java.awt.Graphics2D;

public class GUIProgressBar extends GUIBase {
	protected double progress;
	protected Color barColor;
	
	public GUIProgressBar(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.progress = 0.0;
	}
	
	public GUIProgressBar(int x, int y, int width, int height, Color barColor) {
		super(x, y, width, height);
		this.progress = 0.0;
		this.barColor = barColor;
	}
	
	public Color getBarColor() {
		return barColor;
	}
	
	public void setBarColor(Color barColor) {
		this.barColor = barColor;
	}
	
	public double getProgress() {
		return progress;
	}
	
	public void setProgress(double progress) {
		this.progress = progress;
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
		
		g.setColor(barColor);
		g.fillRect(x, y, (int)(width*progress), height);
		
		g.setColor(borderColor);
		g.drawRect(x, y, width, height);
	}
	
}
