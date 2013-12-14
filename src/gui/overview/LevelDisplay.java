package gui.overview;

import gui.popup.PopupFactory;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import parser.objects.RelOptInfo;

/**
 * {@link LevelDisplay}s represent one level of the parsing. E.g. the first
 * level would display all the HDD-access-methods for the relations, the second
 * one would represent the 2-way-joins and so on. The {@link LevelDisplay}s are
 * then put into an {@link Overview}.<br>
 * Each {@link LevelDisplay} keeps track of the reloptinfos that are added to it
 * so that the information can be retrieved for more detailed displaying from
 * here.
 * 
 * @author Daniel
 * 
 */
public class LevelDisplay extends JComponent {
	private static final long serialVersionUID = 1L;
	public final int _level;
	public final List<RelOptInfo> _reloptinfos;

	/**
	 * Constructor
	 * 
	 * @param level
	 *            the level of the display (1 = direct access, 2 = 2-way-join,
	 *            ...)
	 */
	public LevelDisplay(final int level) {
		_level = level;
		_reloptinfos = new ArrayList<RelOptInfo>();
		setLayout(new FlowLayout());
		setBorder(BorderFactory.createTitledBorder("Level " + level));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent me) {
				PopupFactory.create(
						(JFrame) SwingUtilities.getRoot(LevelDisplay.this),
						LevelDisplay.this).setVisible(true);
			}
		});
	}

	/**
	 * Adds another {@link RelOptInfo} to the display. It will be displayed as a
	 * button that is labeled with the list of ids it joins. Clicking on it
	 * summons a popup that displayes the list of considered paths from this
	 * reloptinfo.
	 * 
	 * @param reloptinfo
	 *            reloptinfo to add
	 */
	public void addRelOptInfo(final RelOptInfo reloptinfo) {
		_reloptinfos.add(reloptinfo);
		final JButton bu = new JButton(reloptinfo._ids.toString());
		bu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent me) {
				/*	final TreePopup<Path> popup = new TreePopup<Path>(
							(JFrame) SwingUtilities.getRoot(LevelDisplay.this), ""
									+ _level, new Dimension(400, 400));
					for (final Path p : relopt._pathlist) {
						final Tree<Path> tree = TreeFactory.treeify(p);
						popup._trees.addTree(tree,
								tree._root._element._strategy.toString());
					}
					*/
				PopupFactory.create(
						(JFrame) SwingUtilities.getRoot(LevelDisplay.this),
						reloptinfo).setVisible(true);

			}
		});
		bu.setToolTipText(reloptinfo._ids.toString());
		add(bu);
	}

}
