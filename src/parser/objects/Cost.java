package parser.objects;

/**
 * Holds information about costs for accessing relations where we have two
 * different costs:<br>
 * <strong>startup cost:</strong> the overhead before the first tuple can be
 * generated (startup_cost + N*(per_row_cost) where N is the estimated rows)<br>
 * <strong>total cost:</strong> the costs to generate all tuples from the
 * relation
 * 
 * @author Daniel
 * 
 */
public class Cost {
	public final float _startup, _total;

	/**
	 * Constructor
	 * 
	 * @param startup
	 *            costs before the first tuple can be generated
	 * @param total
	 *            costs to generate all tuples from the relation
	 */
	public Cost(final float startup, final float total) {
		this._startup = startup;
		this._total = total;
	}

	@Override
	public String toString() {
		return String.format("Cost[%f..%f]", _startup, _total);
	}
}
