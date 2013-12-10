package parser.objects;

import java.util.List;

public class PathList {
	public final List<Path> _paths;
	public final Path _cheapestStartup, _cheapestTotal;

	public PathList(final List<Path> paths, final Path cheapestStartup,
			final Path cheapestTotal) {
		_paths = paths;
		_cheapestStartup = cheapestStartup;
		_cheapestTotal = cheapestTotal;
	}
}
