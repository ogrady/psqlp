package parser;

import io.logger.LogMessageType;
import io.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import parser.objects.AccessStrategy;
import parser.objects.Cost;
import parser.objects.Join;
import parser.objects.Path;
import parser.objects.RelOptInfo;
import exception.ParseException;

public class RelOptInfoParser extends Parser<RelOptInfo> {
	private List<String> _buffer;
	private final Logger _logger;

	public RelOptInfoParser(final Logger logger) {
		_logger = logger;
	}

	@Override
	public RelOptInfo parse(final List<String> input) throws ParseException {
		_buffer = input;
		final StringBuilder firstLine = new StringBuilder(_buffer.remove(0));
		return parseRelOptInfo(firstLine, getIndent(firstLine));
	}

	private void logIn(final String thing, final StringBuilder from) {
		_logger.print(
				String.format("parsing %s from '%s'", thing, from.toString()),
				LogMessageType.PARSER_IN);
	}

	private void logOut(final Object thing) {
		_logger.print(String.format("successfully parsed '%s'", thing),
				LogMessageType.PARSER_OUT);
	}

	private void logFail(final String thing) {
		_logger.print(String.format("could not parse any %s", thing),
				LogMessageType.PARSER_FAIL);
	}

	public RelOptInfo parseRelOptInfo(final StringBuilder input,
			final int indent) throws ParseException {
		logIn("RelOptInfo", input);
		removeIndent(input, indent);
		truncate(input, "RELOPTINFO (");
		final List<Integer> ids = parseRelIds(input, indent);
		truncate(input, "): ");
		final int rows = parseRows(input, indent);
		truncate(input, " ");
		final int width = parseWidth(input, indent);
		linebreak(input);
		String baserestrictinfo = null, joininfo = null;
		removeIndent(input, indent);
		// trailing whitespace from grammar dismissed due to trimming
		if (lookahead(input, "\tbaserestrictinfo:")) {
			baserestrictinfo = parseBaseRestrictinfo(input, indent);
			removeIndent(input, indent);
		}
		// trailing whitespace from grammar dismissed due to trimming
		if (lookahead(input, "\tjoininfo:")) {
			joininfo = parseJoinInfo(input, indent);
			removeIndent(input, indent);
		}
		final List<Path> pathlist = parsePathList(input, indent + 1);
		Path cheapestStartup = null, cheapestTotal = null;
		if (lookahead(input, "\tcheapest startup path:")) {
			logIn("cheapest startup path", input);
			cheapestStartup = parseCheapestStartupPath(input, indent + 1);
		} else {
			logFail("cheapest startup path");
		}
		if (lookahead(input, "\tcheapest total path:")) {
			logIn("cheapest total path", input);
			cheapestTotal = parseCheapestTotalPath(input, indent + 1);
		} else {
			logFail("cheapest total path");
		}
		linebreak(input);
		final RelOptInfo reloptinfo = new RelOptInfo(ids, rows, width,
				baserestrictinfo, joininfo, pathlist, cheapestStartup,
				cheapestTotal);
		logOut(reloptinfo);
		return reloptinfo;
	}

	public List<Integer> parseRelIds(final StringBuilder input, final int indent)
			throws ParseException {
		logIn("IDs", input);
		final List<Integer> ids = new ArrayList<Integer>();
		while (!lookahead(input, "\\)")) {
			ids.add(parseInt(input));
			trimFront(input);
		}
		logOut(ids);
		return ids;
	}

	public int parseRows(final StringBuilder input, final int indent)
			throws ParseException {
		logIn("rows", input);
		truncate(input, "rows=");
		final int rows = parseInt(input);
		logOut(rows);
		return rows;
	}

	public int parseWidth(final StringBuilder input, final int indent)
			throws ParseException {
		logIn("width", input);
		truncate(input, "width=");
		final int width = parseInt(input);
		logOut(width);
		return width;
	}

	public Cost parseCost(final StringBuilder input, final int indent)
			throws ParseException {
		logIn("costs", input);
		truncate(input, "cost=");
		final float startup = parseFloat(input);
		truncate(input, "..");
		final float total = parseFloat(input);
		final Cost cost = new Cost(startup, total);
		logOut(cost);
		return cost;
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
		logIn("joininfo", input);
		truncate(input, "\tjoininfo:");
		final String info = consume(input);
		linebreak(input);
		logOut(info);
		return info;
	}

	public String parseClauses(final StringBuilder input, final int indent)
			throws ParseException {
		logIn("clauses", input);
		truncate(input, " clauses:");
		final String info = consume(input);
		linebreak(input);
		logOut(info);
		return info;
	}

	public String parseBaseRestrictinfo(final StringBuilder input,
			final int indent) throws ParseException {
		logIn("baserestrictinfo", input);
		truncate(input, "\tbaserestrictinfo:");
		final String info = consume(input);
		linebreak(input);
		logOut(info);
		return info;
	}

	public Path parseCheapestStartupPath(final StringBuilder input,
			final int indent) throws ParseException {
		logIn("cheapest startup path", input);
		linebreak(input);
		truncate(input, "\tcheapest startup path:");
		linebreak(input);
		return parsePath(input, indent);
	}

	public Path parseCheapestTotalPath(final StringBuilder input,
			final int indent) throws ParseException {
		logIn("cheapest total path", input);
		linebreak(input);
		truncate(input, "\tcheapest total path:");
		linebreak(input);
		return parsePath(input, indent);
	}

	public List<Path> parsePathList(final StringBuilder input, final int indent)
			throws ParseException {
		logIn("pathlist", input);
		final ArrayList<Path> paths = new ArrayList<Path>();
		truncate(input, "\tpath list:");
		linebreak(input);
		try {
			while (true) {
				paths.add(parsePath(input, indent));
				logOut(paths.get(paths.size() - 1));
			}
		} catch (final ParseException e) {
			logOut(paths);
			// this feels dirty...
		}
		return paths;
	}

	public AccessStrategy parseAccessStrategy(final StringBuilder input,
			final int indent) throws ParseException {
		logIn("access strategy", input);
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
		logOut(AccessStrategy.values()[i]);
		return AccessStrategy.values()[i];
	}

	public Path parsePath(final StringBuilder input, final int indent)
			throws ParseException {
		logIn("path", input);
		removeIndent(input, indent);
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
		// note: normally there would be two leading whitespaces instead of two
		// tabs. But since we replaced all leading whitespaces with tabs to
		// unify the indent we have to improvise here
		// trailing whitespace from grammar dismissed due to trimming
		final String in = generateIndentAsTabs(indent);
		if (lookahead(input, in + "\t\tpathkeys:")) {
			truncate(input, in);
			logIn("pathkeys", input);
			truncate(input, "\t\tpathkeys:");
			pathkeys = consume(input);
			logOut(pathkeys);
			linebreak(input);
		} else {
			logFail("pathkeys");
		}
		Join join = null;
		// trailing whitespace from grammar dismissed due to trimming
		if (lookahead(input, in + "\t\tclauses:")) {
			truncate(input, in);
			join = parseJoin(input, indent);
		} else {
			logFail("join");
		}
		Path subpath = null;
		try {
			logIn("subpath", input);
			subpath = parsePath(input, indent + 1);
		} catch (final ParseException pe) {
			logFail("path");
		}
		return new Path(ids, strat, rows, cost, pathkeys, join, subpath);
	}

	private void resetIndent(final StringBuilder input, final int indent) {
		String restored = input.toString();
		for (int i = 0; i < indent; i++) {
			restored = "\t" + restored;
		}
		input.delete(0, input.length());
		input.append(restored);
	}

	public Join parseJoin(final StringBuilder input, final int indent)
			throws ParseException {
		logIn("join", input);
		truncate(input, "\t\tclauses:");
		final String clauses = consume(input);
		linebreak(input);
		removeIndent(input, indent);
		int sortouter = -1, sortinner = -1, materializeinner = -1;
		if (lookahead(input, "\t\tsortouter=")) {
			final int[] mergepath = parseMergePath(input, indent);
			sortouter = mergepath[0];
			sortinner = mergepath[1];
			materializeinner = mergepath[2];
		} else {
			resetIndent(input, indent);
		}
		logIn("outher join path", input);
		final Path outerJoinPath = parsePath(input, indent + 1);
		logIn("inner join path", input);
		final Path innerJoinPath = parsePath(input, indent + 1);
		return new Join(sortouter, sortinner, materializeinner, clauses,
				outerJoinPath, innerJoinPath);
	}

	public int[] parseMergePath(final StringBuilder input, final int indent)
			throws ParseException {
		logIn("mergepath", input);
		truncate(input, "\t\tsortouter=");
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

	public String generateIndentAsTabs(final int indent) {
		String prefix = "";
		for (int i = 0; i < indent; i++) {
			prefix += "\t";
		}
		return prefix;
	}

	public static void pause() {
		try {
			System.in.read();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
