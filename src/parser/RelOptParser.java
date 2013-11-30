package parser;

import io.logger.LogMessageType;
import io.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import parser.objects.AccessStrategy;
import parser.objects.Cost;
import parser.objects.Path;
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
		getInt(input);
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

	private List<Path> pathlist(StringBuilder input) throws ParseException {
		logger.print(String.format("parsing pathlist from '%s'", input),
				LogMessageType.PARSER);
		final ArrayList<Path> paths = new ArrayList<Path>();
		truncate(input, "path list:");
		while (!lookahead(new StringBuilder(_buffer.get(0)),
				"cheapest startup path:")) {
			input = lbr(input);
			final AccessStrategy strategy = accessstrategy(input);
			truncate(input, "(");
			final int id = getInt(input);
			truncate(input, ")");
			final int rows = rows(input);
			final Cost cost = cost(input);
			// input = lbr(input);
			if (lookahead(new StringBuilder(_buffer.get(0)), "pathkeys")) {
				input = lbr(input);
				consume(input);
			}
			// input = lbr(input);
			paths.add(new Path(id, strategy, rows, cost));
		}
		return paths;
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
