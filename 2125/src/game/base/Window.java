//window class that creates a JFrame and handles the game scene

package game.base;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.gui.GUIText;
import game.scene.Scene;

public class Window extends JFrame implements Runnable {
	private static final long serialVersionUID = -4231431240916134873L;
	
	private Thread thread;
	private boolean running;
	private boolean paused;
	
	private Scene scene;
	private GUIText fpsCounter;
	
	public Window(String title, int width, int height) {
		super(title);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Dimension size = new Dimension(width, height);
		getContentPane().setPreferredSize(size);
		
		running = false;
		paused = false;
		
		scene = null;
		
		fpsCounter = new GUIText(width-80, 0, 100, 16, "FPS: 0");
		fpsCounter.setFont(new Font("Arial", Font.PLAIN, 14));
		fpsCounter.setTextColor(new Color(255, 255, 0));
		fpsCounter.setVisible(false);
		
		//stop the program when the window is closed
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				stop();
				System.exit(0);
			}
		});
	}

	public Scene createScene(Dimension size) {
		Scene scene = new Scene(size);
		setScene(new Scene(size));
		return scene;
	}
	
	public void setScene(Scene scene) {
		if(this.scene != null)
			getContentPane().remove(this.scene);
		
		this.scene = scene;
		getContentPane().add(scene);
		scene.requestFocusInWindow(); //ensures keyboard & mouse listeners are active
		scene.initScene();
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public void showFPS(boolean show) {
		if(show)
			fpsCounter.setVisible(true);
		else
			fpsCounter.setVisible(false);
	}
	
	public synchronized void start() {
		if(thread != null && thread.isAlive())
			return;
		
		thread = new Thread(this);
		
		scene.initScene();
		running = true;
		thread.start();
	}
	
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//sets the pause state of the physics simulation
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	@Override
	public void run() {
		double currentTime = System.currentTimeMillis();
		double newTime;
		double delta = 0.0;
		double frameTime = 0;
		int frames = 0;
		
		while(running) {
			newTime = System.currentTimeMillis();
			delta += newTime - currentTime;
			frameTime += newTime - currentTime;
			currentTime = newTime;
			
			//limit the physics simulation to roughly 32 ticks per second
			while(delta >= 31)
			{
				if(paused == false)
					scene.tickScene(0.031); //fixed delta time step
				
				delta -= 31;
			}
			
			if(frameTime >= 1000) //update the fps counter every second
			{
				fpsCounter.setText("FPS: " + frames, false);
				frameTime = 0;
				frames = 0;
			}
			
			scene.renderScene(fpsCounter);
			frames++;
			
			//force the thread to sleep for 1 millisecond to decrease CPU load
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
