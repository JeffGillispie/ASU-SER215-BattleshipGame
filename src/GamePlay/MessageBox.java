package GamePlay;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MessageBox {
	
	public static void show(String message, String title) {
		JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel pane = new JPanel();
        pane.setPreferredSize(new Dimension(400, 200));
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        JTextArea textArea = new JTextArea();
        textArea.setSize(380, 180);
        textArea.setText(message);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        pane.add(textArea, gbc);
        JButton button = new JButton("OK");
        gbc.gridx = 0;
        gbc.gridy = 1;
        button.addActionListener(new ActionListener() {
  	      public void actionPerformed(ActionEvent e) {
  	        frame.dispose();
  	      }
  	    });
        pane.add(button, gbc);
        frame.add(pane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	public static void show(String message) {
		show(message, "");
	}
}
