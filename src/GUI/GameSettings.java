package GUI;

public class GameSettings {
	
	public enum PlayerType {
		Human,
		Computer
	}
	
	private PlayerType playerOneType;
	private PlayerType playerTwoType;
	
	public GameSettings(PlayerType playerOneType, PlayerType playerTwoType) {
		this.playerOneType = playerOneType;
		this.playerTwoType = playerTwoType;
	}
	
	public PlayerType getPlayerOneType() { return playerOneType; }
	public PlayerType getPlayerTwoType() { return playerTwoType; }
}
