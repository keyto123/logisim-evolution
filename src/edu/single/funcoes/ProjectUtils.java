package edu.single.funcoes;

import java.util.Iterator;
import java.util.Set;

import com.cburch.logisim.comp.Component;
import com.cburch.logisim.gui.generic.LFrame;
import com.cburch.logisim.gui.menu.LogisimMenuBar;
import com.cburch.logisim.proj.Project;

import edu.single.mips.DataFlow;
import edu.single.mips.DataFlow_Frame;

public class ProjectUtils {
	
	public static Set<Component> getComponents(Project proj) {
		return proj.getCurrentCircuit().getNonWires();
	}
	
	public static Iterator<Component> getComponentsIterator(Project proj) {
		return getComponents(proj).iterator();
	}
	
	public static LFrame getFrame(Project proj) {
		return proj.getFrame();
	}
	
	public static LogisimMenuBar getMenuBar(Project proj) {
		LogisimMenuBar lmb = (LogisimMenuBar)proj.getFrame().getJMenuBar();
		return lmb;
	}
	
	public static DataFlow_Frame getDataFlowFrame(Project proj) {
		return getMenuBar(proj).getMenuLala().getDataFlowFrame();
	}
	
	public static DataFlow getDataFlow(Project proj) {
		return getDataFlowFrame(proj).getFlow();
	}
}
