package gui;

import java.awt.Graphics;

import javax.swing.JButton;

import structure.TreeNode;

public class VisualNode extends JButton {
	private static final long serialVersionUID = 1L;
	private final TreeNode<?> _node;

	public TreeNode<?> getNode() {
		return _node;
	}

	public VisualNode(final TreeNode<?> node) {
		super(node.toString());
		_node = node;
		node.setRepresentation(this);
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);
	}
}
