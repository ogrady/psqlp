package test;

import gui.Visualisation;

import java.awt.Dimension;

public class GuiStartup {
	public static void main(final String[] args) {
		Visualisation.DEBUG = args.length > 0
				&& args[0].equalsIgnoreCase("debug");
		final Visualisation v = new Visualisation(new Dimension(800, 500));
		v.setVisible(true);
	}
}
