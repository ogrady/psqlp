package gui.popup;

import java.awt.Graphics;

import structure.Tree;
import structure.TreeNode;

public class TreeRenderer extends Renderer<Tree<?>> {
	private final int marginPerLevel = 50;

	public TreeRenderer(final Tree<?> renderable) {
		super(renderable);
	}

	@Override
	public void render(final Graphics g, final int x, final int y) {
		renderSubTree(0, g.getClip().getBounds().width, _renderable._root, g);
	}

	private void renderSubTree(final int left, final int right,
			final TreeNode<?> root, final Graphics g) {
		final int middle = (right + left) / 2;
		TreeNode<?> leftChild, rightChild;
		leftChild = root.getLeftChild();
		rightChild = root.getRightChild();
		// g.drawString(root.toString(), middle, (root.getHeight() + 1) *
		// marginPerLevel)

		if (leftChild != null) {
			g.drawLine(middle, (root.getHeight() + 1) * marginPerLevel,
					(middle + left) / 2, (root.getHeight() + 2)
							* marginPerLevel);
			renderSubTree(left, middle / 2, leftChild, g);

		}
		if (leftChild != null) {
			renderSubTree(middle / 2, right, rightChild, g);
		}
		new NodeRenderer(root).render(g, middle, (root.getHeight() + 1)
				* marginPerLevel);
		;

	}
}
