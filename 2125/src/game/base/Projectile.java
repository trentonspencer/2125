package game.base;

import game.scene.SceneObject;

public abstract class Projectile extends SceneObject {
	protected double damage;
	protected int lifeTime;
	
	public Projectile(Sprite sprite, double x, double y, int typemask, int lifeTime, double damage) {
		super(sprite, x, y, typemask);
		this.lifeTime = lifeTime;
		this.damage = damage;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public abstract void onDeath();
	
	@Override
	public void advanceTick(double dt) {
		tick(dt);
		currentTick++;
		totalDelta += dt;
		
		if(currentTick >= lifeTime) //prevents projectiles from slowly causing performance problems
			onDeath();
	}
}
