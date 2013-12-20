package gui.tree;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import structure.Tree;

/**
 * Display that contains multiple trees. Each tree is displayed within its own
 * tab and scrollable
 * 
 * @author Daniel
 * 
 * @param <T>
 *            type of the contained trees
 */
public class TabbedMultiTreeDisplay<T> extends JComponent {
	private static final long serialVersionUID = 1L;
	private final JTabbedPane _tabs;

	/**
	 * Constructor
	 */
	public TabbedMultiTreeDisplay() {
		setLayout(new BorderLayout());
		_tabs = new JTabbedPane();
		add(_tabs, BorderLayout.CENTER);
	}

	/**
	 * Adds a new tree to the display. Creates another tab with a scrollable and
	 * the tree in it
	 * 
	 * @param tree
	 *            the tree to add
	 */
	public void addTree(final Tree<T> tree, final String title) {
		final TreePanel<T> display = new TreePanel<T>(getSize());
		display.setTree(tree);
		_tabs.addTab(title, new JScrollPane(display));
	}

}
