package edu.single.mips;

import java.util.List;

import com.cburch.logisim.gui.generic.RegTabContent;

public class RegisterManip {

	private List<List<String>> registerList = RegTabContent.getRegContent();

	String registerListContent[][];
	
	public RegisterManip() {
		updateTable();
	}
	
	public static int getRegisterIndex(String register) {
		switch (register) {
		case "IF_MAIN":
			return 0;

		case "ID_MAIN":
			return 1;

		case "EX_MAIN":
			return 2;

		case "MEM_MAIN":
			return 3;

		case "WB_MAIN":
			return 4;

		default:
			return -1;
		}
	}

	private void updateTable() {
		registerListContent = new String[registerList.size()][2];
		int i = 0;
		for (List<String> list : registerList) {
			int j = 0;
			for (String s : list) {
				registerListContent[i][j] = s;
				j++;
			}
			i++;
		}
	}

	public String[][] getRegisterListContent() {
		updateTable();
		return registerListContent;
	}
	
	
}
