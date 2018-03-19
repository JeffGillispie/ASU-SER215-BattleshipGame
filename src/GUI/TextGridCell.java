package GUI;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class TextGridCell extends JPanel {

	private static final long serialVersionUID = 1L;

	public TextGridCell(String label) {		
		JLabel text = new JLabel(label);
		add(text);
	}

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(20, 20);
    }

}
