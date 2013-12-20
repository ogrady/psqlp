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
		_tooltip += createToolTipElement("startup cost", ""
				+ _element._cost._startup);
		_tooltip += createToolTipElement("total cost", ""
				+ _element._cost._total);
		if (_element._pathkeys != null && !_element._pathkeys.trim().equals("")) {
			_tooltip += createToolTipElement("path keys", _element._pathkeys);
		}
		if (_element._join != null && _element._join._restrictClauses != null
				&& !_element._join._restrictClauses.trim().equals("")) {
			_tooltip += createToolTipElement("join restrictions",
					_element._join._restrictClauses);
		}
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
