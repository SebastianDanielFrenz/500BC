package game.gui;

import java.util.List;

public class LayoutManager {
	
	public LayoutManager(int lowX, int highX, int lowY, int highY) {
		this.lowX = lowX;
		this.highX = highX;
		this.lowY = lowY;
		this.highY = highY;
	}

	private int lowX,highX,lowY,highY;
	private List<LayoutObject> objects;
	
}
