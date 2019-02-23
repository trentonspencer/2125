//unused class

package game.base;

import java.awt.Color;
import java.util.ArrayList;

import game.scene.SceneObject;

public class Emitter {
	ArrayList<Particle> particles;
	
	protected SceneObject parent;
	
	protected int currentTick;
	protected double x;
	protected double y;
	
	protected double xvel;
	protected double yvel;
	
	protected int spawnRate;
	protected int lifetime;
	protected double initialSize;
	protected double finalSize;
	protected Color initialColor;
	protected Color finalColor;
	
	public Emitter(SceneObject parent, double x, double y, int lifetime, int spawnRate) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.spawnRate = spawnRate;
		
		currentTick = 0;
	}

	public void setVel(double xvel, double yvel) {
		this.xvel = xvel;
		this.yvel = yvel;
	}
	
	public void setInitialColor(Color color) {
		this.initialColor = color;
	}
	
	public void setFinalColor(Color color) {
		this.finalColor = color;
	}
	
	public void setInitialSize(double size) {
		this.initialSize = size;
	}
	
	public void setFinalSize(double size) {
		this.finalSize = size;
	}
	
	public void tick(double dt) {
		if(currentTick % spawnRate == 0) {
			double x = this.x;
			double y = this.y;
			
			if(parent != null) {
				x += parent.getX();
				y += parent.getY();
			}
			
			Particle p = new Particle(lifetime, initialSize, finalSize, initialColor, finalColor) {
				public void onDeath() {
					particles.remove(this);
				}
			};
			
			p.setPos(x, y);
			p.setVel(xvel, yvel);
			particles.add(p);
		}
	}
	
	public void advanceTick(double dt) {
		tick(dt);
		currentTick++;
	}
}
