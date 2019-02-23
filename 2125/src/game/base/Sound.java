package game.base;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	protected Clip clip;
	
	public Sound(String file) {
		AudioInputStream stream;
		
		try {
			BufferedInputStream bufferStream = new BufferedInputStream(getClass().getResourceAsStream(file));
			stream = AudioSystem.getAudioInputStream(bufferStream);
			clip = AudioSystem.getClip();
			clip.open(stream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}
	
	public void play() {
		//clip.setFramePosition(0); //reset the clip to the start
		//clip.start();
	}
	
	public void stop() {
		if(clip.isRunning())
			clip.stop();
	}
}
