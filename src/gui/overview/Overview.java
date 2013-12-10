package gui.overview;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;

import parser.objects.RelOptInfo;

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
	private int _maxLevel;

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
		// if (_levels.get(level) == null) {
		if (level > _maxLevel) {
			for (int i = _maxLevel; i <= level; i++) {
				final LevelDisplay newLevel = new LevelDisplay(i);
				_levels.put(level, newLevel);
				add(newLevel);
			}
			_maxLevel = level;
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
	public void addRelOptInfo(final RelOptInfo relopt, final int level) {
		addLevelDisplay(level);
		final LevelDisplay display = _levels.get(level);
		display.add(new JButton(relopt._ids.toString()));
		revalidate();
	}
}
