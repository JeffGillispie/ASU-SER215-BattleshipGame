package GUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import GamePlay.Coordinate;

public class ShipGrid extends JPanel {

	private static final long serialVersionUID = 1L;
	private int numRows = 10;
	private int numCols = 10;
	private Coordinate selectedCellCoordinate;
	private GridCell selectedCell;
	private GameBoard gameBoard;

    public ShipGrid(GameBoard gameBoard) {
    	this.gameBoard = gameBoard;
    	selectedCellCoordinate = null;
    	selectedCell = null;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        addColumnHeader(gbc);
        
        for (int row = 1; row <= numRows; row++) {
            for (int col = 0; col <= numCols; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                
                if (col == 0) {
                	TextGridCell rowHeader = new TextGridCell(Integer.toString(row));
                	add(rowHeader, gbc);
                }
                else {                
	                GridCell cell = new GridCell(this);
	                Border border = new MatteBorder(1,1,1,1, Color.BLACK);                
	                cell.setBorder(border);
	                add(cell, gbc);
                }
            }
        }
    }
    
    private void addColumnHeader(GridBagConstraints gbc) {
    	for (int col = 1; col <= numCols; col++) {
    		gbc.gridx = col;
    		gbc.gridy = 0;
    		TextGridCell cell = new TextGridCell(Integer.toString(col));
    		add(cell, gbc);
    	}
    }
    
    public void setSelectedCell(Coordinate coordinate, GridCell cell) {
    	this.selectedCellCoordinate = coordinate;
    	this.selectedCell = cell;
    	this.gameBoard.updateTurnInfo(this);
    }
    
    public void setSelectedCell(Coordinate coordinate) {
    	this.selectedCellCoordinate = coordinate;
    	
    	GridCell match = null;
    	GridBagLayout layout = (GridBagLayout)this.getLayout();
    	
    	for (Component comp : this.getComponents()) {
    		GridBagConstraints gbc = layout.getConstraints(comp);
    		if (gbc.gridx == coordinate.getX() && gbc.gridy == coordinate.getY() && comp instanceof GridCell) {
    			match = (GridCell)comp;
    			break;
    		}
    	}
    	
    	this.selectedCell = match;
    }
    
    public void resetSelectedCell() {
    	this.selectedCell = null;
    	this.selectedCellCoordinate = null;
    }
    
    public Coordinate getSelectedCellCoordinate() { return this.selectedCellCoordinate; }
    public GridCell getSelectedCell() { return this.selectedCell; }
}
