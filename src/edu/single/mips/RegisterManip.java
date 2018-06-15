package edu.single.mips;

import java.util.List;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.gui.generic.RegTabContent;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.tools.Strings;

import edu.single.funcoes.LalaFunctions;

public class RegisterManip extends InstanceFactory {

	private int oldClock = 0;
	private List<List<String>> allRegisters = RegTabContent.getRegContent();
	
	public RegisterManip() {
		super("RegManip", Strings.getter("Testing"));
		Bounds bounds = Bounds.create(-40, -40, 80, 80);
		this.setOffsetBounds(bounds);
		
		this.setPorts(new Port[] {new Port(-40, 0, Port.INPUT, 1)});
		
	}
	
	@Override
	public void paintInstance(InstancePainter painter) {
		painter.drawBounds();
		painter.drawPort(0);
		LalaFunctions.setTitle(painter, this);
		
	}

	@Override
	public void propagate(InstanceState state) {
		int clk = state.getPortValue(0).toIntValue();
		if(clk != oldClock) {
			if(clk == 1) {
				for(List<String> list : allRegisters) {
					if(list.get(0).equals("lulu"))
						continue;
					for(String s : list) {
						System.out.print(s + " ");
					}
					System.out.println();
				}
			}
			oldClock = clk;
		}
	}
}
