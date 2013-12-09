package parser.objects;

public class Cost {
	public final float startup, total;

	public Cost(final float startup, final float total) {
		this.startup = startup;
		this.total = total;
	}

	@Override
	public String toString() {
		return String.format("Cost[%f..%f]", startup, total);
	}
}
