package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import structure.Backend;

public class Visualisation extends JFrame implements MouseListener {
	private static final long serialVersionUID = 1L;
	private final Display _display;
	private final JScrollPane _scrollpane;

	public Visualisation(final Dimension size) {
		setSize(size);
		_display = new Display(size, new Backend());
		_scrollpane = new JScrollPane(_display);
		setLayout(new BorderLayout());
		add(_scrollpane, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		_display.setTree(new Backend().getTree());
		_display.addMouseListener(this);

	}

	public void zoom(final float factor) {
		System.out.println(factor);
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
		v.zoom(4);
	}

	@Override
	public void mouseClicked(final MouseEvent e) {

	}

	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(final MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		if (e.getButton() == e.BUTTON1) {
			zoom(2);
		} else {
			zoom(0.5f);
		}
	}
}
