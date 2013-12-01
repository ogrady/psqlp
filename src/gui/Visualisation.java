package gui;

import io.FileParser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import structure.Backend;

/**
 * GUI that holds a resizable Display on which a tree can be visualized
 * 
 * @author Daniel
 * 
 */
public class Visualisation extends JFrame implements MouseListener {
	private static final long serialVersionUID = 1L;
	private final Display _display;
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

		_display = new Display(size);
		_display.setTree(new Backend().getTree());
		_display.addMouseListener(this);
		_scrollpane = new JScrollPane(_display);
		_parser = new FileParser();

		setJMenuBar(createMenu());
		add(_scrollpane, BorderLayout.CENTER);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent arg0) {
				_display.reTree();
			}
		});

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
					_parser.read(chooser.getSelectedFile());
				}
			}
		});
		menu.add(item);
		menuBar.add(menu);
		menu = new JMenu("Zoom");
		item = new JMenuItem("In", KeyEvent.VK_I);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.ALT_MASK));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent ae) {
				zoom(2);
			}
		});
		menu.add(item);
		item = new JMenuItem("Out", KeyEvent.VK_O);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.ALT_MASK));
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				zoom(0.5f);
			}
		});
		menu.add(item);
		menuBar.add(menu);
		return menuBar;
	}

	/**
	 * Zooms in on the display which solves the problem of crowded areas of the
	 * tree where nodes may overlap. Zooming in causes the Display to show a
	 * scrollbar
	 * 
	 * @param factor
	 *            the factor by which to zoom (2 for doubled size, 0.5f for
	 *            zooming out)
	 */
	public void zoom(final float factor) {
		_display.setPreferredSize(new Dimension(
				(int) (_display.getSize().width * factor), (int) (_display
						.getSize().height * factor)));

		_display.setTree(new Backend().getTree());
		repaint();
		revalidate();
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
	}

	@Override
	public void mousePressed(final MouseEvent e) {
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			zoom(2);
		} else {
			zoom(0.5f);
		}
	}

	public static void main(final String[] args) {
		final Visualisation v = new Visualisation(new Dimension(800, 500));
		v.setVisible(true);
		v.zoom(0);
	}
}
