package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

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

	/**
	 * Constructor
	 * 
	 * @param size
	 *            initial size of the gui
	 */
	public Visualisation(final Dimension size) {
		super("PostgreSQL Join-Plan Visualizer");
		setSize(size);
		_display = new Display(size);
		_scrollpane = new JScrollPane(_display);
		setLayout(new BorderLayout());
		add(_scrollpane, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		_display.setTree(new Backend().getTree());
		_display.addMouseListener(this);

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

	public static void main(final String[] args) {
		final Visualisation v = new Visualisation(new Dimension(800, 500));
		v.setVisible(true);
		v.zoom(0);
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
}
