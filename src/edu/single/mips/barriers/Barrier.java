package edu.single.mips.barriers;

import java.util.ArrayList;
import java.util.List;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;

import edu.single.funcoes.LsdFunctions;

public abstract class Barrier extends InstanceFactory {

	protected byte ports = 2; // don't need to think of clock as input
	protected static final byte CLOCKINDEX = 0;
	protected static final byte CLEARINDEX = 1;
	protected boolean oldClockState = false;
	
	protected Barrier(String name, int ports) {
		super(name);
		this.ports = (byte)ports;
		
		int height = (ports - 2) * 10 + 20;
		Bounds bounds = Bounds.create(-20, -height / 2, 40, height);
		
		this.setOffsetBounds(bounds);
		
		initPorts(bounds);
	}
	
	protected Barrier(String name) {
		super(name);
	}
	
	protected void initPorts(Bounds bounds) {
		List<Port> portList = new ArrayList<Port>();
		
		// Base Ports
		portList.add(new Port(-10, -bounds.getY(), Port.INPUT, 1)); // Clock
		portList.add(new Port(10, -bounds.getY(), Port.INPUT, 1)); // Clear
		
		// add other ports
		for(int i = 2; i < this.ports; i += 2) {
			addPort(portList, i, bounds.getX(), (bounds.getY() - 10) + i * 10, 1);
		}
		
		this.setPorts(portList);
	}
	
	protected void addPort(List<Port> list, int index, int posx, int posy, int width) {
		list.add(new Port(posx, posy, Port.INPUT, width));
		list.add(new Port(-posx, posy, Port.INPUT, width));
	}

	@Override
	public void propagate(InstanceState state) {
		boolean clockState = state.getPortValue(CLOCKINDEX).toIntValue() == 1;
		boolean clearState = state.getPortValue(CLEARINDEX).toIntValue() == 1;
		
		if(clearState) {
			// Reset all values
			for(int i = 2; i < ports; i += 2) {
				state.setPort(i + 1, Value.createKnown(state.getPortValue(i).getBitWidth(), 0), 300);
			}
		}
		
		if(clockState != oldClockState) {
			oldClockState = clockState;
			if(clockState) {
				for(int i = 2; i < ports; i += 2) {
					Value inputValue = state.getPortValue(i);
					state.setPort(i + 1, inputValue, 32);
				}
			}
		}
	}
	
	@Override
	public void paintInstance(InstancePainter painter) {
		painter.drawPort(CLOCKINDEX, "clk", Direction.SOUTH);
		painter.drawPort(CLEARINDEX, "clr", Direction.SOUTH);
		
		for(int i = 2; i < ports; i += 2) {
			int width = painter.getPortValue(i).getBitWidth().getWidth();
			painter.drawPort(i, width + "", Direction.EAST);
			painter.drawPort(i + 1, width + "", Direction.WEST);
		}
		
		painter.drawBounds();
		LsdFunctions.setTitle(painter, this.getName());
	}

}
