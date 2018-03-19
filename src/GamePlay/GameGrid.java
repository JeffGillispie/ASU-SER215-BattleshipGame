package GamePlay;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.awt.geom.Line2D;

public class GameGrid {
	
	public enum PlacementType {
		Valid,
		OutOfBounds,
		Overlap
	}
	
	public enum ShotType {
		Hit,
		Miss
	}
	
	private Fleet fleet;
	private List<Coordinate> shots;
	
	public GameGrid() {
		this.shots = new ArrayList<Coordinate>();
	}

	public PlacementType canPlaceShot(Coordinate coordinate) {
		
		boolean overlap = false;
		
		for (Coordinate shot : this.shots) {
			if (shot.getX() == coordinate.getX() && shot.getY() == coordinate.getY()) {
				overlap = true;
				break;
			}
		}
		
		if (coordinate.getX() < 0 || coordinate.getX() > 10)
			return PlacementType.OutOfBounds;
		else if (coordinate.getY() < 0 || coordinate.getY() > 10)
			return PlacementType.OutOfBounds;
		else if (overlap) 
			return PlacementType.Overlap;
		else
			return PlacementType.Valid;
	}
	
	public static PlacementType canPlaceShips(Ship[] ships) {
		boolean outofbounds = false; // init outofbounds
		boolean overlap = false; // init overlap
		
		for (Ship ship : ships) {			
			
			// get ship line coords
			int x1 = ship.getLocation().getX();
			int x2 = ship.getLocation().getX();
			int y1 = ship.getLocation().getY();
			int y2 = ship.getLocation().getY();
			
			// update ending coords based on orientation
			if (ship.getOrientation() == Ship.Orientation.Horizontal)
				x2 = x2 - 1 + ship.getSize();
			else
				y2 = y2 - 1 + ship.getSize();
			
			if (x1 < 0 || x1 > 10 || x2 < 0 || x2 > 10) // check if x coords are in grid bounds
				outofbounds = true;
			else if (y1 < 0 || y1 > 10 || y2 < 0 || y2 > 10) // check if y coords are in grid bounds
				outofbounds = true;
		}		
		
		// check for overlap
		overlap = hasOverlappingShips(ships);
		
		if (outofbounds) 
			return PlacementType.OutOfBounds;		
		else if (overlap)
			return PlacementType.Overlap;
		else
			return PlacementType.Valid;
	}
	
	private static boolean shipsOverlap(Ship ship1, Ship ship2) {
		
		// get ship 1 line coords
		int s1x1 = ship1.getLocation().getX();
		int s1x2 = ship1.getLocation().getX();
		int s1y1 = ship1.getLocation().getY();
		int s1y2 = ship1.getLocation().getY();
		
		// update ending coords based on orientation
		if (ship1.getOrientation() == Ship.Orientation.Horizontal)
			s1x2 = s1x2 - 1 + ship1.getSize();
		else
			s1y2 = s1y2 - 1 + ship1.getSize();		
		
		// get ship 2 line coords
		int s2x1 = ship2.getLocation().getX();
		int s2x2 = ship2.getLocation().getX();
		int s2y1 = ship2.getLocation().getY();
		int s2y2 = ship2.getLocation().getY();
		
		// update ending coords based on orientation
		if (ship2.getOrientation() == Ship.Orientation.Horizontal)
			s2x2 = s2x2 - 1 + ship2.getSize();
		else
			s2y2 = s2y2 - 1 + ship2.getSize();
		
		Line2D line1 = new Line2D.Float(s1x1, s1y1, s1x2, s1y2);
		Line2D line2 = new Line2D.Float(s2x1, s2y1, s2x2, s2y2);
		return line2.intersectsLine(line1);
	}

	private static boolean hasOverlappingShips(Ship[] ships) {
		boolean overlap = false;
		
		Queue<Ship> shipQueue = new LinkedList<Ship>();
		
		for (Ship ship : ships)
			shipQueue.add(ship);
		
		while (shipQueue.size() > 0) {
					
			Ship currentShip = shipQueue.remove();
			Ship[] remainingShips = shipQueue.toArray(new Ship[shipQueue.size()]);
			
			for (Ship remainingShip : remainingShips) {
				if (shipsOverlap(currentShip, remainingShip)) {
					overlap = true;
					break;
				}
			}
			
			if (overlap)
				break;
		}
		
		return overlap;
	}
	
	public void placeShips(Ship[] ships) {
		this.fleet = new Fleet(ships[0], ships[1], ships[2], ships[3], ships[4]);
	}
	
	public ShotType placeShot(Coordinate coordinate) {

		ShotType shot = ShotType.Miss;
		
		if (canPlaceShot(coordinate) == PlacementType.Valid) {
			this.shots.add(coordinate);
			
			for (Ship ship : this.fleet.getShips()) {
				if (ship.hasCoordinates(coordinate)) {
					ship.shipHit(coordinate);
					shot = ShotType.Hit;
					break;
				}				
			}
		}
		
		return shot;
	}
	
	public Fleet getFleet() { return this.fleet; }
	
	public Coordinate[] getShots() { return this.shots.toArray(new Coordinate[this.shots.size()]); }
	
	public int getHitCount() {
		int totalHits = 0;
		Ship[] ships = this.fleet.getShips();
		
		for (Ship ship : ships) {
			totalHits += ship.getHits().size();
		}
		
		return totalHits;
	}
	
	public int getMissCount() {
		int hits = getHitCount();		
		return this.shots.size() - hits;
	}

	public int getShipsSunk() {
		int sunkShips = 0;
		
		for (Ship ship : this.fleet.getShips()) {
			if (ship.isSunk())
				sunkShips++;
		}
		
		return sunkShips;
	}
}
