package gui.tree;

import gui.popup.PopupFactory;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import parser.objects.Path;
import structure.Backend;
import structure.Tree;
import structure.TreeNode;

public class TreeDisplay<T> extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int MARGIN = 50;
	private static final int WIDTH = 100;
	private static final int HEIGHT = 25;
	private final List<VisualTreeNode> _visualNodes;
	private Tree<T> _tree;

	/**
	 * Constructor
	 * 
	 * @param size
	 *            initial size for the Display
	 */
	public TreeDisplay(final Dimension size) {
		setSize(size);
		_visualNodes = new ArrayList<VisualTreeNode>();
		setBackground(Color.WHITE);
		setLayout(null);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent me) {
				zoom(me.getButton() == MouseEvent.BUTTON1 ? 2 : 0.5f);
			}
		});
	}

	/**
	 * Zooms in on the display which solves the problem of crowded areas of the
	 * tree where nodes may overlap. Zooming in causes the Display to show a
	 * scrollbar. It then repaints the whole tree to fit the size again.
	 * 
	 * @param factor
	 *            the factor by which to zoom (2 for doubled size, 0.5f for
	 *            zooming out)
	 */
	public void zoom(final float factor) {
		setPreferredSize(new Dimension((int) (getSize().width * factor),
				(int) (getSize().height * factor)));
		reTree();
		repaint();
		revalidate();
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
	public void setTree(final Tree<T> tree) {
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
		// TODO popup
		node.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent me) {
				final Path path = (Path) node.getNode()._element;
				final int level = path._ids.size();
				final String key = path._ids.toString();
				final TreePopup<Path> popup = PopupFactory.create(
						(Window) TreeDisplay.this.getTopLevelAncestor(),
						Backend._reloptinfos.get(level).get(key));
				popup.setModalityType(ModalityType.APPLICATION_MODAL);
				popup.setVisible(true);
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
