package edu.single.mips.barriers;

import java.util.ArrayList;
import java.util.List;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.instance.Port;

import edu.single.funcoes.LsdFunctions;

public class EXBarrier extends Barrier {

	public EXBarrier() {
		super("EX/MEM");
		this.ports += 14;

		Bounds bounds = Bounds.create(-30, -260, 60, 520);
		this.setOffsetBounds(bounds);

		int distances[] = LsdFunctions.getDistanceFromMiddle(bounds);

		List<Port> ps = new ArrayList<Port>();

		// Base ports
		ps.add(new Port(-10, distances[1], Port.INPUT, 1)); // Clock
		ps.add(new Port(10, distances[1], Port.INPUT, 1)); // Clear

		// Relative ports
		int i = 0;

		// upper
		for (i = 2; i <= 8; i += 2) {
			addPort(ps, i, -distances[0], -distances[1] + i * 10, 1);
		}

		// middle
		addPort(ps, 10, -distances[0], 10, 32);
		addPort(ps, 12, -distances[0], 70, 32);

		// bottom
		addPort(ps, 14, -distances[0], 190, 5);

		this.setPorts(ps);
	}

	@Override
	protected void addPort(List<Port> list, int index, int posx, int posy, int width) {
		super.addPort(list, -1, posx, posy, width);
	}

}
