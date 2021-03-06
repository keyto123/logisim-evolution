package edu.cornell.cs3410; // com.cburch.incr;

import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;

public class Components extends Library {
	private List<Tool> tools;

	public Components() {
		tools = Arrays.asList(new Tool[] { new AddTool(new RegisterFile32()), new AddTool(new Program32()),
				new AddTool(new ALU()), new AddTool(new Incrementer()), new AddTool(new Video()),
				new AddTool(new Ram()), new AddTool(new SPIM()), new AddTool(new RamIO()),
				});
	}

	@Override
	public String getName() {
		return "CS3410-Components";
	}

	@Override
	public String getDisplayName() {
		return "CS3410 Components";
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
