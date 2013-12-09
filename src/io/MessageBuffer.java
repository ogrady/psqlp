package io;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.Parser;
import exception.ParseException;

/**
 * The message buffer keeps track of the incoming messages from the continous
 * stream from the log file. Whenever we receive a new message starting with
 * "RELOPTINFO" we close our former buffer and send it to the parser and start a
 * new buffer for upcoming messages.
 * 
 * @author Daniel
 * 
 */
public class MessageBuffer implements IInputReceiver {
	private final List<String> _buffer;
	private final Parser<?> _parser;
	private int _indent;

	/**
	 * Constructor
	 * 
	 * @param messageParser
	 *            the parser the input is passed to once the buffer is full (in
	 *            termes of "contains a complete message")
	 */
	public MessageBuffer(final Parser<?> messageParser) {
		_buffer = new ArrayList<String>();
		_parser = messageParser;
		_indent = -1;
	}

	/**
	 * This method buffers the incoming lines into blocks and passes those
	 * blocks to the parser as soon as it determines that one block has ended.<br>
	 * While blank lines are discarded, blocks are detected via their indent.
	 * Outermost blocks all have the same indent while their child-nodes are
	 * shifted further to the right.
	 */
	@Override
	public synchronized void receive(final String rawLine) {
		final String trimmed = rawLine.trim();
		// buffer is empty -> we just parsed a block -> start a new block by
		// remembering this indent
		if (!trimmed.equals("")) {
			if (_buffer.isEmpty()) {
				_indent = getIndent(rawLine);
			}
			// we have a running block and the indent of the new line is the
			// same as
			// the line that started the block
			// -> the block has ended and a new block has started
			// -> parse the old block and clear the buffer
			else if (getIndent(rawLine) == _indent) {
				try {
					_parser.parse(_buffer);
				} catch (final ParseException e) {
					e.printStackTrace();
				}
				_buffer.clear();
			}
			if (relevant(rawLine)) {
				_buffer.add(trimmed);
			}
		}
	}

	/**
	 * Checks for the indent of a line aka its leading whitespace-characters,
	 * including tabs
	 * 
	 * @param line
	 *            line to get the indent for
	 * @return indent measured in whitespaces or 0 if the line is not indented
	 *         at all
	 */
	public int getIndent(final String line) {
		final Pattern whitespaces = Pattern.compile("^\\s*");
		final Matcher matcher = whitespaces.matcher(line);
		return matcher.find() ? matcher.group().length() : 0;
	}

	/**
	 * Checks whether the input is relevant or just lines from the log we don't
	 * need
	 * 
	 * @param line
	 *            the input
	 * @return true, if the line does not match any blacklist-criteria
	 */
	public boolean relevant(final String line) {
		final String trimmed = line.trim();
		// we discard blank lines (which is quite obvious) and lines that start
		// with DEBUG:. The latter because as seen in
		// the source those are not part of the printout and therefore have
		// other sources we are not interested in
		return !(trimmed.equals("") || trimmed.startsWith("DEBUG:"));
	}
}
