package edu.single.mips;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.cburch.logisim.gui.generic.LFrame;

public class DataFlow_Frame extends LFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DataFlow flow;
	
	public DataFlow_Frame() {
		this.setTitle("Base frame for Register table");
		this.setAlwaysOnTop(true);
		this.setSize(800, 200);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		flow = new DataFlow();
		JScrollPane x = new JScrollPane(flow.getTable());
		this.add(x);
		this.setVisible(true);
	}
	
	public DataFlow getFlow() {
		return flow;
	}
}
