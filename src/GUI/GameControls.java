package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import GamePlay.ComputerPlayer;
import GamePlay.Coordinate;
import GamePlay.GameGrid;
import GamePlay.GameGrid.PlacementType;
import GamePlay.HumanPlayer;
import GamePlay.MessageBox;
import GamePlay.Player;
import GamePlay.Ship;

public class GameControls extends JPanel {
	
	private static final long serialVersionUID = 1L;	
	private GameSettings gameSettings = null;
	private boolean playerOneSetup = false;
	private GameBoard gameBoard;
	private int round = 1;
	private Player currentPlayer = null;
	private GameSettings.PlayerType currentPlayerType = null;
	private ShipGrid currentShipGrid = null;
	
	private JLabel s1Label = new JLabel("Select the position and orientation of your carrier (size 5).");
	private JLabel s2Label = new JLabel("Select the position and orientation of your battleship (size 4).");
    private JLabel s3Label = new JLabel("Select the position and orientation of your submarine (size 3).");
    private JLabel s4Label = new JLabel("Select the position and orientation of your crusier (size 3).");
    private JLabel s5Label = new JLabel("Select the position and orienation of your destoryer (size 2).");
    private JTextField playerName = new JTextField();
	private JComboBox<Integer> s1x = new JComboBox<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
	private JComboBox<Integer> s2x = new JComboBox<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
	private JComboBox<Integer> s3x = new JComboBox<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
	private JComboBox<Integer> s4x = new JComboBox<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
	private JComboBox<Integer> s5x = new JComboBox<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
	private JComboBox<Integer> s1y = new JComboBox<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
	private JComboBox<Integer> s2y = new JComboBox<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
	private JComboBox<Integer> s3y = new JComboBox<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
	private JComboBox<Integer> s4y = new JComboBox<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
	private JComboBox<Integer> s5y = new JComboBox<Integer>(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
	private JComboBox<String> s1Orientation = new JComboBox<String>(new String[] { "Horizontal", "Vertical" });
	private JComboBox<String> s2Orientation = new JComboBox<String>(new String[] { "Horizontal", "Vertical" });
	private JComboBox<String> s3Orientation = new JComboBox<String>(new String[] { "Horizontal", "Vertical" });
	private JComboBox<String> s4Orientation = new JComboBox<String>(new String[] { "Horizontal", "Vertical" });
	private JComboBox<String> s5Orientation = new JComboBox<String>(new String[] { "Horizontal", "Vertical" });
	private JButton placeShipsButton = new JButton("Place Ships");	
	private JButton fireShotButton = new JButton("Fire Shot");
	private JButton startGameButton = new JButton("Start Game");
	@SuppressWarnings("rawtypes")
	private JComboBox<GameSettings.PlayerType> playerOneType = new JComboBox<GameSettings.PlayerType>(GameSettings.PlayerType.values());
	@SuppressWarnings("rawtypes")
	private JComboBox<GameSettings.PlayerType> playerTwoType = new JComboBox<GameSettings.PlayerType>(GameSettings.PlayerType.values());
	
	
	public GameControls(GameBoard gameBoard) {
		this.gameBoard = gameBoard; // init game board
		
		// setup button actions
		this.startGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startButtonClick();
	  	    }
	  	});		
		this.placeShipsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shipPlacementContinueButtonClick();
	  	    }
	  	});
		this.fireShotButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				takeShotButtonClick();
			}
		});
		
		// setup start game panel
		getStartGamePanel();		
	}
	
	private void startButtonClick() {
		// init game settings
		this.gameSettings = new GameSettings(
				(GameSettings.PlayerType)playerOneType.getSelectedItem(), 
				(GameSettings.PlayerType)playerTwoType.getSelectedItem());
		
		// reset panel
		this.removeAll();
		this.updateUI();
		
		// player 1 setup
		JPanel player1Setup = getPlayerSetupPanel(this.gameSettings.getPlayerOneType(), "Player One Ship Placement");
		this.add(player1Setup);
		
		if (this.currentPlayerType == GameSettings.PlayerType.Computer)
			shipPlacementContinueButtonClick();
	}
	
	private void shipPlacementContinueButtonClick() {
		Ship[] ships = getShipSettings();
		PlacementType placementResult = GameGrid.canPlaceShips(ships);
		
		if (placementResult == GameGrid.PlacementType.OutOfBounds) {
			MessageBox.show("The current ship placement is invalid, at least one ship is out of bounds. Change the placement settings and try again.");
		}
		else if (placementResult == GameGrid.PlacementType.Overlap) {
			MessageBox.show("The current ship placement is invalid, at least two ships overlap positions. Change the placement settings and try again.");
		}
		else {
			if (!playerOneSetup) {
				Player player1;
				
				if (this.gameSettings.getPlayerOneType() == GameSettings.PlayerType.Human) {
					String name = playerName.getText();
					
					if (name == null || name.isEmpty())
						name = "Player One";
					
					player1 = new HumanPlayer(new GameGrid(), name);
				}
				else {
					player1 = new ComputerPlayer(new GameGrid(), "Computer Player One");
				}
				
				player1.getGrid().placeShips(ships);
				this.gameBoard.setPlayer1(player1);
				
				if (this.currentPlayerType == GameSettings.PlayerType.Human)
					MessageBox.show("Player One Setup Complete.");
				
				playerOneSetup = true;
				resetShipPlacementControls();
				this.removeAll();
				this.updateUI();
				JPanel player2Setup = getPlayerSetupPanel(this.gameSettings.getPlayerTwoType(), "Player Two Ship Placement");
				this.add(player2Setup);
				
				if (this.currentPlayerType == GameSettings.PlayerType.Computer)
					shipPlacementContinueButtonClick();
			}
			else {
				Player player2;
				
				if (this.gameSettings.getPlayerTwoType() == GameSettings.PlayerType.Human) {
					String name = playerName.getText();
					
					if (name == null || name.isEmpty())
						name = "Player Two";
					
					player2 = new HumanPlayer(new GameGrid(), name);
				}
				else {
					player2 = new ComputerPlayer(new GameGrid(), "Computer Player Two");
				}
				
				player2.getGrid().placeShips(ships);
				this.gameBoard.setPlayer2(player2);
				
				if (this.currentPlayerType == GameSettings.PlayerType.Human)
					MessageBox.show("Player Two Setup Complete.");
				
				this.removeAll();
				this.updateUI();
				JPanel turnPanel = getTurnPanel(this.gameBoard.getPlayer1());
				this.add(turnPanel);
				
				if (this.currentPlayerType == GameSettings.PlayerType.Computer)
					takeShotButtonClick();
			}			
		}			
		
	}
	
	private void takeShotButtonClick() {
		Coordinate shotCoordinate = getSelectedCoordinate();
		
		if (shotCoordinate != null) {
			Player opponent = getOpponent();			
			GameGrid.PlacementType shotPlacementType = opponent.getGrid().canPlaceShot(shotCoordinate);
			
			if (shotPlacementType.equals(GameGrid.PlacementType.OutOfBounds))
				MessageBox.show("The shot is out of bounds.", "Invalid Shot");
			else if (shotPlacementType.equals(GameGrid.PlacementType.Overlap))
				MessageBox.show("That shot has already been taken.", "Invalid Shot");
			else { // shot is valid
				GameGrid.ShotType shotResult = opponent.getGrid().placeShot(shotCoordinate);
				
				if (shotResult.equals(GameGrid.ShotType.Miss)) {
					if (this.currentPlayerType == GameSettings.PlayerType.Human)
						MessageBox.show("MISS!", "Shot Result");
					currentShipGrid.getSelectedCell().setBackGroundColor(Color.BLUE);
				}
				else { // shot is hit
					String msg = "HIT!";
					
					for (Ship ship : opponent.getGrid().getFleet().getShips()) {
						if (ship.hasCoordinates(shotCoordinate) && ship.isSunk()) {							
							msg = String.format("HIT! You sunk the %s!", ship.getName());
							break;
						}
					}
					
					if (this.currentPlayerType == GameSettings.PlayerType.Human)
						MessageBox.show(msg, "Shot Result");
					currentShipGrid.getSelectedCell().setBackGroundColor(Color.RED);					
				}
				
				this.removeAll();
				this.updateUI();
				
				if (!this.gameBoard.isGameOver()) {
					if (this.currentPlayer.equals(this.gameBoard.getPlayer2())) // test for end of round
						this.round++; // new round
				
					getOpponentsShipGrid().resetSelectedCell(); // reset cell selection
					JPanel turnPanel = getTurnPanel(opponent); // get new turn panel
					this.add(turnPanel);
					
					if (this.currentPlayerType == GameSettings.PlayerType.Computer)
						takeShotButtonClick();
				}
				else { // game over
					JPanel gameOverPanel = getGameOverPanel();
					this.add(gameOverPanel);
				}
			}			
		}
		else { // no shot selected
			MessageBox.show("No shot coordinate has been selected.", "Invalid Shot");
		}	
	}
	
	private Player getOpponent() { 
		if (this.currentPlayer.equals(this.gameBoard.getPlayer1()))
			return this.gameBoard.getPlayer2();
		else
			return this.gameBoard.getPlayer1();
	}
	
	private ShipGrid getOpponentsShipGrid() {
		if (this.currentShipGrid.equals(this.gameBoard.getShipGrid1()))
				return this.gameBoard.getShipGrid2();
		else
			return this.gameBoard.getShipGrid1();
	}
	
	private JPanel getStartGameTopPanel() {
		JPanel topPane = new JPanel();
		topPane.setLayout(new BoxLayout(topPane, BoxLayout.Y_AXIS));		
        JLabel title = new JLabel("Welcom to BATTLESHIP!");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));
        topPane.add(title);
        topPane.add(Box.createRigidArea(new Dimension(0, 20)));        
        JLabel objectiveTitle = new JLabel("Objective:");
        objectiveTitle.setFont(new Font("Arial", Font.BOLD, 14));
        topPane.add(objectiveTitle);
        topPane.add(new JLabel("To sink all of your opponent's ships by correctly"));
        topPane.add(new JLabel("guessing their location."));
        topPane.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel playTitle = new JLabel("Game Play:");
		playTitle.setFont(new Font("Arial", Font.BOLD, 14));
		topPane.add(playTitle);
		topPane.add(new JLabel("Players take turns firing a shot to attack enemy"));
		topPane.add(new JLabel("ships hidden in the opposing players ship grid."));
		topPane.add(new JLabel("When all cells on a ship are hit the ship"));
		topPane.add(new JLabel("is sunk. The first player to sink all opposing"));
		topPane.add(new JLabel("ships wins the game."));
		topPane.add(Box.createRigidArea(new Dimension(0, 20)));
		return topPane;
	}
	
	private JPanel getStartGameBottomPanel() {
		
		// setup bottom panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(280, 150));
		bottomPanel.setLayout(new GridBagLayout());
		bottomPanel.setBorder(new MatteBorder(1,1,1,1, Color.BLACK));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10, 10, 10, 10);
		
		// setup player one options
		JLabel playerOneLabel = new JLabel("Player One Type");		
		gbc.gridx = 0;
		gbc.gridy = 0;
		bottomPanel.add(playerOneLabel, gbc);		
		gbc.gridx = 1;
		gbc.gridy = 0;
		bottomPanel.add(playerOneType, gbc);
		
		// setup player two options
		JLabel playerTwoLabel = new JLabel("Player Two Type");
		gbc.gridx = 0;
		gbc.gridy = 1;
		bottomPanel.add(playerTwoLabel, gbc);		
		gbc.gridx = 1;
		gbc.gridy = 1;
		bottomPanel.add(playerTwoType, gbc);
        
		// setup start button
		gbc.gridwidth = 3;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 0;
		gbc.gridy = 2;
		bottomPanel.add(this.startGameButton, gbc);		
        
		return bottomPanel;
	}

	private void getStartGamePanel() {
		this.setPreferredSize(new Dimension(300,600));
		this.add(getStartGameTopPanel());
		this.add(getStartGameBottomPanel());
	}
	
	private void setComputerShipPlacement() {
		Ship[] ships = ComputerPlayer.generateShips();
		
		s1x.setSelectedIndex(ships[0].getLocation().getX() - 1);
		s2x.setSelectedIndex(ships[1].getLocation().getX() - 1);
		s3x.setSelectedIndex(ships[2].getLocation().getX() - 1);
		s4x.setSelectedIndex(ships[3].getLocation().getX() - 1);
		s5x.setSelectedIndex(ships[4].getLocation().getX() - 1);
		s1y.setSelectedIndex(ships[0].getLocation().getY() - 1);
		s2y.setSelectedIndex(ships[1].getLocation().getY() - 1);
		s3y.setSelectedIndex(ships[2].getLocation().getY() - 1);
		s4y.setSelectedIndex(ships[3].getLocation().getY() - 1);
		s5y.setSelectedIndex(ships[4].getLocation().getY() - 1);
		s1Orientation.setSelectedIndex(ships[0].getOrientationIndex());
		s2Orientation.setSelectedIndex(ships[1].getOrientationIndex());
		s3Orientation.setSelectedIndex(ships[2].getOrientationIndex());
		s4Orientation.setSelectedIndex(ships[3].getOrientationIndex());
		s5Orientation.setSelectedIndex(ships[4].getOrientationIndex());
	}
	
	private JPanel getPlayerSetupPanel(GameSettings.PlayerType playerType, String title) {
		// prep panel
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();
		int row = 0;
		this.currentPlayerType = playerType;
		
		if (playerType == GameSettings.PlayerType.Computer) {
			setComputerShipPlacement();
		}		
		        
        // player setup title        
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 2;
        layout.insets = new Insets(10, 0, 0, 0);
        JLabel panelTitle = new JLabel(title);
        panelTitle.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(panelTitle, layout); 
        row++;
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 2;
        layout.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel("The position is the position of the top left ship cell."), layout);
        row++;
        
        if (playerType == GameSettings.PlayerType.Human) {
        	layout.gridx = 0;
        	layout.gridy = row;
        	layout.gridwidth = 1;
        	layout.insets = new Insets(10, 0, 0, 0);
        	panel.add(new JLabel("Name"), layout);
        	layout.gridx = 1;
        	playerName.setPreferredSize(new Dimension(150, 20));
        	
        	if (!playerOneSetup)
        		playerName.setText("Player One");
        	else
        		playerName.setText("Player Two");
        	
        	panel.add(playerName, layout);
        	row++;
        	
        }
        
        // ship 1 placement controls
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 2;
        layout.insets = new Insets(10, 0, 0, 0);
        panel.add(s1Label, layout);        
        row++;        
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 1;
        layout.insets = new Insets(0, 0, 0, 0);        
        panel.add(new JLabel("Horizontal Position"), layout);        
        layout.gridx = 1;
        panel.add(s1x, layout);
        row++;
        layout.gridx = 0;
        layout.gridy = row;
        panel.add(new JLabel("Vertical Position"), layout);
        layout.gridx = 1;
        panel.add(s1y, layout);
        row++;
        layout.gridx = 0;
        layout.gridy = row;
        panel.add(new JLabel("Orientation"), layout);
        layout.gridx = 1;
        panel.add(s1Orientation, layout);
        row++;
        
        
     // ship 2 placement controls
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 2;
        layout.insets = new Insets(10, 0, 0, 0);
        panel.add(s2Label, layout);        
        row++;        
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 1;
        layout.insets = new Insets(0, 0, 0, 0);        
        panel.add(new JLabel("Horizontal Position"), layout);        
        layout.gridx = 1;
        panel.add(s2x, layout);
        row++;
        layout.gridx = 0;
        layout.gridy = row;
        panel.add(new JLabel("Vertical Position"), layout);
        layout.gridx = 1;
        panel.add(s2y, layout);
        row++;
        layout.gridx = 0;
        layout.gridy = row;
        panel.add(new JLabel("Orientation"), layout);
        layout.gridx = 1;
        panel.add(s2Orientation, layout);
        row++;
		
		// ship 3 placement controls
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 2;
        layout.insets = new Insets(10, 0, 0, 0);
        panel.add(s3Label, layout);        
        row++;        
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 1;
        layout.insets = new Insets(0, 0, 0, 0);        
        panel.add(new JLabel("Horizontal Position"), layout);        
        layout.gridx = 1;
        panel.add(s3x, layout);
        row++;
        layout.gridx = 0;
        layout.gridy = row;
        panel.add(new JLabel("Vertical Position"), layout);
        layout.gridx = 1;
        panel.add(s3y, layout);
        row++;
        layout.gridx = 0;
        layout.gridy = row;
        panel.add(new JLabel("Orientation"), layout);
        layout.gridx = 1;
        panel.add(s3Orientation, layout);
        row++;
		
		// ship 4 placement controls
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 2;
        layout.insets = new Insets(10, 0, 0, 0);
        panel.add(s4Label, layout);        
        row++;        
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 1;
        layout.insets = new Insets(0, 0, 0, 0);        
        panel.add(new JLabel("Horizontal Position"), layout);        
        layout.gridx = 1;
        panel.add(s4x, layout);
        row++;
        layout.gridx = 0;
        layout.gridy = row;
        panel.add(new JLabel("Vertical Position"), layout);
        layout.gridx = 1;
        panel.add(s4y, layout);
        row++;
        layout.gridx = 0;
        layout.gridy = row;
        panel.add(new JLabel("Orientation"), layout);
        layout.gridx = 1;
        panel.add(s4Orientation, layout);
        row++;
		
		// ship 5 placement controls
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 2;
        layout.insets = new Insets(10, 0, 0, 0);
        panel.add(s5Label, layout);        
        row++;        
        layout.gridx = 0;
        layout.gridy = row;
        layout.gridwidth = 1;
        layout.insets = new Insets(0, 0, 0, 0);        
        panel.add(new JLabel("Horizontal Position"), layout);        
        layout.gridx = 1;
        panel.add(s5x, layout);
        row++;
        layout.gridx = 0;
        layout.gridy = row;
        panel.add(new JLabel("Vertical Position"), layout);
        layout.gridx = 1;
        panel.add(s5y, layout);
        row++;
        layout.gridx = 0;
        layout.gridy = row;
        panel.add(new JLabel("Orientation"), layout);
        layout.gridx = 1;
        panel.add(s5Orientation, layout);
        row++;
        
        // place ships button
        layout.gridx = 0;
        layout.gridy = row;
        layout.insets = new Insets(10, 0, 0, 0);
        layout.gridwidth = 4;
        panel.add(placeShipsButton, layout);
                        
        return panel;
	}
	
	private Ship[] getShipSettings() {
		return new Ship[] {
				new Ship(5, "Carrier", Ship.Orientation.valueOf((String)s1Orientation.getSelectedItem()), new Coordinate((int)s1x.getSelectedItem(), (int)s1y.getSelectedItem())),
				new Ship(4, "Battleship", Ship.Orientation.valueOf((String)s2Orientation.getSelectedItem()), new Coordinate((int)s2x.getSelectedItem(), (int)s2y.getSelectedItem())),
				new Ship(3, "Submarine", Ship.Orientation.valueOf((String)s3Orientation.getSelectedItem()), new Coordinate((int)s3x.getSelectedItem(), (int)s3y.getSelectedItem())),
				new Ship(3, "Crusier", Ship.Orientation.valueOf((String)s4Orientation.getSelectedItem()), new Coordinate((int)s4x.getSelectedItem(), (int)s4y.getSelectedItem())),
				new Ship(2, "Destroyer", Ship.Orientation.valueOf((String)s5Orientation.getSelectedItem()), new Coordinate((int)s5x.getSelectedItem(), (int)s5y.getSelectedItem()))
		};
	}
	
	private void resetShipPlacementControls() {
		s1x.setSelectedIndex(0);
		s2x.setSelectedIndex(0);
		s3x.setSelectedIndex(0);
		s4x.setSelectedIndex(0);
		s5x.setSelectedIndex(0);
		s1y.setSelectedIndex(0);
		s2y.setSelectedIndex(0);
		s3y.setSelectedIndex(0);
		s4y.setSelectedIndex(0);
		s5y.setSelectedIndex(0);
		s1Orientation.setSelectedIndex(0);
		s2Orientation.setSelectedIndex(0);
		s3Orientation.setSelectedIndex(0);
		s4Orientation.setSelectedIndex(0);
		s5Orientation.setSelectedIndex(0);
	}
	
	private Coordinate getSelectedCoordinate() { return this.currentShipGrid.getSelectedCellCoordinate(); }
	
	private JPanel getTurnPanel(Player player) {
		
		// prep panel
		this.currentPlayer = player;
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();
		int row = 0;
		Coordinate selectedCellCoordinate = null; // init selected cell
		
		if (this.gameBoard.getPlayer1().equals(player)) {// the current player is player #1
			selectedCellCoordinate = this.gameBoard.getShipGrid2().getSelectedCellCoordinate();
			this.currentShipGrid = this.gameBoard.getShipGrid2();
			this.currentPlayerType = this.gameSettings.getPlayerOneType();
		}
		else { // the current player is player #2
			selectedCellCoordinate = this.gameBoard.getShipGrid1().getSelectedCellCoordinate();
			this.currentShipGrid = this.gameBoard.getShipGrid1();
			this.currentPlayerType = this.gameSettings.getPlayerTwoType();
		}
			
		// round label
		layout.gridx = 0;
		layout.gridy = row;
		layout.insets = new Insets(10, 10, 10, 10);
		JLabel roundLabel = new JLabel(String.format("Round %d", this.round));
		roundLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		panel.add(roundLabel, layout);
		row++;
		
		// cell color descritions
		JLabel hitDescription = new JLabel("Red cells are hits.");
		JLabel missDescription = new JLabel("Blue cells are misses.");
		hitDescription.setForeground(Color.RED);
		missDescription.setForeground(Color.BLUE);
		hitDescription.setFont(new Font("Arial", Font.BOLD, 16));
		missDescription.setFont(new Font("Arial", Font.BOLD, 16));
		layout.gridx = 0;
		layout.gridy = row;
		layout.insets = new Insets(10, 10, 10, 10);
		panel.add(hitDescription, layout);
		row++;
		layout.gridx = 0;
		layout.gridy = row;
		layout.insets = new Insets(10, 10, 10, 10);
		panel.add(missDescription, layout);
		row++;
				
		// player stats
		layout.gridx = 0;
		layout.gridy = row;
		layout.insets = new Insets(10, 10, 10, 10);
		JLabel turnLabel = new JLabel(String.format("%s's Turn", player.getName()));
		
		if (this.gameBoard.getPlayer1().equals(player))
			turnLabel.setForeground(new Color(102, 0, 102));
		else
			turnLabel.setForeground(new Color(219, 146, 19));
		
		turnLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		panel.add(turnLabel, layout);
		row++;
		layout.gridx = 0;
		layout.gridy = row;
		layout.insets = new Insets(10, 10, 10, 10);
		JLabel shipLabel = new JLabel(String.format("Ships Sunk: %d", getOpponent().getGrid().getShipsSunk()));
		shipLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		panel.add(shipLabel, layout);
		row++;
		layout.gridx = 0;
		layout.gridy = row;
		layout.insets = new Insets(10, 10, 10, 10);
		JLabel hitLabel = new JLabel(String.format("Total Hits: %d", getOpponent().getGrid().getHitCount()));
		hitLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		panel.add(hitLabel, layout);
		row++;
		layout.gridx = 0;
		layout.gridy = row;
		layout.insets = new Insets(10, 10, 10, 10);
		JLabel missLabel = new JLabel(String.format("Total Misses: %d", getOpponent().getGrid().getMissCount()));
		missLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		panel.add(missLabel, layout);
		row++;
		layout.gridx = 0;
		layout.gridy = row;
		layout.insets = new Insets(10, 10, 10, 10);		
		String selectedCellAddress;
		
		if (selectedCellCoordinate == null && this.currentPlayerType == GameSettings.PlayerType.Human)
			selectedCellAddress = "(No Cell Selected)";
		else if (selectedCellCoordinate == null && this.currentPlayerType == GameSettings.PlayerType.Computer) {
			selectedCellCoordinate = ComputerPlayer.takeAShot(getOpponent().getGrid());
			selectedCellAddress = String.format("(%d,  %d)", selectedCellCoordinate.getX(), selectedCellCoordinate.getY());
			this.currentShipGrid.setSelectedCell(selectedCellCoordinate);
		}
		else
			selectedCellAddress = String.format("(%d,  %d)", selectedCellCoordinate.getX(), selectedCellCoordinate.getY());
		
		JLabel shotLabel = new JLabel(String.format("Selected Cell: %s", selectedCellAddress));
		shotLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		panel.add(shotLabel, layout);
		row++;
		
		layout.gridx = 0;
		layout.gridy = row;
		layout.insets = new Insets(10, 10, 10, 10);		
		panel.add(this.fireShotButton, layout);
		row++;
		
		return panel;
	}
	
	public void updateTurnPanel() {
		if (this.round > 0) {
			this.removeAll();
			this.updateUI();
			JPanel turnPanel = getTurnPanel(this.currentPlayer);
			this.add(turnPanel);
		}		
	}
	
	private JPanel getGameOverPanel() {
		// prep panel
		String winner;
		
		if (this.gameBoard.getPlayer1().getGrid().getShipsSunk() == 5)
			winner = this.gameBoard.getPlayer2().getName();
		else
			winner = this.gameBoard.getPlayer1().getName();
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints layout = new GridBagConstraints();
		int row = 0;
		row = addLabelToPanel(panel, layout, row, "GAME OVER");
		row = addLabelToPanel(panel, layout, row, "Winner: " + winner);
		row = addLabelToPanel(panel, layout, row, "Rounds: " + this.round);
		row = addLabelToPanel(panel, layout, row, " ");
		row = addLabelToPanel(panel, layout, row, this.gameBoard.getPlayer1().getName() + " Game Results");
		row = addLabelToPanel(panel, layout, row, String.format("Ships Sunk: %d", this.gameBoard.getPlayer2().getGrid().getShipsSunk()));
		row = addLabelToPanel(panel, layout, row, String.format("Total Hits: %d", this.gameBoard.getPlayer2().getGrid().getHitCount()));
		row = addLabelToPanel(panel, layout, row, String.format("Total Misses: %d", this.gameBoard.getPlayer2().getGrid().getMissCount()));
		row = addLabelToPanel(panel, layout, row, " ");
		row = addLabelToPanel(panel, layout, row, this.gameBoard.getPlayer2().getName() + " Game Results");
		row = addLabelToPanel(panel, layout, row, String.format("Ships Sunk: %d", this.gameBoard.getPlayer1().getGrid().getShipsSunk()));
		row = addLabelToPanel(panel, layout, row, String.format("Total Hits: %d", this.gameBoard.getPlayer1().getGrid().getHitCount()));
		row = addLabelToPanel(panel, layout, row, String.format("Total Misses: %d", this.gameBoard.getPlayer1().getGrid().getMissCount()));
		return panel;
	}
	
	private int addLabelToPanel(JPanel panel, GridBagConstraints layout, int row, String text) {
		layout.gridx = 0;
		layout.gridy = row;
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.add(label, layout);
		return row + 1;
	}
	
	public ShipGrid getCurrentShipGrid() { return this.currentShipGrid; }
	public Player getCurrentPlayer() { return this.currentPlayer; }
}
