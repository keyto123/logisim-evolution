package edu.single.mips.barriers;

import java.util.ArrayList;
import java.util.List;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;

import edu.single.funcoes.LsdFunctions;

public class MEMBarrier extends Barrier {

	public MEMBarrier() {
		super("MEM/WB");
		this.ports += 10;
		
		Bounds bounds = Bounds.create(-30, -260, 60, 520);
		this.setOffsetBounds(bounds);
		
		int distances[] = LsdFunctions.getDistanceFromMiddle(bounds);
		
		List<Port> ps = new ArrayList<Port>();
		
		// Base ports
		ps.add(new Port(-10, distances[1], Port.INPUT, 1)); // Clock
		ps.add(new Port(10, distances[1], Port.INPUT, 1)); // Clear
		
		// upper
		addPort(ps, 2, -distances[0], -distances[1] + 20, 1);
		addPort(ps, 4, -distances[0], -distances[1] + 40, 1);
		
		// middle
		addPort(ps, 6, -distances[0], 20, 32);
		
		// bottom
		addPort(ps, 8, -distances[0], 80, 32);
		addPort(ps, 10, -distances[0], 190, 5);
		
		this.setPorts(ps);
	}
	
	@Override
	public void addPort(List<Port> list, int index, int posx, int posy, int width) {
		super.addPort(list, index, posx, posy, width);
	}
	
	@Override
	public void paintInstance(InstancePainter painter) {
		super.paintInstance(painter);
	}
	
	@Override
	public void propagate(InstanceState state) {
		super.propagate(state);
	}
	
}
