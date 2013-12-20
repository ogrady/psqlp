package parser.objects;

public class Join {
	public final int _sortouter, _sortinner, _materializeinner;
	public final String _restrictClauses;
	public final Path _outerJoinPath, _innerJoinPath;

	public Join(final int sortouter, final int sortinner,
			final int materializeinner, final String restrictClauses,
			final Path outerJoinPath, final Path innerJoinPath) {
		_sortinner = sortinner;
		_sortouter = sortouter;
		_materializeinner = materializeinner;
		_restrictClauses = restrictClauses;
		_outerJoinPath = outerJoinPath;
		_innerJoinPath = innerJoinPath;
	}

	@Override
	public String toString() {
		return String.format("{inner: %s\r\n outer: %s\r\n restrict: %s\r\n}",
				_innerJoinPath, _outerJoinPath, _restrictClauses);
	}
}
