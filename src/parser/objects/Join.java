package parser.objects;

public class Join {
	public final int _sortouter, _sortinner, _materializeinner;
	public final String _restrictClauses;
	public final Path _outerJoinPath, _innerJoinPath, _subPath;

	public Join(final int sortouter, final int sortinner,
			final int materializeinner, final String restrictClauses,
			final Path outerJoinPath, final Path innerJoinPath,
			final Path subpath) {
		_sortinner = sortinner;
		_sortouter = sortouter;
		_materializeinner = materializeinner;
		_restrictClauses = restrictClauses;
		_outerJoinPath = outerJoinPath;
		_innerJoinPath = innerJoinPath;
		_subPath = subpath;
	}
}
