package gui.tree;

import parser.objects.Path;
import structure.TreeNode;

public class PathTreeNode extends TreeNode<Path> {

	public PathTreeNode(final Path element) {
		super(element);
	}

	@Override
	public String toString() {
		return _element._strategy + _element._ids.toString();
	}

}
