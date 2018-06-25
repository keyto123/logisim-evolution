package edu.single.mips.barriers;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;

import edu.single.funcoes.LsdFunctions;

public class IFBarrier extends Barrier {
	
	public IFBarrier() {
		super("IF/ID");
		this.ports += 4;
		
		Bounds bounds = Bounds.create(-30, -130, 60, 260);
		this.setOffsetBounds(bounds);
		
		int distances[] = LsdFunctions.getDistanceFromMiddle(bounds);
		
		Port ps[] = new Port[] {
			// Base ports
			new Port(-20, distances[1], Port.INPUT, 1), // Clock
			new Port(20, distances[1], Port.INPUT, 1), // Reset
			
			// Relative ports
			new Port(-distances[0], -distances[1] + 60, Port.INPUT, 32),
			new Port(distances[0], -distances[1] + 60, Port.OUTPUT, 32),
			new Port(-distances[0], distances[1] - 30, Port.INPUT, 32),
			new Port(distances[0], distances[1] - 30, Port.OUTPUT, 32),
			
			// Special enable for this barrier
			new Port(0, distances[1], Port.INPUT, 1),
		};
		
		this.setPorts(ps);	
	}
	
	@Override
	public void propagate(InstanceState state) {
		if(state.getPortValue(6).toIntValue() == 0) {
			return;
		}
		super.propagate(state);
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		super.paintInstance(painter);
		painter.drawPort(6, "en", Direction.SOUTH);
		
	}

}
