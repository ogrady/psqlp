package gui.popup;

import java.awt.Graphics;

abstract public class Renderer<R> {
	protected R _renderable;

	public Renderer(final R renderable) {
		_renderable = renderable;
	}

	abstract public void render(final Graphics g, final int x, final int y);

}
