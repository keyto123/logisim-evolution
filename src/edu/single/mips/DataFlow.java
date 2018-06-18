package edu.single.mips;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.cburch.logisim.proj.Project;

import edu.cornell.cs3410.ProgramAssembler;
import edu.single.funcoes.LalaFunctions;

public class DataFlow {

	private Project proj;
	
	private String currentState[][] = new String[5][2];
	private String list[][];

	public RegisterManip regMan = new RegisterManip();

	private JTable table;
	private DefaultTableModel tableModel;
	private TableColumnModel columnModel;
	private static final String[] stages = { " IF", " ID", " EX", "MEM", " WB" };

	public DataFlow(Project proj) {
		this.proj = proj;
		this.updateState();
		this.createTable();

		this.tableModel.addColumn(1);
		this.tableModel.setValueAt(getCurrentCode(), 0, 1);
		this.adjustTable();
	}

	public JTable getTable() {
		return this.table;
	}

	private void adjustColumnWidth(int column, int width) {
		this.columnModel.getColumn(column).setMinWidth(width);
		this.columnModel.getColumn(column).setMaxWidth(width);
	}

	private void adjustTable() {
		int columns = table.getColumnCount();

		this.adjustColumnWidth(0, 40);
		for (int i = 1; i < columns; i++) {
			this.adjustColumnWidth(i, 140);
			this.columnModel.getColumn(i).setHeaderValue(i);
		}
	}
	
	private String getInstructionElement(String code, int pos) {
		String instruction = new String(code);
		instruction.replaceAll(",", "");
		instruction.replaceAll("$", "");
		String instructionSplit[] = instruction.split(" ");
		for(String s : instructionSplit) {
			System.out.println(s);
		}
		
		return instructionSplit[pos];
	}
	
	private boolean checkHazard(int level) {
		int lastColumn = table.getColumnCount() - 1;
		String currentCodeRegisters[] = new String[2];
		
		for(int i = 0; i < 2; i++) {
			currentCodeRegisters[i] = getInstructionElement(currentState[0][0], i + 2);
		}
		
		switch(level) {
		case 0:
			for(String currentReg : currentCodeRegisters) {
				for(int i = 1; i <= 2; i++) {
					String reg = getInstructionElement(table.getValueAt(i, lastColumn - i).toString(), 1);
					if(currentReg.equals(reg)) {
						
					}
				}
			}
			break;
		}
		return false;
	}

	private String getCurrentCode() {
		String value = null, currentCode = null;

		List<String> valueList = LalaFunctions.getComponentValue(proj, "MIPSProgramROM", null, 1);
		if(valueList.isEmpty()) {
			return null;
		}
		
		value = valueList.get(0);

		if (value != null) {
			currentCode = ProgramAssembler.disassemble(Integer.parseInt(value), 0);
		}

		return currentCode;
	}

	private void createTable() {

		tableModel = new DefaultTableModel(5, 1);
		for (int i = 0; i < 5; i++) {
			tableModel.setValueAt(stages[i], i, 0);
		}

		table = new JTable(tableModel) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredScrollableViewportSize() {
				return new Dimension(400, 240);
			}
		};
		table.setEnabled(false);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		columnModel = table.getColumnModel();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	private void updateState() {

		list = regMan.getRegisterListContent();
		for (int i = 1, size = list.length, index = -1; i < size; i++, index = -1) {
			index = RegisterManip.getRegisterIndex(list[i][0]);
			if (index != -1) {
				for (int j = 0; j < 2; j++) {
					currentState[index][j] = list[i][j];
				}
			}
		}
	}

	public void updateTable() {
		int columns = table.getColumnCount();
		
		this.table.setSize(5, columns + 1);
		this.tableModel.addColumn(columns + 1);
		this.updateState();

		if (columns > 0) {
			int i = 0;
			this.tableModel.setValueAt(this.getCurrentCode(), i, columns);

			for (i = i + 1; i < 5; i++) {
				String value;
				Object o = tableModel.getValueAt(i - 1, columns - 1);
				if (o == null) {
					break;
				}
				value = o.toString();

				if (value == null || columns == 1) {
					break;
				}
				this.tableModel.setValueAt(value, i, columns);
			}
		}

		this.adjustTable();
	}
}
