package edu.lala; // com.cburch.incr;

import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;

public class LalaComponents extends Library {
    private List<Tool> tools;

    public LalaComponents() {
        tools = Arrays.asList(new Tool[] {
                new AddTool(new If())
        });
    }

    @Override
    public String getName() { return "lala"; }
    
    @Override
    public String getDisplayName() { return "lala"; }
    
    @Override
    public List<Tool> getTools() { return tools; }
    
    public boolean removeLibrary(String Name) {
    	return false;
    }
}
