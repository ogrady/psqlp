package gui.tree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JDialog;

/**
 * Popup that holds a set of trees in tabs.
 * 
 * @author Daniel
 * 
 * @param <T>
 */
public class TreePopup<T> extends JDialog {
	private static final long serialVersionUID = 1L;
	public final MultiTreeDisplay<T> _trees;

	public TreePopup(final Window owner, final String title,
			final Dimension size) {
		super(owner, title);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setSize(size);
		setLayout(new BorderLayout());
		_trees = new MultiTreeDisplay<T>();
		add(_trees, BorderLayout.CENTER);
	}
}
