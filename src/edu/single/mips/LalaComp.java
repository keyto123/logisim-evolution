package edu.single.mips;

import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;

public class LalaComp extends Library {
	private List<Tool> tools;

	public LalaComp() {
		tools = Arrays.asList(new Tool[] { 
					new AddTool(new ControlUnit()), new AddTool(new AluControl()),
					new AddTool(new InstructionFetch()), new AddTool(new Execute()),
					new AddTool(new ClkRegister()),
				});
	}

	@Override
	public String getName() {
		return "lala-experiments";
	}

	@Override
	public String getDisplayName() {
		return "Lala Experiments";
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
