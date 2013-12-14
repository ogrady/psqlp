package gui.popup;

import gui.overview.LevelDisplay;
import gui.overview.Overview;
import gui.tree.TreeFactory;
import gui.tree.TreePopup;

import java.awt.Dimension;
import java.awt.Frame;

import parser.objects.Path;
import parser.objects.RelOptInfo;
import structure.Tree;

/**
 * Generates a tree-popup from several elements.
 * 
 * @author Daniel
 * 
 */
public class PopupFactory {
	private static final Dimension SIZE = new Dimension(400, 400);

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
	public static TreePopup<Path> create(final Frame owner,
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
	public static TreePopup<Path> create(final Frame owner,
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
	public static TreePopup<Path> create(final Frame owner,
			final RelOptInfo reloptinfo) {
		final TreePopup<Path> popup = new TreePopup<Path>(owner, ""
				+ reloptinfo._ids.size(), SIZE);
		for (final Path p : reloptinfo._pathlist) {
			final Tree<Path> tree = TreeFactory.treeify(p);
			popup._trees
					.addTree(tree, tree._root._element._strategy.toString());
		}
		return popup;
	}
}
