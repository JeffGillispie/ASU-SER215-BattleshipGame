package GamePlay;

public class Player {
	
	protected GameGrid gameGrid;
	protected String name;
	
	public Player(GameGrid gameGrid, String name) {
		this.gameGrid = gameGrid;
		this.name = name;
	}
	
	public String getName() { return this.name; }
	public GameGrid getGrid() { return this.gameGrid; }
}
