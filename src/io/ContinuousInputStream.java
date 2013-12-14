package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ContinuousInputStream {
	public volatile boolean _reading;
	private BufferedReader _in;

	/**
	 * Stops the reader from reading and closes the inputstream if it is opened
	 */
	public void stop() {
		_reading = false;
		if (_in != null) {
			try {
				_in.close();
				_in = null;
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * reads a file continuously. That means whenever something is being append
	 * to the file the read line will be sent to the receiver
	 * 
	 * @param file
	 *            file to read from
	 * @param destination
	 *            where to read to
	 * @param jumpToEnd
	 *            start reading at the end of the file
	 * @throws IOException
	 *             when the file is not accessible or is a directory
	 */
	public void read(final File file, final IInputReceiver destination,
			final boolean jumpToEnd) throws IOException {
		// this will close iff this stream is already reading from another file
		stop();
		_reading = true;
		if (!file.exists()) {
			throw new IOException(String.format(
					"can't read file '%s' (does not exist)",
					file.getAbsoluteFile()));
		}
		if (file.isDirectory()) {
			throw new IOException(String.format(
					"can't read file '%s' (is a directory)",
					file.getAbsoluteFile()));
		}
		try {
			_in = new BufferedReader(new FileReader(file));
			String line;
			if (jumpToEnd) {
				while (_in.readLine() != null) {
					// intentionally left blank - jumps to the end of the file
				}
			}
			while (_reading) {
				line = _in.readLine();
				if (line != null) {
					synchronized (line) {
						destination.receive(line);
					}
				}
			}
		} catch (final FileNotFoundException e) {
			// checked beforehand
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			stop();
		}
	}

	public static void main(final String[] args) throws IOException {
		new ContinuousInputStream().read(new File("dummy"),
				new IInputReceiver() {
					@Override
					public void receive(final String line) {
						System.out.println(line);
					}
				}, true);
	}
}
