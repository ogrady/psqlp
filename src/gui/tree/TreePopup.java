package gui.tree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JDialog;

/**
 * Popup that holds a set of trees in a certain fasion.
 * 
 * @author Daniel
 * 
 * @param <T>
 */
public class TreePopup<T> extends JDialog {
	private static final long serialVersionUID = 1L;
	public final TabbedMultiTreeDisplay<T> _trees;

	/**
	 * Contructor
	 * 
	 * @param owner
	 *            parent window
	 * @param title
	 *            title of the popup
	 * @param size
	 *            initial size
	 * @param tabbed
	 *            whether the trees should be displayed in a tabbed fashion.
	 *            Using false makes the trees appear in line (default behavior
	 *            when omiting this parameter)
	 */
	public TreePopup(final Window owner, final String title,
			final Dimension size, final boolean tabbed) {
		super(owner, title);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(size);
		setLayout(new BorderLayout());
		_trees = tabbed ? new TabbedMultiTreeDisplay<T>()
				: new LinedUpMultiTreeDisplay<T>();
		add(_trees, BorderLayout.CENTER);
	}

	public TreePopup(final Window owner, final String title,
			final Dimension size) {
		this(owner, title, size, false);
	}
}
