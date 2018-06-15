package com.cburch.logisim.gui.menu;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import com.cburch.logisim.gui.generic.LFrame;

import edu.single.mips.RegisterManip;

public class MenuLala extends Menu {

	private LogisimMenuBar menubar;
	private JMenuItem testing = new JMenuItem();
	
	// Todo: keep testing this -- write function for update state to simulate clock
	private JMenuItem updateTest = new JMenuItem();

	private String currentState[][] = new String[5][2];
	private List<List<String>> fullState = new ArrayList<List<String>>();
	
	RegisterManip regMan = new RegisterManip();

	public MenuLala(LogisimMenuBar menubar) {
		this.menubar = menubar;
		testing.addActionListener(a -> testing_buttonFunction());
		add(testing);
	}
	
	private void updateState() {
		String list[][] = regMan.getRegisterListContent();
		System.out.println(list.length);
		for (int i = 0, size = list.length, index = -1; i < size; i++, index = -1) {
			switch(list[i][0]) {
			case "IF_MAIN":
				index = 0;
				break;
				
			case "ID_MAIN":
				index = 1;
				break;
				
			case "EX_MAIN":
				index = 2;
				break;
				
			case "MEM_MAIN":
				index = 3;
				break;
				
			case "WB_MAIN":
				index = 4;
				break;
			}
			
			if(index != -1) {
				for(int j = 0; j < 2; j++) {
					currentState[index][j] = list[i][j];
				}				
			}
		}
		printArray(currentState);
	}
	
	private <T> void printArray(T[][] obj) {
		for(T[] ob : obj) {
			for(T o : ob) {
				System.out.print(o + " ");
			}
			System.out.println();
		}
	}

	private void testing_buttonFunction() {
		LFrame frame = new LFrame();
		frame.setTitle("Base frame for Register table");
		frame.setAlwaysOnTop(true);
		frame.setSize(150, 150);

		updateState();

		frame.setVisible(true);
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
