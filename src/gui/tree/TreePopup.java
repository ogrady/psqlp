package gui.tree;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;

import structure.Tree;
import structure.TreeNode;

public class TreePopup<T> extends JDialog {
	private static final long serialVersionUID = 1L;
	public final MultiTreeDisplay<T> _trees;

	public TreePopup(final Frame owner) {
		super(owner, "title", true);
		setLayout(new BorderLayout());
		_trees = new MultiTreeDisplay<T>();
		add(_trees, BorderLayout.CENTER);
		pack();
	}

	public static void main(final String[] args) {
		final TreePopup<Integer> popup = new TreePopup<Integer>(null);
		popup.setVisible(true);
		Tree<Integer> tree = new Tree<Integer>(new TreeNode<Integer>(5));
		tree._root.setLeftChild(new TreeNode<Integer>(34));
		popup._trees.addTree(tree);
		tree = new Tree<Integer>(new TreeNode<Integer>(500));
		tree._root.setLeftChild(new TreeNode<Integer>(1));
		tree._root.setRightChild(new TreeNode<Integer>(1000));
		tree._root.getLeftChild().setRightChild(new TreeNode<Integer>(600));
		popup._trees.addTree(tree);
		popup.pack();
	}

}
