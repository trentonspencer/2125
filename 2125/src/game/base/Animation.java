package game.base;

import java.util.ArrayList;

public class Animation {
	private ArrayList<AnimationFrame> frames = new ArrayList<AnimationFrame>();
	
	public Animation(String path, int frameCount, boolean doCollision) {
		for(int i = 1; i <= frameCount; i++) {
			frames.add(new AnimationFrame(path + i + ".png", doCollision));
		}
	}
	
	public AnimationFrame getFrame(int frame) {
		if(frames.size() == 0)
			return null;
		return frames.get(frame);
	}
	
	public int getFrameCount() {
		return frames.size();
	}
}
