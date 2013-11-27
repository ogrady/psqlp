package io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import parser.Parser;
import parser.RelOptParser;
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

	/**
	 * Constructor
	 * 
	 * @param messageParser
	 *            the parser the input is passed to once the buffer is full (in
	 *            termes of "contains a complete message")
	 */
	private MessageBuffer(final Parser<?> messageParser) {
		_buffer = new ArrayList<String>();
		_parser = messageParser;
	}

	@Override
	public synchronized void receive(String line) {
		line = line.trim();
		if (line.startsWith("RELOPTINFO") && !_buffer.isEmpty()) {
			try {
				_parser.parse(_buffer);
			} catch (final ParseException e) {
				e.printStackTrace();
			}
			_buffer.clear();
		}
		if (!line.equals("")) {
			_buffer.add(line);
		}
	}

	public static void main(final String[] args) throws IOException {
		new ContinuousInputStream().read(new File("dummy"), new MessageBuffer(
				new RelOptParser()));
	}

}
