//extends the Ship class and lets us easily set a location for the ship to travel to

package game.main;

import game.base.Sprite;

public abstract class AIShip extends Ship {
	protected double targetX;
	protected double targetY;
	
	public AIShip(Sprite sprite, double x, double y, int typemask) {
		super(sprite, x, y, typemask);
	}
	
	public void setTargetPos(double x, double y) {
		targetX = x;
		targetY = y;
	}
	
	public void move(double dt) {
		xvel = targetX-x;
		yvel = targetY-y;
		
		if(xvel < 0.1 && yvel < 0.1) {
			xvel = 0;
			yvel = 0;
			onTargetReached();
		}
		
		x += xvel*dt;
		y += yvel*dt;
	}
	
	public abstract void onTargetReached();
}
