package com.cburch.logisim.gui.menu;

import javax.swing.JMenuItem;

public class MenuLala extends Menu {

	private LogisimMenuBar menubar;
	private JMenuItem testing = new JMenuItem();
	
	public MenuLala(LogisimMenuBar menubar) {
		this.menubar = menubar;
		testing.addActionListener(a -> testing_buttonFunction());
		add(testing);
	}
	
	private void testing_buttonFunction() {
		System.out.println("Worked");
	}

	@Override
	void computeEnabled() {
		this.setEnabled(true);
		menubar.fireEnableChanged();
	}
	
	public void localeChanged() {
		this.setText("lala");
		testing.setText("Testing");
	}
}
