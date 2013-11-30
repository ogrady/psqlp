package structure;

public class TreeNode<T> {
	private TreeNode<T> _parent, _leftChild, _rightChild;
	public final T _element;

	public int getHeight() {
		/*final int left = _leftChild == null ? 0 : _leftChild.getHeight() + 1;
		final int right = _rightChild == null ? 0 : _rightChild.getHeight() + 1;
		return Math.max(left, right);*/
		return _parent != null ? _parent.getHeight() + 1 : 0;
	}

	public int getDepth() {
		final int left = _leftChild == null ? 0 : _leftChild.getDepth() + 1;
		final int right = _rightChild == null ? 0 : _rightChild.getDepth() + 1;
		return Math.max(left, right);
	}

	public TreeNode<T> getParent() {
		return _parent;
	}

	public void setParent(final TreeNode<T> parent) {
		this._parent = parent;
	}

	public TreeNode<T> getLeftChild() {
		return _leftChild;
	}

	public void setLeftChild(final TreeNode<T> leftChild) {
		this._leftChild = leftChild;
	}

	public TreeNode<T> getRightChild() {
		return _rightChild;
	}

	public void setRightChild(final TreeNode<T> rightChild) {
		this._rightChild = rightChild;
	}

	public TreeNode(final T element) {
		_element = element;
	}

	@Override
	public String toString() {
		return _element.toString();
	}
}
