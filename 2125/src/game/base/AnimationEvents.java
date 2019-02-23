package game.base;

public interface AnimationEvents {
	public void onStart();
	public void onStop();
	public void onUpdate();
	public void onFrameChange(AnimationFrame frame);
}
