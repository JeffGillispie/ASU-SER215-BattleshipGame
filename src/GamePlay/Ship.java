package GamePlay;
import java.util.ArrayList;
import java.util.List;

public class Ship {
	
	public enum Orientation {
		Horizontal,
		Vertical
	}
	
	private int size;
	private String name;
	private Orientation orientation;
	private Coordinate location;
	private List<Coordinate> hits;
	
	public Ship(int size, String name, Orientation orientation, Coordinate location) {
		this.size = size;
		this.name = name;
		this.orientation = orientation;
		this.location = location;
		this.hits = new ArrayList<Coordinate>();
	}
	
	public int getSize() { return this.size; }
	public String getName() { return this.name; }
	public Orientation getOrientation() { return this.orientation; }
	public Coordinate getLocation() { return this.location; }
	public boolean hasCoordinates(Coordinate coordinate) {
		boolean hasCoordinate = false;
		
		int x = coordinate.getX();
		int y = coordinate.getY();
		int x1 = this.location.getX();
		int y1 = this.location.getY();
		int x2 = this.location.getX();
		int y2 = this.location.getY();
		
		if (this.orientation == Orientation.Horizontal)
			x2 = x2 - 1 + this.size;
		else 
			y2 = y2 - 1 + this.size;
		
		if (x >= x1 && x <= x2 && y >= y1 && y <= y2)
			hasCoordinate = true;
		
		return hasCoordinate;
	}
	public List<Coordinate> getHits() { return this.hits; }
	public void shipHit(Coordinate coordinate) {
		if (hasCoordinates(coordinate) && !this.hits.contains(coordinate))
			this.hits.add(coordinate);
	}

	public boolean isSunk() {
		if (this.size == this.hits.size())
			return true;
		else return false;
	}
	
	public int getOrientationIndex() {
		if (this.orientation == Orientation.Horizontal)
			return 0;
		else 
			return 1;
	}
}
