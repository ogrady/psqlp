package parser;

import java.util.ArrayList;
import java.util.List;

public class Access {
	public enum AccessStrategy {
		SEQSCAN, IDXSCAN
	}

	private final int id;
	private final List<String> pathkeys;
	private final int rowCount;
	private final float minCost, maxCost;

	public Access(final int id, final int rowCount, final float minCost,
			final float maxCost, final String... pathkeys) {
		this.id = id;
		this.rowCount = rowCount;
		this.minCost = minCost;
		this.maxCost = maxCost;
		this.pathkeys = new ArrayList<String>();
		for (final String p : pathkeys) {
			this.pathkeys.add(p);
		}
	}

}
