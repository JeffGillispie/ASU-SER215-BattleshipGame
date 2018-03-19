package GUI;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import GamePlay.MessageBox;
import GamePlay.Player;

public class GameBoard extends JPanel{

	private static final long serialVersionUID = 1L;
	private Player player1;
	private Player player2;
	private ShipGrid shipGrid1;
	private ShipGrid shipGrid2;
	private GameControls gameControls;

	public GameBoard() {
		player1 = null;
		player2 = null;
		this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        int gridColRow = 0;
        
        // add title
        JLabel title = new JLabel("Battleship Game");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = gridColRow;
        gridColRow++;
        this.add(title, gbc);
        JSeparator titleSeparator = new JSeparator();
        titleSeparator.setPreferredSize(new Dimension(620, 1));
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = gridColRow;
        gridColRow++;
        this.add(titleSeparator, gbc);
        gbc.gridwidth = 1; // reset grid width
        
        // setup player 1 ship grid
        JLabel player1GridLabel = new JLabel("Player 1 Grid");
        gbc.insets = new Insets(25, 10, 15, 10);
        gbc.gridx = 0;
        gbc.gridy = gridColRow;
        gridColRow++;
        this.add(player1GridLabel, gbc);
        this.shipGrid1 = new ShipGrid(this);        
        gbc.insets = new Insets(0, 10, 40, 10);
        gbc.gridx = 0;
        gbc.gridy = gridColRow;
        gridColRow++;
        this.add(shipGrid1, gbc);
        
        // add grid separator
        JSeparator gridSeparator = new JSeparator();
        gridSeparator.setPreferredSize(new Dimension(300, 1));
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy = gridColRow;
        gridColRow++;
        this.add(gridSeparator, gbc);
        
        // setup player 2 ship grid
        JLabel player2GridLabel = new JLabel("Player 2 Grid");
        gbc.insets = new Insets(25, 10, 0, 10);
        gbc.gridx = 0;
        gbc.gridy = gridColRow;
        gridColRow++;
        this.add(player2GridLabel, gbc);
        this.shipGrid2 = new ShipGrid(this);
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = gridColRow;
        gridColRow++;
        this.add(shipGrid2, gbc);
        
        // game play controls separator
        JSeparator controlSeparator = new JSeparator(JSeparator.VERTICAL);
        controlSeparator.setPreferredSize(new Dimension(10, 620));
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridheight = 5;
        gbc.gridx = 1;
        gbc.gridy = 2;        
        this.add(controlSeparator, gbc);
        gbc.gridheight = 1; // reset grid height
        
        // setup game play controls
        this.gameControls = new GameControls(this);
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridheight = 5;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 2;
        gbc.gridy = 2;
        this.add(gameControls, gbc);
	}

	public void setPlayer1(Player player) { this.player1 = player; }
	public void setPlayer2(Player player) { this.player2 = player; }
	public Player getPlayer1() { return this.player1; }
	public Player getPlayer2() { return this.player2; }
	public ShipGrid getShipGrid1() { return shipGrid1; }
	public ShipGrid getShipGrid2() { return shipGrid2; }
	public void updateTurnInfo(ShipGrid shipGrid) {
		GameControls controls = this.gameControls;
		ShipGrid currentShipGrid = controls.getCurrentShipGrid();
		
		if (shipGrid.equals(currentShipGrid))
			this.gameControls.updateTurnPanel();
		else {
			if (shipGrid.getSelectedCell() != null)
				shipGrid.getSelectedCell().resetBackGroundColor();
			
			MessageBox.show("Please click a cell in the opponents game grid.");
		}
	}
	
	public boolean isGameOver() {
		
		if (this.gameControls.getCurrentPlayer().equals(this.player1)) {
			if (this.player2.getGrid().getShipsSunk() == 5)
				return true;
			else
				return false;
		}
		else { 
			if (this.player1.getGrid().getShipsSunk() == 5)
				return true;
			else 
				return false;
		}	
	}
}
