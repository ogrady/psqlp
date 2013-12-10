package parser.objects;

import java.util.List;

public class RelOptInfo {
	final public List<Integer> _ids;
	final public int _rows, _width;
	final public String _baseRestrictInfo, _joinInfo;
	final public List<Path> _pathlist;
	final public Path _cheapestStartup, _cheapestTotal;

	public RelOptInfo(final List<Integer> ids, final int rows, final int width,
			final String baseRestrictInfo, final String joinInfo,
			final List<Path> pathlist, final Path cheapestStartup,
			final Path cheapestTotal) {
		_ids = ids;
		_rows = rows;
		_width = width;
		_baseRestrictInfo = baseRestrictInfo;
		_joinInfo = joinInfo;
		_pathlist = pathlist;
		_cheapestStartup = cheapestStartup;
		_cheapestTotal = cheapestTotal;
	}

	@Override
	public String toString() {
		return String
				.format("ids: %s\r\n rows: %d\r\n width: %d\r\n baserestrictinfo: %s\r\n joininfo: %s\r\n pathlist: %s\r\n cheapest startup: %s\r\n cheapest total: %s\r\n",
						_ids, _rows, _width, _baseRestrictInfo, _joinInfo,
						_pathlist, _cheapestStartup, _cheapestTotal);
	}
}
