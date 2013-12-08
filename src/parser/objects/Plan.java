package parser.objects;

import java.util.List;

import parser.objects.plan.AccessStrategy;

public class Plan {
	public final List<Integer> ids;
	public final AccessStrategy strategy;
	public final int rows;
	public final Cost cost;
	public final String pathkeys;
	public final String clauses;

	public Plan(final List<Integer> ids, final AccessStrategy strategy,
			final int rows, final Cost cost, final String pathkeys,
			final String clauses) {
		this.ids = ids;
		this.strategy = strategy;
		this.rows = rows;
		this.cost = cost;
		this.pathkeys = pathkeys;
		this.clauses = clauses;
	}

	@Override
	public String toString() {
		return String
				.format("Path[\r\n id: %s \r\n astrat: %s \r\n rows: %d \r\n cost: %s\r\n pkeys: %s \r\n clauses: %s \r\n]",
						ids, strategy, rows, cost, pathkeys, clauses);
	}
}
