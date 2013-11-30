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

	@Override
	public String toString() {
		return String
				.format("Path[\r\n id: %d \r\n astrat: %s \r\n rows: %d \r\n cost: %s\r\n]",
						id, strategy, rows, cost);
	}
}
