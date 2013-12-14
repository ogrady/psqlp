package parser.objects;

import java.util.List;

/**
 * <p>
 * {@link RelOptInfo}s hold the information about the access for n relations.<br>
 * It accumulates the possible access-methods and some meta-information.<br>
 * While accessing single relations are accessed via scans from the HDD,
 * multiple relations are accessed by joining together the best access of the
 * relations as found in previous runs.
 * </p>
 * <p>
 * Fictive example:<br>
 * Relation A can be accessed via SeqScan (cost=4) and IdxScan (cost=1) while<br>
 * Relation B can solely accessed via SeqScan (cost=34)<br>
 * Joining A and B will now only consider the cheapest plans for those:<br>
 * IdxScan[A] and SeqScan[B]) while looking for the best way to join those two
 * relations (which can yield multiple possibilities, like<br>
 * <ul>
 * <li>HashJoin[IdxScan[A],SeqScan[B]].</li>
 * <li>NestedLoopJoin[IdxScan[A],SeqScan[B]].</li>
 * <li>NestedLoopJoin[SeqScan[B],IdxScan[A]].</li>
 * <li>...</li>
 * </ul>
 * </p>
 * 
 * @author Daniel
 * 
 */
public class RelOptInfo {
	final public List<Integer> _ids;
	final public int _rows, _width;
	final public String _baseRestrictInfo, _joinInfo;
	final public List<Path> _pathlist;
	final public Path _cheapestStartup, _cheapestTotal;

	/**
	 * Constructor
	 * 
	 * @param ids
	 *            list of ids accessed
	 * @param rows
	 *            number of rows affected (sum of rows of affected relations)
	 * @param width
	 *            width of the relations
	 * @param baseRestrictInfo
	 *            restrictinfo for the access (WHERE)
	 * @param joinInfo
	 *            clauses for the join
	 * @param pathlist
	 *            pathes considered for this relopt
	 * @param cheapestStartup
	 *            cheapest startup path out of the considered paths
	 * @param cheapestTotal
	 *            cheapest total path out of the considered paths
	 */
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
