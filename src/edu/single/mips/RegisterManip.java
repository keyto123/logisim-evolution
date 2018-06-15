package edu.single.mips;

import java.util.List;

import com.cburch.logisim.gui.generic.RegTabContent;

public class RegisterManip {

	private List<List<String>> registerList = RegTabContent.getRegContent();

	String registerListContent[][];

	public RegisterManip() {
		updateTable();
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
