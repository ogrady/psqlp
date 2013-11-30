package structure;

import gui.IRenderable;
import gui.VisualNode;

/**
 * TreeNodes are one node of a generic Tree. They are representable in terms of
 * being visualizable via swing components.
 * 
 * @author Daniel
 * 
 * @param <T>
 *            datatype this node hold
 */
public class TreeNode<T> implements IRenderable<VisualNode> {
	private TreeNode<T> _parent, _leftChild, _rightChild;
	public final T _element;
	private VisualNode _representation;

	@Override
	public VisualNode getRepresentation() {
		return _representation;
	}

	@Override
	public void setRepresentation(final VisualNode representation) {
		_representation = representation;
	}

	/**
	 * Gets the height of this particular node in the Tree. That's 0 if the node
	 * is the root or the parents height + 1 (recursive)
	 * 
	 * @return the height of the node
	 */
	public int getHeight() {
		return _parent != null ? _parent.getHeight() + 1 : 0;
	}

	/**
	 * Gets the depth of this particular node in the Tree. That depends on which
	 * subtree is deeper. Leaf-level nodes have the depth 0
	 * 
	 * @return the depth of the node
	 */
	public int getDepth() {
		final int left = _leftChild == null ? 0 : _leftChild.getDepth() + 1;
		final int right = _rightChild == null ? 0 : _rightChild.getDepth() + 1;
		return Math.max(left, right);
	}

	/**
	 * @return the parent node of this node
	 */
	public TreeNode<T> getParent() {
		return _parent;
	}

	/**
	 * @param parent
	 *            the new parent. Nodes can't be their own parents but their
	 *            children can be the node's parent (circular tree)
	 */
	public void setParent(final TreeNode<T> parent) {
		if (parent != this) {
			_parent = parent;
		}
	}

	/**
	 * @return the node to the left of the node
	 */
	public TreeNode<T> getLeftChild() {
		return _leftChild;
	}

	/**
	 * @param leftChild
	 *            the new left child node
	 */
	public void setLeftChild(final TreeNode<T> leftChild) {
		_leftChild = leftChild;
	}

	/**
	 * @return the node to the right of the node
	 */
	public TreeNode<T> getRightChild() {
		return _rightChild;
	}

	/**
	 * @param rightChild
	 *            the new right node
	 */
	public void setRightChild(final TreeNode<T> rightChild) {
		this._rightChild = rightChild;
	}

	/**
	 * Constructor
	 * 
	 * @param element
	 *            the element that is encapsulated by the node
	 */
	public TreeNode(final T element) {
		_element = element;
	}

	@Override
	public String toString() {
		return _element.toString();
	}
}
