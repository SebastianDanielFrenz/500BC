package game.territory;

public class LandedTitle {
	public String name;
	
	private LandedTitle parent;
	public LandedTitle[] children;

	public LandedTitle(LandedTitle parent, LandedTitle[] children) {
		this.parent = parent;
		this.children = children;
	}
}
