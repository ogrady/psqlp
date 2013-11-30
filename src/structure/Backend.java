package structure;

import java.util.Random;

public class Backend {
	public Tree<?> getTree() {
		new Random();
		TreeNode<Integer> node = null;
		TreeNode<Integer> left = null;
		TreeNode<Integer> right = null;
		for (int i = 0; i < 5; i++) {
			node = new TreeNode<Integer>(i);
			right = new TreeNode<Integer>(-i);
			if (left != null) {
				left.setParent(node);
			}
			right.setParent(node);
			node.setLeftChild(left);
			node.setRightChild(right);
			left = node;
		}
		return new Tree<Integer>(node);
	}
}
