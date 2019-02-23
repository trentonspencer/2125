//typemasks are used to put objects into certain collision "groups"
//makes it so some objects only collide with certain other objects

package game.base;

public class Typemasks {
	public static final int Default				= 1;
	public static final int Player				= 2;
	public static final int Enemy				= 4;
	public static final int PlayerProjectile	= 8;
	public static final int EnemyProjectile		= 16;
	public static final int Pickup				= 32;
}
