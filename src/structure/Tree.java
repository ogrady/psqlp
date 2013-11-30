package structure;

public class Tree<T> {
	public final TreeNode<T> _root;

	public Tree(final TreeNode<T> root) {
		_root = root;
	}

	public int getDepth() {
		return _root.getDepth();
	}

	public int getWidth() {
		return getDepth() * 2;
	}
}
