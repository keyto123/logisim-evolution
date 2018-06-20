package edu.single.mips;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.cburch.logisim.proj.Project;

import edu.cornell.cs3410.ProgramAssembler;
import edu.single.funcoes.LalaFunctions;

public class DataFlow {

	private Project proj;

	public RegisterManip regMan = new RegisterManip();

	private JTable table;
	private DefaultTableModel tableModel;
	private TableColumnModel columnModel;
	private static final String[] stages = { " IF", " ID", " EX", "MEM", " WB" };

	public DataFlow(Project proj) {
		this.proj = proj;
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
	
	private String getRegisterFrom(String s) {
			if(!s.contains("$")) {
				return null;					
			}
			int index = s.indexOf("$");
			StringBuilder bs = new StringBuilder();
			bs.append("$");
			index++;
			
			char curChar = s.charAt(index);
			do {
				bs.append(curChar);
				index++;
				curChar = s.charAt(index);
			} while(curChar >= '0' && curChar <= '9');
			return bs.toString();
	}

	private List<String> getInstructionRegisters(String code, String instructionType) {
		List<String> registers = new ArrayList<String>();
		
		String instruction = new String(code);
		if(instruction.contains(","))
			instruction = instruction.replaceAll(",", "");
		String instructionSplit[] = instruction.split(" ");
		
		switch(instructionType) {
		case "R":
			for(int i = 1; i < instructionSplit.length; i++) {
				registers.add(instructionSplit[i]);
			}
			break;
		
		case "I":
			for(int i = 1; i < instructionSplit.length - 1; i++) {
				registers.add(instructionSplit[i]);
			}
			registers.add(null);
			break;
			
		case "LW":
			registers.add(instructionSplit[1]);
			registers.add(getRegisterFrom(instructionSplit[2]));
			registers.add(null);
			break;
			
		case "SW":
			registers.add(null);
			registers.add(instructionSplit[1]);
			registers.add(getRegisterFrom(instructionSplit[2]));
			break;
			
		case "J":
			registers.add(null);
			if(instructionSplit[1].contains("$")) {
				registers.add(instructionSplit[1]);
			} else {
				registers.add(null);
			}
			registers.add(null);
			break;
		
		case "B":
			registers.add(null);
			for(int i = 1; i < 3; i++) {
				if(instructionSplit[i].contains("$")) {
					registers.add(instructionSplit[i]);
				} else {
					registers.add(null);
				}
			}
			break;
		
		default:
			System.out.println("Failed to get instruction type match");
			break;
		}
		return registers;
	}
	
	private String getInstructionType(int instruction) {
		int opcode = LalaFunctions.getBits(instruction, 26, 6);
		switch(opcode) {
		case 0:
			return "R";
		
		case 0x23: // lw
			return "LW";
			
		case 0x2B: // sw
			return "SW";
		
		case 0x09: // addiu
		case 0x0A: // slti
		case 0x0B: // sltiu
		case 0x0C: // andi
		case 0x0D: // ori
		case 0x0F: // lui
			return "I";
			
		case 0x02: // j
			return "J";
		
		case 0x04: // beq
		case 0x05: // bne
		case 0x06: // blez
			return "B";
			
		default:
			return null;
		}
	}

	private boolean checkHazard(int level) {
		int lastColumn = table.getColumnCount() - 1;

		// TODO: fix unhandled intructions and change getInstructionElement
		String currentCode = table.getValueAt(0, lastColumn).toString();
		int instruction = Integer.parseInt(LalaFunctions.getComponentValue(proj, "MIPSProgramROM", null, 1).get(0));
		List<String> regs = getInstructionRegisters(currentCode, getInstructionType(instruction));
		
		if(currentCode == null || currentCode.equals("bubble") || currentCode.equals(ProgramAssembler.disassemble(0, 0))) {
			return false;
		}

		switch (level) {
		case 0:
			int length = lastColumn > 2 ? 2 : 1;
			for (int i = 0; i < length; i++) {
				int testingColumn = lastColumn - (i + 1);
				String testingCode = table.getValueAt(0, testingColumn).toString();
				String reg = getInstructionRegisters(testingCode, "R").get(0);
				System.out.println("comp reg: " + reg);
				System.out.println("reg1: " + regs.get(1) + " | reg2: " + regs.get(2));
				
				if (reg.equals(regs.get(1)) || reg.equals(regs.get(2))) {
					JOptionPane.showMessageDialog(proj.getFrame(), "hazard: " + currentCode + " & " + testingCode
							+ " at [IF][" + lastColumn + "] & [IF][" + testingColumn + "]");
				}
			}
			break;
		}
		return false;
	}

	private String getCurrentCode() {
		String value = null, currentCode = null;

		List<String> valueList = LalaFunctions.getComponentValue(proj, "MIPSProgramROM", null, 1);
		if (valueList.isEmpty()) {
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

	public void updateTable() {
		int columns = table.getColumnCount();

		this.table.setSize(5, columns + 1);
		this.tableModel.addColumn(columns + 1);

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
		this.checkHazard(0);
	}
}
