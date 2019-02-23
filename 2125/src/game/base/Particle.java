//unused class

package game.base;

import java.awt.Color;
import java.awt.Graphics2D;

import game.scene.SceneObject;

public abstract class Particle implements Physics {
	protected int currentTick;
	
	protected double x;
	protected double y;
	protected double xvel;
	protected double yvel;
	
	protected int lifetime;
	protected double initialSize;
	protected double finalSize;
	protected double currentSize;
	
	protected Color initialColor;
	protected Color finalColor;
	protected Color currentColor;
	
	public Particle(int lifetime, double initialSize, double finalSize, Color initialColor, Color finalColor) {
		currentTick = 0;
		
		this.lifetime = lifetime;
		this.initialSize = initialSize;
		this.finalSize = finalSize;
		this.initialColor = initialColor;
		this.finalColor = finalColor;
		
		currentColor = initialColor;
		currentSize = initialSize;
		
		onSpawn();
	}
	
	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVel(double xvel, double yvel) {
		this.xvel = xvel;
		this.yvel = yvel;
	}
	
	public void move(double dt) {
		x += xvel * dt;
		y += yvel * dt;
	}
	
	public void update() {
		double step = currentTick/lifetime;
		currentSize = initialSize + step * (finalSize-initialSize);
		currentColor = ColorUtil.lerp(initialColor, finalColor, step);
	}
	
	public void render(Graphics2D g) {
		g.setColor(currentColor);
		g.fillRect((int)(x-currentSize/2), (int)(y-currentSize/2), (int)currentSize, (int)currentSize);
	}
	
	public abstract void onDeath();
	
	@Override
	public void onSpawn() {
	}

	@Override
	public void onCollision(SceneObject other) {
	}

	@Override
	public void tick(double dt) {
		if(currentTick >= lifetime) {
			onDeath();
			return;
		}
		
		update();
		move(dt);
	}

	@Override
	public void advanceTick(double dt) {
		tick(dt);
		currentTick++;
	}
}
