//a group container for gui objects to allow easier management

package game.gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import game.scene.Scene;

public class GUIGroup {
	private ArrayList<GUIBase> objs;
	
	public GUIGroup() {
		objs = new ArrayList<GUIBase>();
	}
	
	public void add(GUIBase obj) {
		objs.add(obj);
	}
	
	public void remove(GUIBase obj) {
		objs.remove(obj);
	}
	
	public GUIBase get(int index) {
		return objs.get(index);
	}
	
	public void clear() {
		objs.clear();
	}
	
	public void setVisible(boolean visible) {
		for(GUIBase obj : objs)
			obj.setVisible(visible);
	}
	
	public void addToScene(Scene scene) {
		for(GUIBase obj : objs)
			scene.add(obj);
	}
	
	public void removeFromScene(Scene scene) {
		for(GUIBase obj : objs)
			scene.remove(obj);
	}
}
