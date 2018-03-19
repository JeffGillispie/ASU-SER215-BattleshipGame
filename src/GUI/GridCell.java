package GUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import GamePlay.Coordinate;
import GamePlay.MessageBox;

public class GridCell extends JPanel {

	private static final long serialVersionUID = 1L;
	private Color defaultBackground = null;
	private ShipGrid parentShipGrid;

	public GridCell(ShipGrid parent) {
		this.parentShipGrid = parent;
		this.addMouseListener(new MouseAdapter() {
			@Override
	        public void mouseEntered(MouseEvent e) {			
	            if (defaultBackground == null)
	            	defaultBackground = getBackground();
	            
	            if (getBackground().equals(defaultBackground))
	            	setBackground(Color.GREEN);
	        }
	
	        @Override
	        public void mouseExited(MouseEvent e) {
	        	if (getBackground().equals(Color.GREEN))
	        		setBackground(defaultBackground);
	        }
	         
	        @Override
	        public void mousePressed(MouseEvent e) {
	        	if (getBackground().equals(Color.GREEN))
	        		setBackground(Color.YELLOW);
	        	
	        	setSelectedCell();
	        }
	     });		
	}

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(20, 20);
    }

    private void setSelectedCell() {
    	try {
    		GridBagLayout layout = (GridBagLayout)this.getParent().getLayout();
    		GridBagConstraints gbc = (GridBagConstraints)layout.getConstraints(this);
        	Coordinate coordinate = new Coordinate(gbc.gridx, gbc.gridy);
        	ShipGrid parent = this.parentShipGrid;
        	GridCell lastCell = parent.getSelectedCell();
        	
        	if (lastCell != null)
        		lastCell.resetBackGroundColor();
        	
        	parent.setSelectedCell(coordinate, this);
    	}
    	catch (Exception ex) {
    		MessageBox.show(ex.getMessage());
    	}
    }
    
    public void setBackGroundColor(Color color) {
    	setBackground(color);
    }
    
    public void resetBackGroundColor() {
    	if (getBackground().equals(Color.YELLOW))
    		setBackground(defaultBackground);
    }
}
