package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import structure.Tree;
import structure.TreeNode;

/**
 * Displays render trees of any kind. They make a (somewhat inefficient)
 * division of the size of the Display so that the root of the tree is always at
 * the top of the tree in the middle of the Display. Subtrees will have equalliy
 * devided space to the left and right to the root. Even if they are not equally
 * width. This waste-issue is fixed by zooming in on certain areas of the tree
 * from the Visualization.
 * 
 * @author Daniel
 * 
 */
public class Display extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int MARGIN = 50;
	private static final int WIDTH = 60;
	private static final int HEIGHT = 30;
	private final List<VisualNode> _visualNodes;

	/**
	 * Constructor
	 * 
	 * @param size
	 *            initial size for the Display
	 */
	public Display(final Dimension size) {
		setSize(size);
		_visualNodes = new ArrayList<VisualNode>();
		setBackground(Color.WHITE);
		setLayout(null);
	}

	/**
	 * Set a new tree for the display which causes it to recreate the whole
	 * representation of the tree. Kinda expensive function.
	 * 
	 * @param tree
	 *            the tree to represent from no on
	 */
	public void setTree(final Tree<?> tree) {
		removeAll();
		_visualNodes.clear();
		createSubTree(tree._root, 0, getWidth());
	}

	/**
	 * Creates the subtree by deviding the remaining space into halves and then
	 * creating two subtrees (left and right) with an equal portion of that
	 * space.<br>
	 * The root itself is inserted right at the middle of the space.<br>
	 * 
	 * @param root
	 *            root (placed in the middle of the remaining space)
	 * @param left
	 *            leftmost absolute position of the remaining space
	 * @param right
	 *            rightmost absolute position of the remaining space
	 */
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
		final VisualNode node = new VisualNode(root);
		_visualNodes.add(node);
		node.setBounds(middle, (root.getHeight() + 1) * MARGIN, WIDTH, HEIGHT);
		add(node);

	}

	/**
	 * Paints all nodes with a line between the node and its parent
	 */
	@Override
	public void paint(final Graphics g) {
		super.paint(g);
		for (final VisualNode node : _visualNodes) {
			final TreeNode<?> parent = node.getNode().getParent();
			if (parent != null) {
				final VisualNode parentRepresentation = parent
						.getRepresentation();
				if (parentRepresentation != null) {
					g.drawLine(
							node.getX() + node.getWidth() / 2,
							node.getY(),
							parentRepresentation.getX()
									+ parentRepresentation.getWidth() / 2,
							parentRepresentation.getY()
									+ parentRepresentation.getHeight());
				}
			}
		}
	}

}
