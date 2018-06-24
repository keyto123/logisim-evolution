package edu.single.mips.barriers;

import java.util.ArrayList;
import java.util.List;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;

import edu.single.funcoes.LsdFunctions;

public class IDBarrier extends Barrier {

	public IDBarrier() {
		super("ID/EX");
		this.ports += 28;
		
		Bounds bounds = Bounds.create(-20, -260, 40, 520);
		this.setOffsetBounds(bounds);
		
		int distance[] = LsdFunctions.getDistanceFromMiddle(bounds);
		
		List<Port> ps = new ArrayList<Port>();
		ps.add(new Port(-10, -bounds.getY(), Port.INPUT, 1)); // Clock
		ps.add(new Port(10, -bounds.getY(), Port.INPUT, 1)); // Reset
		
		// upper
		int i = 2;
		for(i = 2; i <= 14; i += 2) {
			if(i == 10) {
				addPort(ps, i, -distance[0], -distance[1] + i * 10, 4);
			} else {
				addPort(ps, i, -distance[0], -distance[1] + i * 10, 1);				
			}
		}
		
		// Middle
		addPort(ps, 16, -distance[0], -10, 32);
		addPort(ps, 18, -distance[0], 90, 32);
		
		// Bottom
		for(i = 20; i <= 26; i += 2) {
			addPort(ps, i, -distance[0], 160 + (i - 20) * 10, 5);
		}
		
		addPort(ps, i, -distance[0], 160 + (i - 20) * 10, 32);
		
		this.setPorts(ps);
	}
	
	@Override
	public void propagate(InstanceState state) {
		super.propagate(state);
	}
	
	@Override
	public void paintInstance(InstancePainter painter) {
		super.paintInstance(painter);
		
	}
}
