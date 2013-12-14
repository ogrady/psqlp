package gui.popup;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Popup extends JDialog {
	private static final long serialVersionUID = 1L;

	public Popup(final Frame owner) {
		super(owner, "Details", true);
		final String[] columns = { "ID", "Rel1", "Strategy", "Rel2", "Cost",
				"Rows" };
		final Object[][] data = {
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" },
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" },
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" },
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" },
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" },
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" },
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" },
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" },
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" },
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" },
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" },
				{ "90", "R1", "HashJoin", "R2", "0..45", "44" } };
		final JTable table = new JTable(data, columns);
		final JScrollPane scrollpane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		add(scrollpane);

		pack();
	}
}
