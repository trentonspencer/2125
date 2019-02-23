//physics events that objects can define

package game.base;

import game.scene.SceneObject;

public interface Physics {
	public void onSpawn();
	public void onCollision(SceneObject other);
	public void tick(double dt);
	public void advanceTick(double dt);
}
