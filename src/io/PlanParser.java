package io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import parser.Parser;
import parser.objects.Cost;
import parser.objects.Join;
import parser.objects.Path;
import parser.objects.plan.AccessStrategy;
import exception.ParseException;

public class PlanParser extends Parser<Object> {
	private List<String> _buffer;

	@Override
	public Object parse(final List<String> input) throws ParseException {
		_buffer = input;
		return null;
	}

	public void parseRelOptInfo(final StringBuilder input, final int indent)
			throws ParseException {
		truncate(input, "RELOPTINFO (");
	}

	public List<Integer> parseRelIds(final StringBuilder input, final int indent)
			throws ParseException {
		final List<Integer> ids = new ArrayList<Integer>();
		while (!lookahead(input, ")")) {
			ids.add(parseInt(input));
			trimFront(input);
		}
		truncate(input, ")");
		return ids;
	}

	public int parseRows(final StringBuilder input, final int indent)
			throws ParseException {
		truncate(input, "rows=");
		return parseInt(input);
	}

	public int parseWidth(final StringBuilder input, final int indent)
			throws ParseException {
		truncate(input, "width=");
		return parseInt(input);
	}

	public Cost parseCost(final StringBuilder input, final int indent)
			throws ParseException {
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
		truncate(input, "\tjoininfo: ");
		final String info = input.toString();
		linebreak(input);
		return info;
	}

	public String parseClauses(final StringBuilder input, final int indent)
			throws ParseException {
		truncate(input, " clauses: ");
		final String info = input.toString();
		linebreak(input);
		return info;
	}

	public String parseBaseRestrictinfo(final StringBuilder input,
			final int indent) throws ParseException {
		truncate(input, "\tbaserestrictinfo: ");
		final String info = input.toString();
		linebreak(input);
		return info;
	}

	public Path parseCheapestStartupPath(final StringBuilder input,
			final int indent) throws ParseException {
		linebreak(input);
		truncate(input, "\tcheapest startup path:");
		linebreak(input);
		return parsePath(input, indent);
	}

	public Path parseCheapestTotalPath(final StringBuilder input,
			final int indent) throws ParseException {
		linebreak(input);
		truncate(input, "\tcheapest total path:");
		linebreak(input);
		return parsePath(input, indent);
	}

	public List<Path> parsePathList(final StringBuilder input, final int indent)
			throws ParseException {
		final ArrayList<Path> paths = new ArrayList<Path>();
		truncate(input, "\tpath list:");
		linebreak(input);
		try {
			while (true) {
				paths.add(parsePath(input, indent));
			}
		} catch (final ParseException e) {
			// this feels dirty...
		}
		if (lookahead(input, "\tcheapest startup path:")) {
			// TODO store
			parseCheapestStartupPath(input, indent);
		}
		if (lookahead(input, "\tcheapest total path:")) {
			parseCheapestTotalPath(input, indent);
		}
		return paths;
	}

	public AccessStrategy parseAccessStrategy(final StringBuilder input,
			final int indent) throws ParseException {
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

	// TODO
	public Path parsePath(final StringBuilder input, final int indent)
			throws ParseException {
		truncate(input, "\t");
		final AccessStrategy strat = parseAccessStrategy(input, indent);
		List<Integer> ids = new ArrayList<Integer>();
		final int rows = 0;
		if (lookahead(input, "(")) {
			ids = parseRelIds(input, indent);
			truncate(input, ")");
			parseRows(input, indent);
		}
		truncate(input, " ");
		final Cost cost = parseCost(input, indent);
		linebreak(input);
		System.out.println(strat);
		System.out.println(ids);
		System.out.println(rows);
		System.out.println(cost);
		/*
		for(int i = 0; i < indent; i++) {
			truncate(input,"\t");
		}
		truncate(input, " pathkeys: ");
		String keys =*/
		// [{TAB}," pathkeys: ",PATHKEYS],[JOIN]
		return null;
	}

	public Join parseJoin(final StringBuilder input, final int indent)
			throws ParseException {
		truncate(input, "\t clauses: ");
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

	public static void main(final String[] args) throws ParseException {
		final PlanParser p = new PlanParser();
		p._buffer = new ArrayList<String>(
				Arrays.asList(new String[] { "a", "b" }));
		final StringBuilder sb = new StringBuilder("v");
		p.linebreak(sb);
		p.linebreak(sb);
		System.out.println(sb);
		// new PlanParser().removeTab(new StringBuilder("	sdf"));
	}

}
