package GamePlay;
import java.util.List;
import java.util.ArrayList;

public class Fleet {
	protected Ship aircraftCarrier;
	protected Ship battleship;
	protected Ship submarine;
	protected Ship crusier;
	protected Ship destroyer;
	
	public Fleet(Ship aircraftCarrier, Ship battleship, Ship submarine, Ship crusier, Ship destroyer) {
		this.aircraftCarrier = aircraftCarrier;
		this.battleship = battleship;
		this.submarine = submarine;
		this.crusier = crusier;
		this.destroyer = destroyer;
	}
	
	public Ship[] getShips() {
		List<Ship> ships = new ArrayList<Ship>();
		ships.add(this.aircraftCarrier);
		ships.add(this.battleship);
		ships.add(this.submarine);
		ships.add(this.crusier);
		ships.add(this.destroyer);
		return ships.toArray(new Ship[ships.size()]);
	}
}
