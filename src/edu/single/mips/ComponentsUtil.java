package edu.single.mips;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.gui.menu.MenuLala;
import com.cburch.logisim.proj.Project;

import edu.cornell.cs3410.Program32;

public class ComponentsUtil {
	private static List<Program32> rom = new ArrayList<Program32>();
	private static MenuLala menu;
	
	public static void addRom(Program32 program32) {
		rom.add(program32);
	}
	
	public static Program32 getRom(int index) {
		return rom.get(index);
	}
	
	public static void setMenuLala(MenuLala _menu) {
		menu = _menu;
	}
	
	public static MenuLala getMenuLala() {
		return menu;
	}
	
	public static Project getProject() {
		return menu.getProject();
	}
	
	public static CircuitState getCircuitState() {
		return getProject().getCircuitState();
	}
	
	public static Circuit getCurrentCircuit() {
		return getProject().getCurrentCircuit();
	}
	
	public static Set<Component> getComponents() {
		return getCurrentCircuit().getNonWires();
	}
	
	public static Iterator<Component> getComponentsIterator() {
		return getComponents().iterator();
	}
}
