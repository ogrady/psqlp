package io;

import io.logger.Logger;

import java.io.File;
import java.io.IOException;

import structure.Backend;

public class FileParser {
	public final Backend _backend;
	private final ContinuousInputStream _stream;
	private final MessageBuffer _buffer;
	private Thread _thread;
	public final Logger _logger;

	public FileParser(final Logger logger) {
		_logger = logger;
		_stream = new ContinuousInputStream();
		_backend = new Backend(_logger);
		_buffer = new MessageBuffer(_backend);
	}

	public void read(final File file) {
		if (_thread != null) {
			_stream.stop();
		}
		_thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// TODO change to true
					_stream.read(file, _buffer, false);
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		});
		_thread.start();
	}

	public void stop() {
		_stream._reading = false;
		_buffer.flushBuffer();
	}
}
