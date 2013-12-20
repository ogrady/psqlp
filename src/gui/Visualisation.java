package gui;

import gui.overview.Overview;
import io.FileParser;
import io.logger.Logger;

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
import javax.swing.ToolTipManager;

import listener.IBackendListener;
import parser.objects.RelOptInfo;

/**
 * GUI that holds a overview on all levels of the join
 * 
 * @author Daniel
 * 
 */
public class Visualisation extends JFrame implements IBackendListener {
	private static final long serialVersionUID = 1L;
	public static boolean DEBUG = false;
	private final Overview _overview;
	private final JScrollPane _scrollpane;
	private final FileParser _parser;
	public final Logger _logger;

	/**
	 * Constructor
	 * 
	 * @param size
	 *            initial size of the gui
	 */
	public Visualisation(final Dimension size) {
		super("PostgreSQL Join-Plan Visualizer");
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		setSize(size);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		_logger = new Logger();
		_logger.acceptAll();
		_overview = new Overview(_logger);
		_scrollpane = new JScrollPane(_overview);
		_parser = new FileParser(_logger);
		_parser._backend.getListeners().registerListener(this);
		setJMenuBar(createMenu());
		add(_scrollpane, BorderLayout.CENTER);
	}

	/**
	 * Opens another file for reading
	 * 
	 * @param file
	 */
	public void read(final File file) {
		_parser.read(file, !DEBUG);
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
			public void actionPerformed(final ActionEvent ae) {
				final JFileChooser chooser = new JFileChooser();
				final int chosen = chooser.showOpenDialog(Visualisation.this);
				if (chosen == JFileChooser.APPROVE_OPTION) {
					read(chooser.getSelectedFile());
				}
			}
		});
		menu.add(item);
		item = new JMenuItem("Stop Parsing", KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.ALT_MASK));
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent ae) {
				_parser.stop();
			}
		});
		menu.add(item);
		item = new JMenuItem("Clear", KeyEvent.VK_C);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.ALT_MASK));
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent ae) {
				try {
					_parser.stop();
				} catch (final Exception e) {
					e.printStackTrace();
				}
				_overview.clear();
			}
		});
		menu.add(item);
		menuBar.add(menu);
		menu.add(item);
		menuBar.add(menu);
		return menuBar;
	}

	@Override
	public void onNewRelOptInfo(final RelOptInfo roi) {
		_overview.addRelOptInfo(roi, roi._ids.size());
	}

	public static void main(final String[] args) {
		DEBUG = true;
		final Visualisation v = new Visualisation(new Dimension(800, 500));
		v.setVisible(true);
		// v._logger.ignoreAll();
		// v._logger.ignore(LogMessageType.PARSER_IN);
		v.read(new File("C:\\Users\\Daniel\\Desktop\\_dummy"));
	}
}
