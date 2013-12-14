package gui.tree;

import javax.swing.JButton;

import structure.TreeNode;

/**
 * VisualNodes represent a treenode within a graph.<br>
 * They take the nodes toString method as label.
 * 
 * @author Daniel
 * 
 */
public class VisualNode extends JButton {
	private static final long serialVersionUID = 1L;
	private final TreeNode<?> _node;

	/**
	 * @return the node that is being represented by this visualnode
	 */
	public TreeNode<?> getNode() {
		return _node;
	}

	/**
	 * Constructor
	 * 
	 * @param node
	 *            the node to represent
	 */
	public VisualNode(final TreeNode<?> node) {
		super(node.toString());
		setToolTipText(node.toString());
		_node = node;
		node.setRepresentation(this);
	}
}
