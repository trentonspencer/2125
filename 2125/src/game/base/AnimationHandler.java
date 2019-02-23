package game.base;

public class AnimationHandler implements AnimationEvents {
	private Animation animation;
	
	private int frame;
	private int frameDuration;
	private int frameCount;
	private int totalFrames;
	
	private boolean looping;
	private boolean playing;
	private boolean playForward;
	
	public AnimationHandler(Animation animation, int frameDuration) {
		this.animation = animation;
		this.frameDuration = frameDuration;
		
		frame = 0;
		frameCount = 0;
		
		if(animation != null)
			totalFrames = animation.getFrameCount();
	}
	
	public void setAnimation(Animation animation) {
		stop();
		this.animation = animation;
		totalFrames = animation.getFrameCount();
	}
	
	public void setFrameDuration(int frameDuration) {
		this.frameDuration = frameDuration;
	}
	
	public void play(boolean loop, boolean playForward) {
		if(playing)
			return;
		
		this.looping = loop;
		this.playForward = playForward;
		
		frame = playForward ? 0 : totalFrames-1;
		frameCount = 0;
		
		playing = true;
		
		onStart();
	}
	
	public void stop() {
		playing = false;
		looping = false;
		
		onStop();
	}
	
	public void update() {
		if(playing == false)
			return;
		
		frameCount++;
		
		if(frameCount > frameDuration) {
			frameCount = 0;
			
			frame += playForward ? 1 : -1;
			
			if(frame == totalFrames || frame < 0) {
				if(looping)
					frame = playForward ? 0 : totalFrames-1;
				else {
					stop();
					return;
				}
			}
			
			onFrameChange(animation.getFrame(frame));
		}
		
		onUpdate();
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public boolean isPlayingForward() {
		return playForward;
	}
	
	public boolean isLooping() {
		return looping;
	}
	
	public AnimationFrame getCurrentFrame() {
		return animation.getFrame(frame);
	}
	
	public int getCurrentFrameNumber() {
		return frame;
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onStop() {
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onFrameChange(AnimationFrame frame) {
	}
}
