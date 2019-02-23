//divides the world into a grid and inserts objects into the cells they overlap
//used for collision

package game.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.scene.SceneObject;

public class SpatialGrid {
	private int size;
	private List<List<List<SceneObject>>> containers;
	
	public SpatialGrid(int size, int width, int height) {
		this.size = size;
		
		width = (int) Math.ceil((double)width/(double)size);
		height = (int) Math.ceil((double)height/(double)size);
		
		containers = new ArrayList<List<List<SceneObject>>>(height);
		
		for(int i = 0; i < height; i++) {
			List<List<SceneObject>> cell = new ArrayList<List<SceneObject>>();
			for(int j = 0; j < width; j++)
				cell.add(new ArrayList<SceneObject>());
			
			containers.add(cell);
		}
	}
	
	public void clear() {
		for(int i = 0; i < containers.size(); i++) {
			for(int j = 0; j < containers.get(i).size(); j++)
				containers.get(i).get(j).clear();
		}
	}
	
	public void insert(SceneObject obj) {
		int x = (int)(obj.getX()/size);
		int y = (int)(obj.getY()/size);
		int xx = (int)(obj.getX() + obj.getWidth())/size;
		int yy = (int)(obj.getY() + obj.getHeight())/size;
		
		x = Math.max(0, x);
		y = Math.max(0, y);
		
		xx = Math.min(containers.get(0).size()-1, xx);
		yy = Math.min(containers.size()-1, yy);
		
		for(int i = y; i <= yy; i++) {
			for(int j = x; j <= xx; j++)
				containers.get(i).get(j).add(obj);
		}
	}
	
	public List<List<List<SceneObject>>> getGrid() {
		return containers;
	}
	
	//debug function to draw the grid
	public void draw(Graphics2D g) {
		for(int i = 0; i < containers.size(); i++) {
			for(int j = 0; j < containers.get(i).size(); j++) {
				if(containers.get(i).get(j).size() > 0)
					g.setColor(new Color(255, 0, 255));
				else
					g.setColor(new Color(0, 0, 0));
				
				g.drawRect(j*size, i*size, size, size);
			}
		}
	}
}
