package edu.single.mips;

import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;

import edu.single.mips.barriers.EXBarrier;
import edu.single.mips.barriers.IDBarrier;
import edu.single.mips.barriers.IFBarrier;
import edu.single.mips.barriers.MEMBarrier;

public class LsdComponents extends Library {
	private List<Tool> tools;

	public LsdComponents() {
		tools = Arrays.asList(new Tool[] {
			new AddTool(new Teste()),	
			new AddTool(new ControlUnit()),
			new AddTool(new InstructionFetch()),
			new AddTool(new Execute()),
			new AddTool(new IFBarrier()),
			new AddTool(new IDBarrier()),
			new AddTool(new EXBarrier()),
			new AddTool(new MEMBarrier()),
			new AddTool(new InstructionSplitter()),
			new AddTool(new HazardUnit()),
		});
	}

	@Override
	public String getName() {
		return "LSD-MIPS";
	}

	@Override
	public String getDisplayName() {
		return "LSD MIPS";
	}

	@Override
	public List<Tool> getTools() {
		return tools;
	}

	@Override
	public boolean removeLibrary(String Name) {
		return false;
	}
}
