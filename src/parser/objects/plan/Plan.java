package parser.objects.plan;



public class Plan {

	public enum JoinStrategy {
		HASHJOIN, NESTLOOP
	}

	private AccessStrategy access;
	private JoinStrategy join;
	private int id;
	private float minCost, maxCost;
	private int rows;

	public Plan(final AccessStrategy access, final JoinStrategy join) {

	}
}
