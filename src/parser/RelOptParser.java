package parser;

import io.logger.LogMessageType;
import io.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import parser.objects.Cost;
import parser.objects.Plan;
import parser.objects.plan.AccessStrategy;
import exception.ParseException;

public class RelOptParser extends Parser<Object> {
	private static final Logger logger = new Logger();
	private List<String> _buffer;

	public RelOptParser() {
		logger.accept(
		// LogMessageType.PARSER
		);
	}

	@Override
	public Object parse(final List<String> input) throws ParseException {
		_buffer = input;
		final StringBuilder firstLine = lbr(new StringBuilder());
		relopt(firstLine);
		return null;
	}

	private void relopt(StringBuilder input) throws ParseException {
		logger.print(String.format("parsing reloptinfo from '%s'", input),
				LogMessageType.PARSER);
		truncate(input, "RELOPTINFO (");
		System.out.println(ids(input));
		// getInt(input);
		truncate(input, "): ");
		rows(input);
		width(input);
		input = lbr(input);
		if (lookahead(input, "baserestrictinfo")) {
			consume(input);
			input = lbr(input);
		}
		System.out.println("------------------- new paths:");
		System.out.println(pathlist(input));
	}

	private int rows(final StringBuilder input) throws ParseException {
		logger.print(String.format("parsing rows from '%s'", input),
				LogMessageType.PARSER);
		truncate(input, "rows=");
		return getInt(input);
	}

	private int width(final StringBuilder input) throws ParseException {
		logger.print(String.format("parsing width from '%s'", input),
				LogMessageType.PARSER);
		truncate(input, "width=");
		return getInt(input);
	}

	private StringBuilder lbr(final StringBuilder currentRemainder)
			throws ParseException {
		if (currentRemainder.length() > 0) {
			throw new ParseException(
					String.format(
							"trying to fetch next line when current line was not consumed yet: '%s'",
							currentRemainder.toString()));
		}
		return new StringBuilder(_buffer.remove(0));
	}

	private List<Plan> pathlist(StringBuilder input) throws ParseException {
		logger.print(String.format("parsing pathlist from '%s'", input),
				LogMessageType.PARSER);
		final ArrayList<Plan> paths = new ArrayList<Plan>();
		truncate(input, "path list:");
		while (!lookahead(new StringBuilder(_buffer.get(0)),
				"cheapest startup path:")) {
			input = lbr(input);
			final AccessStrategy strategy = accessstrategy(input);
			truncate(input, "(");
			// final int id = getInt(input);
			final List<Integer> ids = ids(input);
			truncate(input, ")");
			final int rows = rows(input);
			final Cost cost = cost(input);
			// input = lbr(input);
			String pathkeys = "";
			String clauses = "";
			if (lookahead(new StringBuilder(_buffer.get(0)), "pathkeys")) {
				input = lbr(input);
				pathkeys = pathkeys(input);
			}
			if (lookahead(new StringBuilder(_buffer.get(0)), "clauses:")) {
				input = lbr(input);
				clauses = clauses(input);
			}
			// input = lbr(input);
			paths.add(new Plan(ids, strategy, rows, cost, pathkeys, clauses));
		}
		return paths;
	}

	private List<Integer> ids(final StringBuilder input) throws ParseException {
		final ArrayList<Integer> ids = new ArrayList<Integer>();
		while (lookahead(input, "\\d")) {
			ids.add(getInt(input));
			trimFront(input);
		}
		return ids;
	}

	private String pathkeys(final StringBuilder input) throws ParseException {
		truncate(input, "pathkeys:");
		return consume(input);
	}

	private String clauses(final StringBuilder input) throws ParseException {
		truncate(input, "clauses:");
		return consume(input);
	}

	private AccessStrategy accessstrategy(final StringBuilder input)
			throws ParseException {
		logger.print(String.format("parsing accessstrategy from '%s'", input),
				LogMessageType.PARSER);
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

	private Cost cost(final StringBuilder input) throws ParseException {
		logger.print(String.format("parsing cost from '%s'", input),
				LogMessageType.PARSER);
		truncate(input, "cost=");
		final float from = getFloat(input);
		truncate(input, "..");
		final float to = getFloat(input);
		return new Cost(from, to);
	}
}
