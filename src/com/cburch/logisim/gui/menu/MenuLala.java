package com.cburch.logisim.gui.menu;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import edu.single.mips.DataFlow_Frame;

public class MenuLala extends Menu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LogisimMenuBar menubar;
	private JMenuItem testing = new JMenuItem();
	private JMenuItem updateTest = new JMenuItem();

	private DataFlow_Frame dataFlowFrame;

	public MenuLala(LogisimMenuBar menubar) {
		this.menubar = menubar;

		this.testing.addActionListener(a -> testing_buttonFunction());
		this.updateTest.addActionListener(a -> updateTest_buttonFunction());

		this.add(testing);
		this.add(updateTest);
	}

	private void updateTest_buttonFunction() {
		if (dataFlowFrame != null)
			dataFlowFrame.getFlow().updateTable();
		else
			JOptionPane.showMessageDialog(this, "Uso incorreto, abra a tabela primeiro.");
	}

	private void testing_buttonFunction() {
		if (dataFlowFrame != null) {
			JOptionPane.showMessageDialog(this,
					"Please close the last opened dataflow frame before creating a new one.");
			return;
		}
		dataFlowFrame = new DataFlow_Frame(menubar.getProject());
		dataFlowFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dataFlowFrame.dispose();
				dataFlowFrame = null;
			}
		});
	}

	public DataFlow_Frame getDataFlowFrame() {
		return dataFlowFrame;
	}

	@Override
	void computeEnabled() {
		this.setEnabled(true);
		menubar.fireEnableChanged();
	}

	public void localeChanged() {
		this.setText("lala");
		testing.setText("Testing");
		updateTest.setText("updateTest");
	}
}
