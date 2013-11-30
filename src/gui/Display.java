package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import structure.Backend;
import structure.Tree;
import structure.TreeNode;

public class Display extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int MARGIN = 50;
	private final Backend _backend;

	public Display(final Dimension size, final Backend backend) {
		setSize(size);
		_backend = backend;
		setBackground(Color.WHITE);
		setLayout(null);
	}

	public void setTree(final Tree<?> tree) {
		removeAll();
		createSubTree(tree._root, 0, getWidth());
	}

	private void createSubTree(final TreeNode<?> root, final int left,
			final int right) {
		final int middle = (right + left) / 2;
		TreeNode<?> leftChild, rightChild;
		leftChild = root.getLeftChild();
		rightChild = root.getRightChild();
		if (leftChild != null) {
			createSubTree(leftChild, left, middle / 2);

		}
		if (leftChild != null) {
			createSubTree(rightChild, middle / 2, right);
		}
		final JButton node = new JButton(root.toString());

		node.setBounds(middle, (root.getHeight() + 1) * MARGIN, 50, 20);
		add(node);

	}

}
