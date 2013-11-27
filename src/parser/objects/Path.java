package parser.objects;

public class Path {
	public final int id;
	public final AccessStrategy strategy;
	public final int rows;
	public final Cost cost;

	public Path(final int id, final AccessStrategy strategy, final int rows,
			final Cost cost) {
		this.id = id;
		this.strategy = strategy;
		this.rows = rows;
		this.cost = cost;
	}
}
