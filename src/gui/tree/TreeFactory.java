package gui.tree;

import parser.objects.Path;
import structure.Tree;
import structure.TreeNode;

public class TreeFactory {
	public static Tree<Path> treeify(final Path path) {
		return new Tree<Path>(nodify(path));
	}

	private static TreeNode<Path> nodify(final Path path) {
		final TreeNode<Path> node = new TreeNode<Path>(path);
		if (path != null && path._join != null) {
			if (path._join._outerJoinPath != null) {
				node.setLeftChild(nodify(path._join._outerJoinPath));
			}
			if (path._join._innerJoinPath != null) {
				node.setRightChild(nodify(path._join._innerJoinPath));
			}
		}
		return node;
	}
}
