package structure;

/**
 * Tree for generic data (unbalanced) for nodes that hold up to two child-nodes
 * 
 * @author Daniel
 * 
 * @param <T>
 *            datatype this tree contains
 */
public class Tree<T> {
	public final TreeNode<T> _root;

	/**
	 * Constructor
	 * 
	 * @param root
	 *            root node of the tree
	 */
	public Tree(final TreeNode<T> root) {
		_root = root;
	}

	/**
	 * Gets the depth of the tree which is the depth of the root-node
	 * 
	 * @return depth of the tree
	 */
	public int getDepth() {
		return _root.getDepth();
	}

	/**
	 * Gets the width of the tree that which is twice the depth (as every level
	 * can double the width of the tree)
	 * 
	 * @return the width of the tree
	 */
	public int getWidth() {
		// return getDepth() * 2;
		return _root.getWidth();
	}
}
