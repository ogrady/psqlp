package structure;

public class Backend {
	public Tree<?> getTree() {
		TreeNode<Integer> node = null;
		TreeNode<Integer> left = null;
		TreeNode<Integer> right = null;
		for (int i = 0; i < 20; i++) {
			node = new TreeNode<Integer>(i + 1);
			right = new TreeNode<Integer>(-i + 1);
			if (left != null) {
				node.setLeftChild(left);
				left.setParent(node);
			}
			right.setParent(node);

			node.setRightChild(right);
			left = node;
		}
		return new Tree<Integer>(node);
	}
}
