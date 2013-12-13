package parser.objects;

import java.util.List;

import parser.objects.plan.AccessStrategy;

/**
 * A Path specifies the access to a relation. That relation might either be
 * scalar in terms of a physical relation within the DB-system or an artificial
 * relation created from multiple oher relations (eg a join). The plan itself
 * holds information about the access.
 * 
 * @author Daniel
 * 
 */
public class Path {
	public final List<Integer> _ids;
	public final AccessStrategy _strategy;
	public final int _rows;
	public final Cost _cost;
	public final String _pathkeys;
	public final Join _join;
	public final Path _subpath;

	/**
	 * Constructor
	 * 
	 * @param ids
	 *            set of IDs this plan accesses (either one element if it is
	 *            access to one relation or multiple if it is a join)
	 * @param strategy
	 *            the strategy used to access or join
	 * @param rows
	 *            the number of rows within this plan
	 * @param cost
	 *            the cost to generate this plan with the passed strategy
	 * @param pathkeys
	 *            the pathkeys if any
	 * @param clauses
	 *            the clauses if any
	 */
	public Path(final List<Integer> ids, final AccessStrategy strategy,
			final int rows, final Cost cost, final String pathkeys,
			final Join join, final Path subpath) {
		_ids = ids;
		_strategy = strategy;
		_rows = rows;
		_cost = cost;
		_pathkeys = pathkeys;
		_join = join;
		_subpath = subpath;
	}

	/**
	 * Two Plans are equal if they have the exact same IDs (same order too) and
	 * use the same {@link AccessStrategy}
	 * 
	 * @param other
	 *            other plan to check against
	 * @return true, if the two plans have the same IDs and strategy
	 */
	public boolean equals(final Path other) {
		boolean equal = _strategy == other._strategy
				&& _ids.size() == other._ids.size();
		int i = 0;
		while (equal && i < _ids.size()) {
			equal = _ids.get(i) == other._ids.get(i);
			i++;
		}
		return equal;
	}

	@Override
	public String toString() {
		return String
				.format("Path[\r\n id: %s \r\n astrat: %s \r\n rows: %d \r\n cost: %s\r\n pathkeys: %s \r\n clauses: %s \r\n subpath: %s \r\n]",
						_ids, _strategy, _rows, _cost, _pathkeys, _join,
						_subpath);
	}
}
