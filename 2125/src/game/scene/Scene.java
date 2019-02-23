//Handles all physics and rendering

package game.scene;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

import game.base.PhysicsListener;
import game.base.SpatialGrid;
import game.gui.GUIBase;
import game.gui.GUIText;

public class Scene extends Canvas {
	private static final long serialVersionUID = 3762684506996826051L;
	
	private ArrayList<SceneObject> sceneObjs;
	private ArrayList<SceneObject> backgroundObjs;
	private ArrayList<GUIBase> guiObjs;
	private SpatialGrid collisionGrid;
	
	private PhysicsListener plistener;
	
	private int totalTicks;
	
	public Scene(Dimension size) {
		super();
		
		setMinimumSize(size);
		setSize(size);
		setIgnoreRepaint(true);
		
		totalTicks = 0;
		sceneObjs = new ArrayList<SceneObject>();
		backgroundObjs = new ArrayList<SceneObject>();
		guiObjs = new ArrayList<GUIBase>();
		collisionGrid = new SpatialGrid(64, 800, 600);
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				for(int i = guiObjs.size()-1; i >= 0; i--) {
					GUIBase obj = guiObjs.get(i);
					if(obj.isVisible() && obj.isContained(e.getX(), e.getY())) {
						guiObjs.get(i).onClick(e);
						return;
					}
				}
			}
		});
		
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				for(int i = guiObjs.size()-1; i >= 0; i--) {
					GUIBase obj = guiObjs.get(i);
					if(obj.isVisible() && obj.isContained(e.getX(), e.getY()))
						obj.setHovered(true);
					else
						obj.setHovered(false);
				}
			}
			
			public void mouseDragged(MouseEvent e) {
				for(int i = guiObjs.size()-1; i >= 0; i--) {
					GUIBase obj = guiObjs.get(i);
					if(obj.isVisible() && obj.isContained(e.getX(), e.getY()))
						obj.setHovered(true);
					else
						obj.setHovered(false);
				}
			}
		});
	}
	
	public void add(SceneObject obj) {
		sceneObjs.add(obj);
	}
	
	public void remove(SceneObject obj) {
		sceneObjs.remove(obj);
	}
	
	public void clear() {
		sceneObjs.clear();
	}
	
	public ArrayList<SceneObject> getSceneObjects() {
		return sceneObjs;
	}
	
	public void addToBackground(SceneObject obj) {
		backgroundObjs.add(obj);
	}
	
	public void removeFromBackground(SceneObject obj) {
		backgroundObjs.remove(obj);
	}
	
	public void clearBackground() {
		backgroundObjs.clear();
	}
	
	public void add(GUIBase obj) {
		guiObjs.add(obj);
	}
	
	public void remove(GUIBase obj) {
		guiObjs.remove(obj);
	}
	
	public void clearGUIObjects() {
		guiObjs.clear();
	}
	
	public void hideAllGUIObjects() {
		for(GUIBase obj : guiObjs)
			obj.setVisible(false);
	}
	
	public void showAllGUIObjects() {
		for(GUIBase obj : guiObjs)
			obj.setVisible(true);
	}
	
	public void setPhysicsListener(PhysicsListener plistener) {
		this.plistener = plistener;
	}
	
	public void initScene() {
		createBufferStrategy(3);
	}
	
	public void resetTicks() {
		totalTicks = 0;
	}
	
	public void renderScene(GUIText fpsCounter) {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		
//	    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
//	    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g.clearRect(0, 0, getWidth(), getHeight());
		
		for(SceneObject obj : backgroundObjs) {
			obj.render(g);
		}
		
		for(SceneObject obj : sceneObjs) {
			obj.render(g);
		}
		
		for(int i = 0; i < guiObjs.size(); i++) {
			GUIBase obj = guiObjs.get(i);
			if(obj.isVisible())
				obj.render(g);
		}
		
		if(fpsCounter.isVisible())
			fpsCounter.render(g);
		
		g.dispose();
		bs.show();
		
		Toolkit.getDefaultToolkit().sync(); //force the window to redraw and avoid useless waiting
	}
	
	public void tickScene(double dt) {
		collisionGrid.clear(); //clear the grid each tick and re-insert all colliding objects
		
		//draw background objects first
		SceneObject obj;
		for(int i = 0; i < backgroundObjs.size(); i++) {
			obj = backgroundObjs.get(i);
			obj.updateAnimation();
			obj.advanceTick(dt);
		}
		
		//draw all scene objects
		for(int i = 0; i < sceneObjs.size(); i++) {
			obj = sceneObjs.get(i);
			obj.updateAnimation();
			obj.advanceTick(dt);
			
			//only insert if the object has collision
			if(obj.isColliding())
				collisionGrid.insert(obj);
		}
		
		//absolute monster
		//iterate through all grid cells
		//and do a collision check against all objects in a particular cell
		List<List<List<SceneObject>>> grid = collisionGrid.getGrid();
		List<List<SceneObject>> row;
		List<SceneObject> cell;
		
		for(int i = 0; i < grid.size(); i++) {
			row = grid.get(i);
			for(int j = 0; j < row.size(); j++) {
				cell = row.get(j);
				for(int k = 0; k < cell.size(); k++) {
					obj = cell.get(k);
					for(int l = k+1; l < cell.size(); l++) {
						if(obj.isCollidingWith(cell.get(l))) {
							obj.onCollision(cell.get(l));
							cell.get(l).onCollision(obj);
							break;
						}
					}
				}
			}
		}
		
		totalTicks++;
		if(plistener != null)
			plistener.tick(dt, totalTicks); //tick the physics listener
	}
}
