package parser;

import io.logger.LogMessageType;
import io.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import parser.objects.Cost;
import parser.objects.Join;
import parser.objects.Path;
import parser.objects.PathList;
import parser.objects.RelOptInfo;
import parser.objects.plan.AccessStrategy;
import exception.ParseException;

public class RelOptInfoParser extends Parser<RelOptInfo> {
	private List<String> _buffer;
	private final Logger _logger;

	public RelOptInfoParser(final Logger logger) {
		/*_logger = new Logger();
		_logger.accept(LogMessageType.PARSER);*/
		_logger = logger;
	}

	@Override
	public RelOptInfo parse(final List<String> input) throws ParseException {
		_buffer = input;
		final StringBuilder firstLine = new StringBuilder(_buffer.remove(0));
		return parseRelOptInfo(firstLine, getIndent(firstLine));
	}

	private void log(final String thing, final StringBuilder from) {
		_logger.print(
				String.format("parsing %s from '%s'", thing, from.toString()),
				LogMessageType.PARSER);
	}

	public RelOptInfo parseRelOptInfo(final StringBuilder input,
			final int indent) throws ParseException {
		log("RelOptInfo", input);
		removeIndent(input, indent);
		truncate(input, "RELOPTINFO (");
		final List<Integer> ids = parseRelIds(input, indent);
		truncate(input, "): ");
		final int rows = parseRows(input, indent);
		truncate(input, " ");
		final int width = parseWidth(input, indent);
		linebreak(input);
		String baserestrictinfo = null;
		final String joininfo = null;
		removeIndent(input, indent);
		if (lookahead(input, "\tbaserestrictinfo: ")) {
			baserestrictinfo = parseBaseRestrictinfo(input, indent);
			// linebreak(input);
			removeIndent(input, indent);
		}
		if (lookahead(input, "\tjoininfo: ")) {
			parseJoinInfo(input, indent);
		}
		final PathList pathlist = parsePathList(input, indent);
		linebreak(input);
		return new RelOptInfo(ids, rows, width, baserestrictinfo, joininfo,
				pathlist._paths, pathlist._cheapestStartup,
				pathlist._cheapestTotal);
	}

	public List<Integer> parseRelIds(final StringBuilder input, final int indent)
			throws ParseException {
		log("IDs", input);
		final List<Integer> ids = new ArrayList<Integer>();
		while (!lookahead(input, "\\)")) {
			ids.add(parseInt(input));
			trimFront(input);
		}
		return ids;
	}

	public int parseRows(final StringBuilder input, final int indent)
			throws ParseException {
		log("rows", input);
		truncate(input, "rows=");
		return parseInt(input);
	}

	public int parseWidth(final StringBuilder input, final int indent)
			throws ParseException {
		log("width", input);
		truncate(input, "width=");
		return parseInt(input);
	}

	public Cost parseCost(final StringBuilder input, final int indent)
			throws ParseException {
		log("costs", input);
		truncate(input, "cost=");
		final float startup = parseFloat(input);
		truncate(input, "..");
		final float total = parseFloat(input);
		return new Cost(startup, total);
	}

	public void removeTab(final StringBuilder input) throws ParseException {
		truncate(input, "\t");
	}

	public void removeTabs(final StringBuilder input) throws ParseException {
		while (lookahead(input, "\t")) {
			removeTab(input);
		}
	}

	public void linebreak(final StringBuilder input) {
		input.delete(0, input.length());
		input.append(_buffer.remove(0));
	}

	public String parseJoinInfo(final StringBuilder input, final int indent)
			throws ParseException {
		log("joininfo", input);
		truncate(input, "\tjoininfo: ");
		final String info = input.toString();
		linebreak(input);
		return info;
	}

	public String parseClauses(final StringBuilder input, final int indent)
			throws ParseException {
		log("clauses", input);
		truncate(input, " clauses: ");
		final String info = input.toString();
		linebreak(input);
		return info;
	}

	public String parseBaseRestrictinfo(final StringBuilder input,
			final int indent) throws ParseException {
		log("baserestrictinfo", input);
		truncate(input, "\tbaserestrictinfo: ");
		final String info = input.toString();
		linebreak(input);
		return info;
	}

	public Path parseCheapestStartupPath(final StringBuilder input,
			final int indent) throws ParseException {
		log("cheapest startup path", input);
		linebreak(input);
		truncate(input, "\tcheapest startup path:");
		linebreak(input);
		return parsePath(input, indent);
	}

	public Path parseCheapestTotalPath(final StringBuilder input,
			final int indent) throws ParseException {
		log("cheapest total path", input);
		linebreak(input);
		truncate(input, "\tcheapest total path:");
		linebreak(input);
		return parsePath(input, indent);
	}

	public PathList parsePathList(final StringBuilder input, final int indent)
			throws ParseException {
		log("pathlist", input);
		final ArrayList<Path> paths = new ArrayList<Path>();
		truncate(input, "\tpath list:");
		linebreak(input);
		try {
			while (true) {
				paths.add(parsePath(input, indent));
			}
		} catch (final ParseException e) {
			e.printStackTrace();
			// this feels dirty...
		}
		Path cheapestStartup = null, cheapestTotal = null;
		if (lookahead(input, "\tcheapest startup path:")) {
			cheapestStartup = parseCheapestStartupPath(input, indent);
		}
		if (lookahead(input, "\tcheapest total path:")) {
			cheapestTotal = parseCheapestTotalPath(input, indent);
		}
		return new PathList(paths, cheapestStartup, cheapestTotal);
	}

	public AccessStrategy parseAccessStrategy(final StringBuilder input,
			final int indent) throws ParseException {
		log("access strategy", input);
		final String internalString = input.toString();
		int i = 0;
		while (i < AccessStrategy.values().length
				&& !internalString
						.startsWith(AccessStrategy.values()[i].name())) {
			i++;
		}
		if (i >= AccessStrategy.values().length) {
			throw new ParseException(String.format(
					"could not determine access-strategy from '%s'",
					internalString));
		}
		truncate(input, AccessStrategy.values()[i].name());
		return AccessStrategy.values()[i];
	}

	public Path parsePath(final StringBuilder input, final int indent)
			throws ParseException {
		log("path", input);
		removeIndent(input, indent);
		truncate(input, "\t");
		final AccessStrategy strat = parseAccessStrategy(input, indent);
		List<Integer> ids = new ArrayList<Integer>();
		final int rows = 0;
		if (lookahead(input, "\\(")) {
			truncate(input, "(");
			ids = parseRelIds(input, indent);
			truncate(input, ") ");
			parseRows(input, indent);
		}
		truncate(input, " ");
		final Cost cost = parseCost(input, indent);
		linebreak(input);
		String pathkeys = null;
		System.out.println(input);
		System.out.println(indent);
		// pause();
		removeIndent(input, indent);
		// note: normally there would be two leading whitespaces instead of two
		// tabs. But since we replaced all leading whitespaces with tabs to
		// unify the indent we have to improvise here
		if (lookahead(input, "\t\tpathkeys:")) {
			truncate(input, "\t\tpathkeys: ");
			pathkeys = consume(input);
			linebreak(input);
		}
		Join join = null;
		// additional tab?
		if (lookahead(input, "\t\tclauses: ")) {
			join = parseJoin(input, indent);
		}
		return new Path(ids, strat, rows, cost, pathkeys, join);
	}

	public Join parseJoin(final StringBuilder input, final int indent)
			throws ParseException {
		log("join", input);
		truncate(input, "\t\tclauses: ");
		final String clauses = input.toString();
		linebreak(input);
		int sortouter = -1, sortinner = -1, materializeinner = -1;
		if (lookahead(input, "\tsortouter=")) {
			final int[] mergepath = parseMergePath(input, indent);
			sortouter = mergepath[0];
			sortinner = mergepath[1];
			materializeinner = mergepath[2];
		}
		final Path outerJoinPath = parsePath(input, indent + 1);
		final Path innerJoinPath = parsePath(input, indent + 1);
		Path subpath = null;
		try {
			subpath = parsePath(input, indent + 1);
		} catch (final ParseException pe) {
		}
		return new Join(sortouter, sortinner, materializeinner, clauses,
				outerJoinPath, innerJoinPath, subpath);
	}

	public int[] parseMergePath(final StringBuilder input, final int indent)
			throws ParseException {
		log("mergepath", input);
		truncate(input, "\tsortouter=");
		final int sortouter = parseInt(input);
		truncate(input, " sortinner=");
		final int sortinner = parseInt(input);
		truncate(input, " materializeinner=");
		final int matinner = parseInt(input);
		linebreak(input);
		return new int[] { sortouter, sortinner, matinner };
	}

	public void removeIndent(final StringBuilder input, final int indent)
			throws ParseException {
		String prefix = "";
		for (int i = 0; i < indent; i++) {
			prefix += "\t";
		}
		truncate(input, prefix);
	}

	public static void pause() {
		try {
			System.in.read();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
