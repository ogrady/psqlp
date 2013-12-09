package gui.overview;

import io.FileParser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

/**
 * GUI that holds a overview on all levels of the join
 * 
 * @author Daniel
 * 
 */
public class Visualisation extends JFrame {
	private static final long serialVersionUID = 1L;
	private final Overview _overview;
	private final JScrollPane _scrollpane;
	private final FileParser _parser;

	/**
	 * Constructor
	 * 
	 * @param size
	 *            initial size of the gui
	 */
	public Visualisation(final Dimension size) {
		super("PostgreSQL Join-Plan Visualizer");
		setSize(size);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		_overview = new Overview();
		_scrollpane = new JScrollPane(_overview);
		_parser = new FileParser();
		setJMenuBar(createMenu());
		add(_scrollpane, BorderLayout.CENTER);
	}

	/**
	 * Opens another file for reading
	 * 
	 * @param file
	 */
	public void read(final File file) {
		_parser.read(file);
	}

	/**
	 * Creates the menu bar with the elements:<br>
	 * <ul>
	 * <li>
	 * File
	 * <ul>
	 * <li>Load</li>
	 * </ul>
	 * </li>
	 * <li>
	 * Zoom
	 * <ul>
	 * <li>In</li>
	 * <li>Out</li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * @return the created menu
	 */
	public JMenuBar createMenu() {
		final JMenuBar menuBar = new JMenuBar();
		JMenuItem item;
		JMenu menu;

		menu = new JMenu("File");
		item = new JMenuItem("Load", KeyEvent.VK_L);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.ALT_MASK));
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final JFileChooser chooser = new JFileChooser();
				final int chosen = chooser.showOpenDialog(Visualisation.this);
				if (chosen == JFileChooser.APPROVE_OPTION) {
					// _parser.read(chooser.getSelectedFile());
					read(chooser.getSelectedFile());
				}
			}
		});
		menu.add(item);
		menuBar.add(menu);
		menu.add(item);
		menuBar.add(menu);
		return menuBar;
	}

	public static void main(final String[] args) {
		final Visualisation v = new Visualisation(new Dimension(800, 500));
		v.setVisible(true);
	}
}
