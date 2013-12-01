package io;

import java.io.File;
import java.io.IOException;

import parser.RelOptParser;

public class FileParser {
	private final ContinuousInputStream _stream;
	private final IInputReceiver _receiver;
	private Thread _thread;

	public FileParser() {
		_stream = new ContinuousInputStream();
		_receiver = new MessageBuffer(new RelOptParser());
	}

	public void read(final File file) {
		if (_thread != null) {
			// TODO when the inputstream is locked in the readLine method this
			// part will lock up the thread until another line is read
			_stream.stop();
		}
		_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					_stream.read(file, _receiver);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		});
		_thread.run();
	}
}
