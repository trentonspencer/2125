//lets you hook a scene's tickScene function to add additional functionality

package game.base;

public abstract class PhysicsListener {
	public abstract void tick(double dt, int totalTicks);
}
