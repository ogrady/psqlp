package gui.popup;

import gui.overview.LevelDisplay;
import gui.overview.Overview;
import gui.tree.TreeFactory;
import gui.tree.TreePopup;

import java.awt.Dimension;
import java.awt.Window;
import java.util.Collection;

import parser.objects.Path;
import parser.objects.RelOptInfo;
import structure.Tree;

/**
 * Generates a tree-popup from several elements.<br>
 * TODO Some of those are probably never used should be pruned upon final
 * cleanup
 * 
 * @author Daniel
 * 
 */
public class PopupFactory {
	private static final Dimension SIZE = new Dimension(700, 300);

	/**
	 * Creates a popup from an {@link Overview}. This will display ALL paths
	 * from ALL reloptinfos from ALL levels of the overview!
	 * 
	 * @param owner
	 *            owning window
	 * @param overview
	 *            overview to create the popup from
	 * @return invisible popup
	 */
	public static TreePopup<Path> create(final Window owner,
			final Overview overview) {
		final TreePopup<Path> popup = new TreePopup<Path>(owner,
				overview.getName(), SIZE);
		for (final LevelDisplay display : overview._levels.values()) {
			for (final RelOptInfo reloptinfo : display._reloptinfos) {
				for (final Path path : reloptinfo._pathlist) {
					popup._trees.addTree(TreeFactory.treeify(path),
							path._strategy.toString() + path._ids.toString());
				}
			}
		}
		return popup;
	}

	/**
	 * Creates a popup from a {@link LevelDisplay}. This will display ALL paths
	 * from ALL reloptinfos within the level!
	 * 
	 * @param owner
	 *            owning window
	 * @param display
	 *            display to create the popup from
	 * @return invisible popup
	 */
	public static TreePopup<Path> create(final Window owner,
			final LevelDisplay display) {
		final TreePopup<Path> popup = new TreePopup<Path>(owner,
				display.getName(), SIZE);
		for (final RelOptInfo reloptinfo : display._reloptinfos) {
			for (final Path path : reloptinfo._pathlist) {
				popup._trees.addTree(TreeFactory.treeify(path),
						path._strategy.toString() + path._ids.toString());
			}
		}
		return popup;
	}

	/**
	 * Creates a popup from a list of relopts. This will display ALL paths from
	 * ALL reloptinfos within the list!
	 * 
	 * @param owner
	 *            owning window
	 * @param list
	 *            arbitrary list of reloptinfos to display the popup from
	 * @return invisible popup
	 */
	public static TreePopup<Path> create(final Window owner,
			final Collection<RelOptInfo> list) {
		final TreePopup<Path> popup = new TreePopup<Path>(owner, ""
				+ list.size(), SIZE);
		for (final RelOptInfo reloptinfo : list) {
			for (final Path path : reloptinfo._pathlist) {
				popup._trees.addTree(TreeFactory.treeify(path),
						path._strategy.toString() + path._ids.toString());
			}
		}
		return popup;
	}

	/**
	 * Creates a popup from a {@link RelOptInfo}. This will display ALL paths
	 * from the reloptinfo!<br>
	 * Usage: clicking on a button (representing a reloptinfo) within the
	 * overview
	 * 
	 * @param owner
	 *            owning window
	 * @param reloptinfo
	 *            the reloptinfo to create the popup from
	 * @return invisible popup
	 */
	public static TreePopup<Path> create(final Window owner,
			final RelOptInfo reloptinfo) {
		final TreePopup<Path> popup = new TreePopup<Path>(owner, "Level "
				+ reloptinfo._ids.size(), SIZE);
		for (final Path p : reloptinfo._pathlist) {
			final Tree<Path> tree = TreeFactory.treeify(p);
			popup._trees
					.addTree(tree, tree._root._element._strategy.toString());
		}
		return popup;
	}
}
