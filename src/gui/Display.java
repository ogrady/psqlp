package gui;

import gui.popup.Popup;
import gui.tree.VisualTreeNode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
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
@Deprecated
abstract public class Display extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int MARGIN = 50;
	private static final int WIDTH = 100;
	private static final int HEIGHT = 30;
	private final List<VisualTreeNode> _visualNodes;
	private Tree<?> _tree;
	private final JFrame _parent;

	/**
	 * Constructor
	 * 
	 * @param size
	 *            initial size for the Display
	 */
	public Display(final JFrame parent, final Dimension size) {
		setSize(size);
		_parent = parent;
		_visualNodes = new ArrayList<VisualTreeNode>();
		setBackground(Color.WHITE);
		// setLayout(null);
	}

	/**
	 * Rebuilds the tree to fit to the parents size with the current tree
	 * (costly)
	 */
	public void reTree() {
		setTree(_tree);
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
		_tree = tree;
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
		TreeNode<?> leftChild, rightChild;
		leftChild = root.getLeftChild();
		rightChild = root.getRightChild();

		int rightWidth = 0, leftWidth = 0;
		if (leftChild != null) {
			leftWidth = leftChild.getWidth();
		} else {
			leftWidth = 1;
		}
		if (rightChild != null) {
			rightWidth = rightChild.getWidth();
		}

		final int middle = (right + left) / (rightWidth + leftWidth + 1)
				* leftWidth;
		if (leftChild != null) {
			createSubTree(leftChild, left, middle);
		}
		if (rightChild != null) {
			createSubTree(rightChild, middle, right);
		}
		final VisualTreeNode node = new VisualTreeNode(root);
		node.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent me) {
				new Popup(Display.this._parent).setVisible(true);
			}
		});
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
		for (final VisualTreeNode node : _visualNodes) {
			final TreeNode<?> parent = node.getNode().getParent();
			if (parent != null) {
				final VisualTreeNode parentRepresentation = parent
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
