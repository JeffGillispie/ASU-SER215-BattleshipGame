package GamePlay;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class ComputerPlayer extends Player {

	public ComputerPlayer(GameGrid gameGrid, String name) {
		super(gameGrid, name);
	}
	
	public static Coordinate takeAShot(GameGrid gameGrid) {
		int x = 0;
		int y = 0;
		Coordinate coordinate = new Coordinate(x, y);
		
		do {
			Random rx = new Random();
			Random ry = new Random();
			
			x = rx.nextInt(10) + 1;
			y = ry.nextInt(10) + 1;
			
			coordinate = new Coordinate(x, y);
		} while (gameGrid.canPlaceShot(coordinate) != GameGrid.PlacementType.Valid);
		
		return coordinate;
	}
	
	public static Ship[] generateShips() {		
		List<Ship> ships = new ArrayList<Ship>();
		
		do {
			ships = new ArrayList<Ship>();
			List<Ship> shipPrototypes = new ArrayList<Ship>();
			
			shipPrototypes.add(new Ship(5, "Carrier", Ship.Orientation.Horizontal, new Coordinate(1,1)));
			shipPrototypes.add(new Ship(4, "Battleship", Ship.Orientation.Horizontal, new Coordinate(1,1)));
			shipPrototypes.add(new Ship(3, "Submarine", Ship.Orientation.Horizontal, new Coordinate(1,1)));
			shipPrototypes.add(new Ship(3, "Crusier", Ship.Orientation.Horizontal, new Coordinate(1,1)));
			shipPrototypes.add(new Ship(2, "Destroyer", Ship.Orientation.Horizontal, new Coordinate(1,1)));
			
			int x = 0;
			int y = 0;
			boolean isHorizontal = false;
			Ship.Orientation orientation;
			
			while (ships.size() < 5) {
				Random rx = new Random();
				Random ry = new Random();
				Random rz = new Random();
				
				x = rx.nextInt(10) + 1;
				y = ry.nextInt(10) + 1;
				isHorizontal = rz.nextBoolean();
				
				if (isHorizontal)
					orientation = Ship.Orientation.Horizontal;
				else
					orientation = Ship.Orientation.Vertical;
				
				Ship testShip = new Ship(
						shipPrototypes.get(0).getSize(), 
						shipPrototypes.get(0).getName(),
						orientation,
						new Coordinate(x, y));
				
				ships.add(testShip);
				shipPrototypes.remove(0);
			}
		} while (GameGrid.canPlaceShips(ships.toArray(new Ship[ships.size()])) != GameGrid.PlacementType.Valid);
		
		return ships.toArray(new Ship[ships.size()]);
	}

}
