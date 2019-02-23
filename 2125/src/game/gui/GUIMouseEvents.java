//callbacks called when certain events occur on a GUI object

package game.gui;

import java.awt.event.MouseEvent;

public interface GUIMouseEvents {
	public void onClick(MouseEvent e);
	public void onHover();
	public void onHoverStop();
}
