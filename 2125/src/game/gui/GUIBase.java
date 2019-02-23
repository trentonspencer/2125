//base class for all GUI classes

package game.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import game.scene.Scene;

public abstract class GUIBase implements GUIMouseEvents {
	protected static final Color defaultColor = new Color(0, 0, 0);
	
	protected int z;
	
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	protected boolean isVisible;
	
	protected Color color;
	protected Color borderColor;
	
	protected boolean showBorder;
	
	protected boolean isHovered;
	
	public GUIBase() {
		this.x = 0;
		this.y = 0;
		width = 0;
		height = 0;
		
		showBorder = true;
		
		isVisible = true;
		isHovered = false;
	}
	
	public GUIBase(int x, int y) {
		this.x = x;
		this.y = y;
		width = 0;
		height = 0;
		
		showBorder = true;
		
		isVisible = true;
		isHovered = false;
	}
	
	public GUIBase(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		color = defaultColor;
		borderColor = defaultColor;
		
		showBorder = true;
		
		isVisible = true;
		isHovered = false;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Color getBorderColor() {
		return borderColor;
	}
	
	public boolean getBorderVisibility() {
		return showBorder;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public boolean isHovered() {
		return isHovered;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	public void setBorderVisibility(boolean visible) {
		this.showBorder = visible;
	}
	
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void centerAtX(int x) {
		this.x = x-width/2;
	}
	
	public void centerAtY(int y) {
		this.y = y-height/2;
	}
	
	public void centerAt(int x, int y) {
		this.x = x-width/2;
		this.y = y-height/2;
	}
	
	public void alignRight(int x) {
		this.x = x-width;
	}
	
	public void setExtent(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setVisible(boolean visible) {
		isVisible = visible;
	}
	
	public void setHovered(boolean hovered) {
		if(hovered != isHovered) {
			isHovered = hovered;
			if(hovered)
				onHover();
			else
				onHoverStop();
		}
	}
	
	public boolean isContained(int x, int y) {
		if(x >= this.x && x <= this.x+width &&
				y >= this.y && y <= this.y+height)
			return true;
		return false;
	}
	
	public abstract void render(Graphics2D g);
	
	public void onClick(MouseEvent e) {
	}
	
	public void onHover() {
	}
	
	public void onHoverStop() {
	}
}
