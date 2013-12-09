package gui.overview;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

/**
 * An Overview holds several blocks where each block represents one step within
 * the dynamic programming.<br>
 * Within these blocks all considered plans of that step (all 1-relation plans
 * on the first level, all 2-relation plans on the second...) are displayed.
 * 
 * @author Daniel
 * 
 */
public class Overview extends JComponent {
	private static final long serialVersionUID = 1L;
	private final Map<Integer, LevelDisplay> _levels;

	/**
	 * Constructor
	 */
	public Overview() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_levels = new HashMap<Integer, LevelDisplay>();
	}

	/**
	 * Add a new level (eg a new step of the path-generator
	 * 
	 * @param level
	 *            the number of this level, has to be unique or will fail
	 *            silently
	 */
	public void addLevelDisplay(final int level) {
		if (_levels.get(level) != null) {
			final LevelDisplay newLevel = new LevelDisplay(level);
			_levels.put(level, newLevel);
			add(newLevel);

		} else {
			System.err.println("level already exists");
		}

	}

	/**
	 * Add an element to a specific layer. If the layer does not exist yet it
	 * will be created on the fly.
	 * 
	 * @param element
	 *            the element to add to the layer
	 * @param level
	 *            the level-number
	 */
	public void addElement(final Object element, final int level) {
		if (_levels.get(level) == null) {
			addLevelDisplay(level);
		}
		// _levels.get(level)
	}
}
