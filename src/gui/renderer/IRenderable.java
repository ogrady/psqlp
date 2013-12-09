package gui.renderer;

import javax.swing.JComponent;

public interface IRenderable<C extends JComponent> {
	public C getRepresentation();

	public void setRepresentation(C representation);
}
