package parser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.ParseException;
import exception.ResultParseException;

/**
 * Parser class which holds the basic functionality for input-strings.<br>
 * This means: deleting portions of the input<br>
 * Cutting off numbers or boolean values from the beginning of the string<br>
 * Trimming the front of the string<br>
 * When failing to execute one of these operations an exception will be thrown
 * as we assume the calling the method at this point was correct and the
 * expected string is simply missing in the input ( = the input does not match
 * our grammar)
 * 
 * @author Daniel
 * 
 * @param <P>
 *            parsers can yield arbitrary objects where P determines what type
 *            of parsable object is yielded from the specific parser
 */
public abstract class Parser<P> implements IParser<List<String>, P> {
	private static final Pattern INTEGER_PATTERN = Pattern.compile("^\\d+");
	private static final Pattern FLOAT_PATTERN = Pattern
			.compile("^\\d+(.\\d+)?");
	private static final Pattern BOOLEAN_PATTERN = Pattern.compile(
			"^(true|false)", Pattern.CASE_INSENSITIVE);
	private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s*");

	/**
	 * Checks whether the beginning of the input meets the expectation. If so
	 * the prefix will be discarded and everything is fine. Else an exception
	 * will be thrown
	 * 
	 * @param input
	 *            the input string
	 * @param expected
	 *            the expected prefix
	 * @throws ParseException
	 *             when the prefix does not meet the expectations
	 */
	protected void checkNext(final String input, final String expected)
			throws ParseException {
		if (!input.startsWith(expected)) {
			throw formException(String.format(
					"invalid token '%s' - expected '%s'", input, expected));
		}
	}

	/**
	 * Does a lookahead on the input. Used when it comes to repeated ({...}) or
	 * optional rules ([...]) where the input should not be removed but rather
	 * checked ahead
	 * 
	 * @param input
	 *            input string
	 * @param expected
	 *            the lookahead to check for
	 * @return true, if the input starts off with the expected prefix
	 */
	protected boolean lookahead(final StringBuilder input, final String expected) {
		return input.toString().startsWith(expected);
	}

	/**
	 * Removes a portion from the input (the prefix) by reference
	 * 
	 * @param input
	 *            input string that will be truncated by a prefix
	 * @throws ParseException
	 *             if the prefix is not in the
	 */
	protected void truncate(final StringBuilder input, final String prefix)
			throws ParseException {
		trimFront(input);
		if (prefix != null) {
			if (!input.toString().startsWith(prefix)) {
				throw formException(String.format(
						"malformed input '%s' - expected '%s'", input, prefix));
			} else {
				input.delete(0, prefix.length());
			}
		}
	}

	/**
	 * Removes the next line from the list of lines, removes a portion (the
	 * prefix) from that line and returns the remainder as integer
	 * 
	 * @param input
	 *            input string
	 * @return remaining string parsed to integer
	 * @throws ParseException
	 */
	protected Integer getInt(final StringBuilder input) throws ParseException {
		Integer nr = null;
		final String token = match(input, INTEGER_PATTERN);
		if (token != null) {
			try {
				nr = Integer.parseInt(token);
			} catch (final NumberFormatException nfe) {
				throw formException(String.format("malformed integer '%s'",
						token));
			}
		}
		return nr;
	}

	protected String match(final StringBuilder input, final Pattern pattern)
			throws ParseException {
		final Matcher matcher = pattern.matcher(input);
		String result = null;
		if (matcher.find()) {
			result = matcher.group();
			input.delete(0, result.length());
		}
		return result;
	}

	/**
	 * Removes a portion (the prefix) from the input and returns the remainder
	 * as boolean
	 * 
	 * @param input
	 *            input string
	 * @param prefix
	 *            prefix to remove
	 * @return remaining string parsed to boolean
	 * @throws ParseException
	 */
	protected Boolean getBool(final StringBuilder input) throws ParseException {
		return Boolean.parseBoolean(match(input, BOOLEAN_PATTERN));
	}

	protected Float getFloat(final StringBuilder input) throws ParseException {
		Float nr = null;
		final String token = match(input, FLOAT_PATTERN);
		if (token != null) {
			try {
				nr = Float.parseFloat(token);
			} catch (final NumberFormatException nfe) {
				throw formException(String.format("malformed integer '%s'",
						token));
			}
		}
		return nr;
	}

	/**
	 * StringBuilder does not support trimming natively and is a final class.
	 * Great idea. Emulating this functionality for front-trimming as we could
	 * have arbitrary intend at the beginning of our input.
	 * 
	 * @param sb
	 *            stringbuilder to trim
	 */
	private void trimFront(final StringBuilder sb) {
		int whitespaces = 0;
		final Matcher matcher = WHITESPACE_PATTERN.matcher(sb.toString());
		if (matcher.find()) {
			whitespaces = matcher.group().length();
		}
		sb.delete(0, whitespaces);
	}

	protected void consume(final StringBuilder sb) {
		sb.delete(0, sb.length());
	}

	protected ParseException formException(final String mes) {
		return new ResultParseException(mes);
	}
}
