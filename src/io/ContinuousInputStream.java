package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ContinuousInputStream {
	private boolean reading;
	private BufferedReader in;

	/**
	 * Stops the reader from reading and closes the inputstream if it is opened
	 */
	public void stop() {
		this.reading = false;
		if (this.in != null) {
			try {
				this.in.close();
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
	 * @throws IOException
	 *             when the file is not accessible or is a directory
	 */
	public void read(final File file, final IInputReceiver destination)
			throws IOException {
		reading = true;
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
			this.in = new BufferedReader(new FileReader(file));
			String line;
			while (reading) {
				line = in.readLine();
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
			this.stop();
		}
	}

	public static void main(final String[] args) throws IOException {
		new ContinuousInputStream().read(new File("dummy"),
				new IInputReceiver() {

					@Override
					public void receive(final String line) {
						System.out.println(line);
					}
				});
	}

}
