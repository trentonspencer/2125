//base physics object

package game.scene;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;

import game.base.AnimationFrame;
import game.base.AnimationHandler;
import game.base.Collision;
import game.base.Emitter;
import game.base.Physics;
import game.base.Sprite;
import game.base.Typemasks;

public class SceneObject implements Physics {
	protected Sprite sprite;
	protected BufferedImage currentImage;
	private AnimationHandler animHandler;
	
	protected ArrayList<Emitter> emitters;
	
	protected double x;
	protected double y;
	protected double xvel;
	protected double yvel;
	
	protected int typemask;
	protected int collidesWith;
	
	protected int currentTick;
	protected double totalDelta;
	
	protected boolean isBright;
	
	public SceneObject(Sprite sprite, double x, double y, int typemask) {
		this.sprite = sprite;
		this.currentImage = sprite.getImage();
		this.x = x;
		this.y = y;
		this.typemask = typemask;
		
		switch(typemask) {
		case Typemasks.Default:
			collidesWith = Typemasks.Default;
			break;
		case Typemasks.Player:
			collidesWith = Typemasks.Enemy | Typemasks.EnemyProjectile | Typemasks.Pickup;
			break;
		case Typemasks.Enemy:
			collidesWith = Typemasks.Player | Typemasks.PlayerProjectile;
			break;
		}
		
		animHandler = new AnimationHandler(null, 5) {
			@Override
			public void onStart() {
				currentImage = this.getCurrentFrame().getImage();
			}
			
			@Override
			public void onStop() {
				currentImage = sprite.getImage();
			}
			
			@Override
			public void onFrameChange(AnimationFrame frame) {
				currentImage = frame.getImage();
			}
		};
		
		emitters = new ArrayList<Emitter>();
		
		xvel = 0;
		yvel = 0;
		
		currentTick = 0;
		totalDelta = 0;
		
		isBright = false;
		
		onSpawn();
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getXVel() {
		return xvel;
	}
	
	public double getYVel() {
		return yvel;
	}
	
	public int getWidth() {
		return sprite.width;
	}
	
	public int getHeight() {
		return sprite.height;
	}
	
	public int getTypemask() {
		return typemask;
	}
	
	public void setPos(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void centerAtX(double x) {
		this.x = x-getWidth()/2;
	}
	
	public void centerAtY(double y) {
		this.y = y-getHeight()/2;
	}
	
	public void centerAt(double x, double y) {
		this.x = x-getWidth()/2;
		this.y = y-getHeight()/2;
	}
	
	public double getCenterX() {
		return x+getWidth()/2;
	}
	
	public double getCenterY() {
		return y+getHeight()/2;
	}
	
	public void setVel(double xvel, double yvel) {
		this.xvel = xvel;
		this.yvel = yvel;
	}
	
	public boolean isBright() {
		return isBright;
	}
	
	public void setIsBright(boolean isBright) {
		this.isBright = isBright;
	}
	
	public void add(Emitter emitter) {
		emitters.add(emitter);
	}
	
	public void remove(Emitter emitter) {
		emitters.remove(emitter);
	}
	
	public void clearEmitters() {
		emitters.clear();
	}
	
	public void render(Graphics g) {
		if(isBright)
			g.drawImage(Sprite.getBrightImage(currentImage), (int)x, (int)y, currentImage.getWidth(), currentImage.getHeight(), null);
		else
			g.drawImage(currentImage, (int)x, (int)y, currentImage.getWidth(), currentImage.getHeight(), null);
	}
	
	public void playAnimation(String animation, boolean loop, boolean playForward) {
		animHandler.setAnimation(sprite.getAnimation(animation));
		animHandler.play(loop, playForward);
	}
	
	public void stopAnimation() {
		animHandler.stop();
	}
	
	public void updateAnimation() {
		animHandler.update();
	}
	
	public AnimationHandler getAnimationHandler() {
		return animHandler;
	}
	
	public boolean isColliding() {
		if(animHandler.isPlaying())
			return animHandler.getCurrentFrame().getCollision() != null;
		else
			return sprite.hasCollision();
	}
	
	public Collision getCollision() {
		if(animHandler.isPlaying())
			return animHandler.getCurrentFrame().getCollision();
		else
			return sprite.getCollision();
	}
	
	public boolean isCollidingWith(SceneObject other) {
		//System.out.println(other.typemask + " & " + collidesWith + " = " + (other.typemask & collidesWith));
		if((other.typemask & collidesWith) == 0)
			return false;
		
		return getCollision().isCollidingWith(other.getCollision(), x, y, other.x, other.y);
	}
	
	public void move(double dt) {
		x += xvel * dt;
		y += yvel * dt;
	}
	
	@Override
	public void onSpawn() {
		
	}
	
	@Override
	public void onCollision(SceneObject other) {
		
	}
	
	@Override
	public void tick(double dt) {
		move(dt);
	}
	
	public void advanceTick(double dt) {
		tick(dt);
		
		for(int i = 0; i < emitters.size(); i++)
			emitters.get(i).advanceTick(dt);
		
		currentTick++;
		totalDelta += dt;
	}
}
