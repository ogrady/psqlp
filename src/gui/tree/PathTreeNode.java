package gui.tree;

import parser.objects.Path;
import structure.TreeNode;

public class PathTreeNode extends TreeNode<Path> {
	private String _tooltip;

	public PathTreeNode(final Path element) {
		super(element);
		_tooltip = "<html>";
		_tooltip += createToolTipElement("ids", _element._ids.toString());
		_tooltip += createToolTipElement("strategy",
				_element._strategy.toString());
		_tooltip += createToolTipElement("cost", _element._cost.toString());
		_tooltip += "<html>";
	}

	@Override
	public String toString() {
		return _element._strategy + _element._ids.toString();
	}

	@Override
	public String getLabelText() {
		return _element._strategy + _element._ids.toString();
	}

	@Override
	public String getToolTipText() {
		return _tooltip;
	}

	private static String createToolTipElement(final String label,
			final String value) {
		return String.format("<strong>%s: </strong>%s<br>", label, value);
	}

}
