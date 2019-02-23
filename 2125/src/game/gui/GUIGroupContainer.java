//a container class for multiple gui groups

package game.gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import game.scene.Scene;

public class GUIGroupContainer {
	private ArrayList<GUIGroup> groups;
	
	public GUIGroupContainer() {
		groups = new ArrayList<GUIGroup>();
	}
	
	public void add(GUIGroup group) {
		groups.add(group);
	}
	
	public void remove(GUIGroup group) {
		groups.remove(group);
	}
	
	public GUIGroup get(int index) {
		return groups.get(index);
	}
	
	public void clear() {
		groups.clear();
	}
	
	public void setVisible(boolean visible) {
		for(GUIGroup group : groups)
			group.setVisible(visible);
	}
}
