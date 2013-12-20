package gui.tree;

import java.awt.GridLayout;

import javax.swing.JScrollPane;

import structure.Tree;

public class LinedUpMultiTreeDisplay<T> extends TabbedMultiTreeDisplay<T> {
	private static final long serialVersionUID = 1L;

	public LinedUpMultiTreeDisplay() {
		setLayout(new GridLayout());
	}

	@Override
	public void addTree(final Tree<T> tree, final String title) {
		final TreePanel<T> display = new TreePanel<T>(getSize());
		display.setTree(tree);
		final JScrollPane scrollpane = new JScrollPane(display);
		add(scrollpane);
		getRootPane().addComponentListener(display);
	}
}
