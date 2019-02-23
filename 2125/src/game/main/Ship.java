//extension of the SceneObject class
//handles damage, health, etc

package game.main;

import game.base.Projectile;
import game.base.Sprite;
import game.base.Typemasks;
import game.scene.SceneObject;

public abstract class Ship extends SceneObject {
	protected double health;
	protected double speed;
	protected int lastBrightTime;
	
	
	public Ship(Sprite sprite, double x, double y, int typemask) {
		super(sprite, x, y, typemask);
		
		if(health == 0)
			health = 100.0;
	}
	
	public void setHealth(double health) {
		this.health = health;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void damage(double amount) {
		health -= amount;
		if(health <= 0)
			die();
		
		setIsBright(true);
		lastBrightTime = currentTick;
		
		onDamage();
	}
	
	public void die() {
		health = 0;
		setVel(0, 0);
		
		onDeath();
	}
	
	public abstract void onDamage();
	public abstract void onDeath();
	
	@Override
	public void onCollision(SceneObject other) {
		if((other.getTypemask() & (Typemasks.EnemyProjectile | Typemasks.PlayerProjectile)) > 0) {
			damage(((Projectile)other).getDamage());
			((Projectile)other).onDeath();
		}
	}
	
	@Override
	public void advanceTick(double dt) {
		tick(dt);
		currentTick++;
		totalDelta += dt;
		
		if(currentTick-lastBrightTime == 1 && isBright())
			setIsBright(false);
	}
}
