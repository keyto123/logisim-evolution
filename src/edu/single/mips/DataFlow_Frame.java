package edu.single.mips;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.cburch.logisim.gui.generic.LFrame;
import com.cburch.logisim.proj.Project;

public class DataFlow_Frame extends LFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DataFlow flow;
	//private Project proj;

	public DataFlow_Frame(Project proj) {
		//this.proj = proj;
		this.setTitle("Base frame for Register table");
		this.setAlwaysOnTop(true);
		this.setSize(800, 200);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		flow = new DataFlow(proj);
		JScrollPane x = new JScrollPane(flow.getTable());
		x.setIgnoreRepaint(true);
		x.setAutoscrolls(true);
		this.add(x);
		this.setVisible(true);
	}

	public DataFlow getFlow() {
		return flow;
	}
}
