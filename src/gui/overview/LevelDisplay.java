package gui.overview;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class LevelDisplay extends JComponent {
	private static final long serialVersionUID = 1L;

	public LevelDisplay(final int level) {
		setLayout(new FlowLayout());
		setBorder(BorderFactory.createTitledBorder("Level " + level));
		// add(new JLabel("" + level), BorderLayout.WEST);
		// add(new JLabel("relopts go here"));
	}

}
