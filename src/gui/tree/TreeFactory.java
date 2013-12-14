package gui.tree;

import parser.objects.Path;
import structure.Tree;

public class TreeFactory {
	public static Tree<Path> treeify(final Path path) {
		return new Tree<Path>(nodify(path));
	}

	private static PathTreeNode nodify(final Path path) {
		final PathTreeNode node = new PathTreeNode(path);
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
