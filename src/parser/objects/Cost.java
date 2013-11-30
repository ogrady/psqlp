package parser.objects;

public class Cost {
	public final float from, to;

	public Cost(final float from, final float to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public String toString() {
		return String.format("Cost[%f..%f]", from, to);
	}
}
