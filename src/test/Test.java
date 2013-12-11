package test;

import gui.OldVisualisation;
import io.ContinuousInputStream;
import io.MessageBuffer;
import io.logger.Logger;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import structure.Backend;

public class Test {
	public static void main(final String[] args) throws IOException {
		if (args.length < 2) {
			System.err
					.println("please call with at least 2 arguments: path to a logfile, whether to parse from start, [whether to use a gui]");
			System.exit(1);
		}
		final String filename = args[0];
		final boolean fromStart = Boolean.parseBoolean(args[1]);
		boolean withGui = false;
		if (args.length == 3) {
			withGui = Boolean.parseBoolean(args[2]);
		}
		if (withGui) {
			final OldVisualisation gui = new OldVisualisation(new Dimension(800, 600));
			gui.setVisible(true);
			gui.read(new File(filename));
		} else {
			new ContinuousInputStream().read(new File(filename),
					new MessageBuffer(new Backend(new Logger())), !fromStart);
		}

	}
}
