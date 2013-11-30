package gui.renderer;

import java.awt.Color;
import java.awt.Graphics;

import structure.TreeNode;

public class NodeRenderer extends Renderer<TreeNode<?>> {
	private static final int W = 40, H = 20;

	public NodeRenderer(final TreeNode<?> node) {
		super(node);
	}

	@Override
	public void render(final Graphics g, final int x, final int y) {
		final Color col = g.getColor();
		g.setColor(Color.GREEN);
		g.fillRect(x - W / 2, y - H / 2, W, H);
		g.setColor(col);
		g.drawString(_renderable.toString(), x, y);
	}

}
